package cloud.devjunior.dto.request;

import cloud.devjunior.enums.TipoConta;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(title = "Cadastro de Conta Bancária",
        description = "Requisição para cadastrar uma nova conta bancária.")
public record CadastroContaBancariaRequest(

        @Schema(description = "Número da conta bancária")
        @NotNull(message = "O número da conta bancária deve ser informado.")
        @Positive(message = "O número da conta bancária deve ser um valor positivo.")
        int numero,

        @Schema(description = "Número da agência bancária")
        @NotNull(message = "O número da agência bancária deve ser informado.")
        @Positive(message = "O número da agência bancária deve ser um valor positivo.")
        int agencia,

        @Schema(description = "ID da instituição financeira")
        @NotNull(message = "O ID da instituição financeira deve ser informado.")
        @Positive(message = "O ID da instituição financeira deve ser um valor positivo.")
        Long instituicaoFinanceiraId,

        @Schema(description = "Tipo da conta bancária")
        @NotNull(message = "O tipo da conta bancária deve ser informado.")
        TipoConta tipo
) {
}
