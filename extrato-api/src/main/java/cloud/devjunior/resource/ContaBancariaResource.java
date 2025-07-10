package cloud.devjunior.resource;

import cloud.devjunior.dto.request.CadastroContaBancariaRequest;
import cloud.devjunior.dto.response.ConsultaPaginadaResponse;
import cloud.devjunior.dto.response.ContaBancariaResponse;
import cloud.devjunior.service.ContaBancariaService;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.ResponseStatus;

import static java.net.HttpURLConnection.HTTP_CREATED;

@Tag(name = "Conta Bancária",
        description = "Gerenciamento de contas bancárias")
@Path("/v1/contas-bancarias")
@Authenticated
public class ContaBancariaResource {

    @Inject
    ContaBancariaService contaBancariaService;

    @Operation(
            summary = "Cadastrar Conta Bancária",
            description = "Cria uma nova conta bancária associada a uma instituição financeira existente."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "201",
                    description = "Conta bancária criada com sucesso"
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Requisição inválida, verifique os dados fornecidos"
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Instituição financeira não encontrada"
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
    @POST
    @ResponseStatus(HTTP_CREATED)
    public void cadastrar(@RequestBody @Valid @NotNull CadastroContaBancariaRequest request) {
        contaBancariaService.criar(request);
    }

    @Operation(
            summary = "Consultar Contas Bancárias",
            description = "Consulta as contas bancárias associadas ao usuário autenticado."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Consulta realizada com sucesso, retornando as contas bancárias do usuário"
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
    public ConsultaPaginadaResponse<ContaBancariaResponse> findAll(@QueryParam("pagina") int pagina,
                                                                   @QueryParam("tamanho") int tamanho) {
        return contaBancariaService.findAll(pagina, tamanho);
    }
}
