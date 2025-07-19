package cloud.devjunior.service;

import cloud.devjunior.dto.request.CadastroImportacaoRequest;
import cloud.devjunior.mapper.ImportacaoMapper;
import cloud.devjunior.repository.ImportacaoRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RequestScoped
public class ImportacaoService {

    @Inject
    UsuarioService usuarioService;

    @Inject
    ContaBancariaService contaBancariaService;

    @Inject
    ImportacaoMapper importacaoMapper;

    @Inject
    ImportacaoRepository importacaoRepository;

    @Transactional
    public void criar(@Valid @NotNull CadastroImportacaoRequest request) {
        var usuario = usuarioService.findCurrent();
        var contaBancaria = contaBancariaService.findById(request.idContaBancaria());
        var importacao = importacaoMapper.fromCadastroImportacaoRequest(request, contaBancaria, usuario);
        importacaoRepository.persistAndFlush(importacao);
    }
}
