package cloud.devjunior.service;

import cloud.devjunior.dto.request.CadastroContaBancariaRequest;
import cloud.devjunior.dto.response.ConsultaPaginadaResponse;
import cloud.devjunior.dto.response.ContaBancariaResponse;
import cloud.devjunior.entity.QContaBancaria;
import cloud.devjunior.mapper.ContaBancariaMapper;
import cloud.devjunior.repository.ContaBancariaRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

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
    public void criar(@Valid @NotNull CadastroContaBancariaRequest request) {
        validarContaBancariaExistente(request);
        var instituicaoFinanceira = instituicaoFinanceiraService.findById(request.instituicaoFinanceiraId());
        var usuario = usuarioService.findCurrent();
        var contaBancaria = contaBancariaMapper.fromCadastroContaBancariaRequest(request, instituicaoFinanceira, usuario);
        contaBancariaRepository.persistAndFlush(contaBancaria);
    }

    public ConsultaPaginadaResponse<ContaBancariaResponse> findAll(int pagina, int tamanho) {
        var usuario = usuarioService.findCurrent();
        var filtro = QContaBancaria.contaBancaria.usuario.eq(usuario);
        var consultaPaginada = contaBancariaRepository.findAll(pagina, tamanho, filtro);

        return new ConsultaPaginadaResponse<>(
                consultaPaginada.paginaAtual(),
                consultaPaginada.totalPaginas(),
                consultaPaginada.totalRegistros(),
                consultaPaginada.dados().stream()
                        .map(contaBancariaMapper::fromContaBancaria)
                        .toList()
        );
    }

    private void validarContaBancariaExistente(CadastroContaBancariaRequest request) {
        if (contaBancariaRepository.existsByNumeroContaAndAgenciaAndInstituicao(
                request.numero(),
                request.agencia(),
                request.instituicaoFinanceiraId())) {
            throw new IllegalArgumentException("Já existe uma conta bancária com os mesmos dados cadastrada.");
        }
    }
}
