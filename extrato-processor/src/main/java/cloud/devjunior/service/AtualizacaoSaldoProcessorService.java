package cloud.devjunior.service;

import cloud.devjunior.repository.MovimentacaoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AtualizacaoSaldoProcessorService {

    @Inject
    MovimentacaoRepository movimentacaoRepository;

    @Inject
    ContaBancariaService contaBancariaService;

    @Transactional
    public void processSaldo(Long contaBancariaId) {
        var saldoAtualizado = movimentacaoRepository.calcularSaldoPorContaBancaria(contaBancariaId);
        contaBancariaService.atualizarSaldo(contaBancariaId, saldoAtualizado);
    }
}
