package cloud.devjunior.service;

import cloud.devjunior.dto.response.ConsultaPaginadaResponse;
import cloud.devjunior.dto.response.InstituicaoFinanceiraResponse;
import cloud.devjunior.entity.InstituicaoFinanceira;
import cloud.devjunior.entity.QInstituicaoFinanceira;
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

    public ConsultaPaginadaResponse<InstituicaoFinanceiraResponse> findAll(int pagina, int tamanho, String nome) {
        var filtro = nome != null ? QInstituicaoFinanceira.instituicaoFinanceira.nome.containsIgnoreCase(nome) : null;
        var consultaPaginada = instituicaoFinanceiraRepository.findAll(pagina, tamanho, filtro);
        return new ConsultaPaginadaResponse<>(
                consultaPaginada.paginaAtual(),
                consultaPaginada.totalPaginas(),
                consultaPaginada.totalRegistros(),
                consultaPaginada.dados().stream()
                        .map(institucaoFinanceira -> new InstituicaoFinanceiraResponse(institucaoFinanceira.getId(), institucaoFinanceira.getNome()))
                        .toList()
        );
    }
}
