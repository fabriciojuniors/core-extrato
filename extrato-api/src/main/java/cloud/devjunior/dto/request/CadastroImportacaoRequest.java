package cloud.devjunior.dto.request;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "CadastroImportacaoRequest",
        description = "Dto para registro de importação de transações bancárias.")
public record CadastroImportacaoRequest(
        @Schema(description = "Nome do usuário que está realizando a importação.")
        String arquivo
) {
}
