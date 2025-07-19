package cloud.devjunior.resource;

import cloud.devjunior.dto.request.CadastroImportacaoRequest;
import cloud.devjunior.service.ImportacaoService;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.ResponseStatus;

import static java.net.HttpURLConnection.HTTP_CREATED;

@Tag(name = "Importação",
        description = "Gerenciamento de importações de transações bancárias")
@Path("/v1/importacoes")
@Authenticated
public class ImportacaoResource {

    @Inject
    ImportacaoService importacaoService;

    @Operation(
            summary = "Cadastrar importação",
            description = "Cria uma nova importação."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "201",
                    description = "Importação criada com sucesso"
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Requisição inválida, verifique os dados fornecidos"
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
    public void cadastrar(@RequestBody @Valid @NotNull CadastroImportacaoRequest request) {
        importacaoService.criar(request);
    }
}
