package cloud.devjunior.entity;

import cloud.devjunior.enums.TipoMovimentacao;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;

import java.math.BigDecimal;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "movimentacoes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movimentacao extends Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "id_conta_bancaria", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Conta bancária não pode ser nula.")
    private ContaBancaria contaBancaria;

    @JoinColumn(name = "id_conta_destino", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ContaBancaria contaBancariaDestino;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "valor", nullable = false, precision = 19, scale = 2)
    private BigDecimal valor;

    @Column(name = "informacoes_adicionais", nullable = false)
    private String informacoesAdicionais;

    @Column(name = "tipo_movimentacao", nullable = false, columnDefinition = "tipo_movimentacao")
    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private TipoMovimentacao tipoMovimentacao;
}
