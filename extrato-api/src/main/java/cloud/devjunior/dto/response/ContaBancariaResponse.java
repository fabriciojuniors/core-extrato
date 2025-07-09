package cloud.devjunior.dto.response;

import cloud.devjunior.enums.TipoConta;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(name = "ContaBancariaResponse",
        description = "Resposta para a consulta de contas bancárias, contendo informações básicas sobre a conta.")
public record ContaBancariaResponse(

        @Schema(description = "ID da conta bancária")
        Long id,

        @Schema(description = "Número da conta bancária")
        int numero,

        @Schema(description = "Agência da conta bancária")
        int agencia,

        @Schema(description = "Instituição financeira associada à conta bancária")
        InstituicaoFinanceiraResponse instituicaoFinanceira,

        @Schema(description = "Tipo da conta bancária")
        TipoConta tipo,

        @Schema(description = "Saldo atual da conta bancária")
        BigDecimal saldo
) {
}
