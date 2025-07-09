package cloud.devjunior.dto.response;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Schema(name = "ConsultaPaginadaResponse",
        description = "Resposta para consultas paginadas, contendo os dados da página atual, total de páginas e total de registros.")
public record ConsultaPaginadaResponse<T>(

        @Schema(description = "Número da página atual")
        int paginaAtual,

        @Schema(description = "Total de páginas disponíveis")
        int totalPaginas,

        @Schema(description = "Total de registros disponíveis")
        long totalRegistros,

        @Schema(description = "Dados da página atual")
        List<T> dados
) {
}
