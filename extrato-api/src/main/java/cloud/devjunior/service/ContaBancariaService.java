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
    UsuarioService usuarioService;

    @Inject
    ContaBancariaMapper contaBancariaMapper;

    @Transactional
    public void criar(CadastroContaBancariaRequest request) {
        validarContaBancariaExistente(request);
        var instituicaoFinanceira = instituicaoFinanceiraService.findById(request.instituicaoFinanceiraId());
        var usuario = usuarioService.findCurrent();
        var contaBancaria = contaBancariaMapper.fromCadastroContaBancariaRequest(request, instituicaoFinanceira, usuario);
        contaBancariaRepository.persistAndFlush(contaBancaria);
    }

    public void validarContaBancariaExistente(CadastroContaBancariaRequest request) {
        if (contaBancariaRepository.existsByNumeroContaAndAgenciaAndInstituicao(
                request.numero(),
                request.agencia(),
                request.instituicaoFinanceiraId())) {
            throw new IllegalArgumentException("Já existe uma conta bancária com os mesmos dados cadastrada.");
        }
    }
}
