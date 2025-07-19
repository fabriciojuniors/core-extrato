package cloud.devjunior.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SituacaoImportacao {
    PENDENTE("Pendente"),
    PROCESSANDO("Processando"),
    CONCLUIDA("Concluída"),
    ERRO("Erro");

    private final String descricao;
}
