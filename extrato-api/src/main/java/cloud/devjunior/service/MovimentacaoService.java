package cloud.devjunior.service;

import cloud.devjunior.dto.response.ConsultaPaginadaResponse;
import cloud.devjunior.dto.response.MovimentacaoResponse;
import cloud.devjunior.mapper.MovimentacaoMapper;
import cloud.devjunior.repository.MovimentacaoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class MovimentacaoService {

    @Inject
    MovimentacaoRepository movimentacaoRepository;
    @Inject
    MovimentacaoMapper movimentacaoMapper;

    public ConsultaPaginadaResponse<MovimentacaoResponse> buscarPaginado(int pagina, int tamanho) {
        var paginada = movimentacaoRepository.findAll(pagina, tamanho, null);
        List<MovimentacaoResponse> dados = paginada.dados().stream()
                .map(movimentacaoMapper::toResponse)
                .collect(Collectors.toList());
        return new ConsultaPaginadaResponse<>(
                paginada.paginaAtual(),
                paginada.totalPaginas(),
                paginada.totalRegistros(),
                dados
        );
    }
}
