package cloud.devjunior.service;

import cloud.devjunior.entity.Importacao;
import cloud.devjunior.entity.Movimentacao;
import cloud.devjunior.enums.SituacaoImportacao;
import cloud.devjunior.enums.TipoMovimentacao;
import cloud.devjunior.repository.ImportacaoRepository;
import cloud.devjunior.repository.MovimentacaoRepository;
import cloud.devjunior.utils.AmazonS3Utils;
import com.webcohesion.ofx4j.domain.data.MessageSetType;
import com.webcohesion.ofx4j.domain.data.ResponseEnvelope;
import com.webcohesion.ofx4j.domain.data.banking.BankingResponseMessageSet;
import com.webcohesion.ofx4j.domain.data.common.Transaction;
import com.webcohesion.ofx4j.io.AggregateUnmarshaller;
import com.webcohesion.ofx4j.io.OFXParseException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import static cloud.devjunior.utils.StringUtils.corrigirEncoding;

@ApplicationScoped
public class ImportacaoProcessorService extends AmazonS3Utils {

    @Inject
    ImportacaoRepository importacaoRepository;

    @Inject
    MovimentacaoRepository movimentacaoRepository;

    @Channel("topic-process-saldo")
    Emitter<Long> atualizacaoSaldoEmitter;

    @Transactional
    public void processar(Long id) {
        Importacao importacao = Optional.ofNullable(importacaoRepository.findById(id))
                .orElseThrow(() -> new NotFoundException("Importação não encontrada com ID: " + id));
        try {
            var arquivoOfx = s3Client.getObjectAsBytes(buildGetRequest(importacao.getArquivo()));
            processarArquivo(arquivoOfx, importacao);

            importacao.setSituacao(SituacaoImportacao.CONCLUIDA);
            importacaoRepository.persist(importacao);

            atualizacaoSaldoEmitter.send(importacao.getContaBancaria().getId());
        } catch (Exception e) {
            importacao.setSituacao(SituacaoImportacao.ERRO);
            importacaoRepository.persist(importacao);
        }
    }

    private void processarArquivo(ResponseBytes<GetObjectResponse> arquivoResponse, Importacao importacao) throws IOException, OFXParseException {
        var stringBruta = new String(arquivoResponse.asByteArray(), StandardCharsets.UTF_8);
        try (var inputStream = new ByteArrayInputStream(stringBruta.getBytes(StandardCharsets.UTF_8))) {
            var aggregateUnmarshaller = new AggregateUnmarshaller<>(ResponseEnvelope.class);
            var response = aggregateUnmarshaller.unmarshal(inputStream);
            var message = response.getMessageSet(MessageSetType.banking);

            if (message == null) {
                throw new RuntimeException("Não foi possível ler as transações do extrato");
            }

            var responses = ((BankingResponseMessageSet) message).getStatementResponses();
            var bankTransaction = responses.stream().findFirst();

            if (bankTransaction.isPresent()) {
                var bank = bankTransaction.get();
                var transactions = bank.getMessage().getTransactionList().getTransactions();
                for (Transaction transaction : transactions) {
                    criarMovimentacao(importacao, transaction);
                }
            }
        } catch (Exception ex) {
            System.out.println("Erro ao processar arquivo OFX: " + ex.getMessage());
            throw ex;
        }
    }

    private void criarMovimentacao(Importacao importacao, Transaction transaction) {
        var movimentacao = Movimentacao.builder()
                .informacoesAdicionais(corrigirEncoding(transaction.getMemo()))
                .valor(transaction.getBigDecimalAmount())
                .contaBancaria(importacao.getContaBancaria())
                .data(LocalDate.ofInstant(transaction.getDatePosted().toInstant(), ZoneId.systemDefault()))
                .tipoMovimentacao(TipoMovimentacao.fromTrantype(transaction.getTransactionType()))
                .build();

        movimentacaoRepository.persist(movimentacao);
    }
}
