package cloud.devjunior.resource;

import cloud.devjunior.dto.response.ConsultaPaginadaResponse;
import cloud.devjunior.dto.response.MovimentacaoResponse;
import cloud.devjunior.service.MovimentacaoService;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Tag(name = "Movimentação bancária",
        description = "Gerenciamento de movimentações bancárias")
@Path("/v1/movimentacoes")
@Authenticated
public class MovimentacaoResource {

    @Inject
    MovimentacaoService movimentacaoService;

    @Operation(
            summary = "Consultar movimentações bancárias",
            description = "Consulta as movimentações bancárias."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Consulta realizada com sucesso"
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "Usuário não autenticado"
            ),
            @APIResponse(
                    responseCode = "403",
                    description = "Usuário não autorizado a realizar esta operação"
            )
    })
    @RolesAllowed("user")
    @GET
    public ConsultaPaginadaResponse<MovimentacaoResponse> findAll(@QueryParam("pagina") int pagina,
                                                                  @QueryParam("tamanho") int tamanho) {
        return movimentacaoService.buscarPaginado(pagina, tamanho);
    }

}
