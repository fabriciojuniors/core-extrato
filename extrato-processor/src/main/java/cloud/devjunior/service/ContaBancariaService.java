package cloud.devjunior.service;

import cloud.devjunior.repository.ContaBancariaRepository;
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

    @Transactional
    public void atualizarSaldo(Long contaBancariaId, BigDecimal saldoAtual) {
        var contaBancaria = Optional.ofNullable(contaBancariaRepository.findById(contaBancariaId))
                .orElseThrow(() -> new NotFoundException("Conta bancária não encontrada com ID: " + contaBancariaId));

        contaBancaria.setSaldo(saldoAtual);
        contaBancariaRepository.persist(contaBancaria);
    }
}
