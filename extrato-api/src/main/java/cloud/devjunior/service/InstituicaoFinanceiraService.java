package cloud.devjunior.service;

import cloud.devjunior.entity.InstituicaoFinanceira;
import cloud.devjunior.repository.InstituicaoFinanceiraRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@RequestScoped
public class InstituicaoFinanceiraService {

    @Inject
    InstituicaoFinanceiraRepository instituicaoFinanceiraRepository;

    public InstituicaoFinanceira findById(long id) {
        return instituicaoFinanceiraRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Instituição financeira não encontrada"));
    }
}
