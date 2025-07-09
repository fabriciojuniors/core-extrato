package cloud.devjunior.dto.response;

import java.time.LocalDateTime;

public record FalhaRequisicao(LocalDateTime dataHora,
                              String mensagem) {
}
