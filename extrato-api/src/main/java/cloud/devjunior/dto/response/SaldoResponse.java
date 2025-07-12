package cloud.devjunior.dto.response;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(name = "SaldoResponse",
        description = "Resposta para consulta de saldo da conta bancária do usuário")
public record SaldoResponse(
        @Schema(description = "Saldo total da conta bancária do usuário")
        BigDecimal saldo
) {
}
