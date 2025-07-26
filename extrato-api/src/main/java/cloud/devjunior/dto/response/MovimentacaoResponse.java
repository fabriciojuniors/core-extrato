package cloud.devjunior.dto.response;

import cloud.devjunior.enums.TipoMovimentacao;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(name = "MovimentacaoResponse",
        description = "Representa uma movimentação bancária.")
public record MovimentacaoResponse(
        BigDecimal valor,
        TipoMovimentacao tipo,
        LocalDate data,
        String informacoesAdicionais,
        ContaBancariaResponse contaBancaria
) {
}
