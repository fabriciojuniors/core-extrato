package cloud.devjunior.dto.request;

import cloud.devjunior.enums.TipoConta;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(title = "Cadastro de Conta Bancária",
        description = "Requisição para cadastrar uma nova conta bancária.")
public record CadastroContaBancariaRequest(int numero,
                                           int agencia,
                                           Long instituicaoFinanceiraId,
                                           TipoConta tipo) {
}
