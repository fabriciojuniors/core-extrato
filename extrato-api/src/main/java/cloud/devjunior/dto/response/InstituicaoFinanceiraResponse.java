package cloud.devjunior.dto.response;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "InstituicaoFinanceiraResponse",
        description = "Resposta para a consulta de instituições financeiras, contendo informações básicas sobre a instituição.")
public record InstituicaoFinanceiraResponse(
        @Schema(description = "ID da instituição financeira")
        Long id,

        @Schema(description = "Nome da instituição financeira")
        String nome
) {
}
