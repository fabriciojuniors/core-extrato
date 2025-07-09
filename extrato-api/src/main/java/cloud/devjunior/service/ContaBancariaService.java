package cloud.devjunior.service;

import cloud.devjunior.dto.request.CadastroContaBancariaRequest;
import cloud.devjunior.mapper.ContaBancariaMapper;
import cloud.devjunior.repository.ContaBancariaRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@RequestScoped
public class ContaBancariaService {

    @Inject
    ContaBancariaRepository contaBancariaRepository;

    @Inject
    InstituicaoFinanceiraService instituicaoFinanceiraService;

    @Inject
    ContaBancariaMapper contaBancariaMapper;

    @Transactional
    public void criar(CadastroContaBancariaRequest request) {
        var instituicaoFinanceira = instituicaoFinanceiraService.findById(request.institutoFinanceiroId());
        var contaBancaria = contaBancariaMapper.fromCadastroContaBancariaRequest(request, instituicaoFinanceira);
        contaBancariaRepository.persistAndFlush(contaBancaria);
    }
}
