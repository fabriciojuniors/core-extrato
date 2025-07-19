package cloud.devjunior.enums;

import com.webcohesion.ofx4j.domain.data.common.TransactionType;
import jakarta.validation.constraints.NotNull;

public enum TipoMovimentacao {

    ENTRADA,
    SAIDA,
    TRANSFERENCIA;

    public static TipoMovimentacao fromTrantype(@NotNull TransactionType transactionType) {
        return switch (transactionType) {
            case CREDIT -> ENTRADA;
            case DEBIT -> SAIDA;
            default -> throw new IllegalArgumentException("Transação não suportada: " + transactionType);
        };
    }
}
