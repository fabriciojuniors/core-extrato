package cloud.devjunior.resource;

import cloud.devjunior.dto.response.ConsultaPaginadaResponse;
import cloud.devjunior.dto.response.ContaBancariaResponse;
import cloud.devjunior.dto.response.InstituicaoFinanceiraResponse;
import cloud.devjunior.service.InstituicaoFinanceiraService;
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

@Tag(name = "Instituição Financeira",
        description = "Gerenciamento de instituições financeiras")
@Path("/v1/instituicoes-financeiras")
@Authenticated
public class InstituicaoFinanceiraResource {

    @Inject
    InstituicaoFinanceiraService instituicaoFinanceiraService;

    @Operation(
            summary = "Consultar instituições financeiras",
            description = "Consulta as instituições financeiras."
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
    public ConsultaPaginadaResponse<InstituicaoFinanceiraResponse> findAll(@QueryParam("pagina") int pagina,
                                                                           @QueryParam("tamanho") int tamanho) {
        return instituicaoFinanceiraService.findAll(pagina, tamanho);
    }

}
