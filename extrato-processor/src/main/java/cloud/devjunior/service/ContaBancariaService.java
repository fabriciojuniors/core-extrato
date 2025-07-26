package cloud.devjunior.service;

import cloud.devjunior.entity.ContaBancaria;
import cloud.devjunior.entity.Usuario;
import cloud.devjunior.enums.TipoConta;
import cloud.devjunior.repository.ContaBancariaRepository;
import com.webcohesion.ofx4j.domain.data.banking.BankAccountDetails;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.math.BigDecimal;
import java.util.Optional;

@ApplicationScoped
public class ContaBancariaService {

    @Inject
    ContaBancariaRepository contaBancariaRepository;

    @Inject
    InstituicaoFinanceiraService instituicaoFinanceiraService;

    @Transactional
    public void atualizarSaldo(Long contaBancariaId, BigDecimal saldoAtual) {
        var contaBancaria = Optional.ofNullable(contaBancariaRepository.findById(contaBancariaId))
                .orElseThrow(() -> new NotFoundException("Conta bancária não encontrada com ID: " + contaBancariaId));

        contaBancaria.setSaldo(saldoAtual);
        contaBancariaRepository.persist(contaBancaria);
    }

    @Transactional
    public ContaBancaria findOrCreateByBankAccountDetails(BankAccountDetails bankAccountDetails,
                                                          Usuario usuario) {
        var contaBancariaOpt = contaBancariaRepository.findByBankAccountDetails(bankAccountDetails);
        return contaBancariaOpt.orElseGet(() -> createContaBancaria(bankAccountDetails, usuario));
    }

    private ContaBancaria createContaBancaria(BankAccountDetails bankAccountDetails,
                                              Usuario usuario) {
        ContaBancaria contaBancaria = ContaBancaria.builder()
                .tipo(TipoConta.CORRENTE)
                .agencia(Integer.parseInt(bankAccountDetails.getBranchId().replace("-", "")))
                .numero(Integer.parseInt(bankAccountDetails.getAccountNumber().replace("-", "")))
                .instituicaoFinanceira(instituicaoFinanceiraService.findById(Integer.parseInt(bankAccountDetails.getBankId())))
                .usuario(usuario)
                .saldo(BigDecimal.ZERO)
                .build();

        contaBancariaRepository.persistAndFlush(contaBancaria);
        return contaBancaria;
    }
}
