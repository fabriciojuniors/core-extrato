package cloud.devjunior.entity;

import cloud.devjunior.enums.TipoConta;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "contas_bancarias")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContaBancaria extends Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "id_usuario", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Usuário não pode ser nulo.")
    private Usuario usuario;

    @Column(name = "numero", nullable = false)
    @NotNull(message = "O número da conta bancária deve ser informado.")
    @Positive(message = "O número da conta bancária deve ser um valor positivo.")
    private int numero;

    @Column(name = "agencia", nullable = false)
    @NotNull(message = "O número da agência bancária deve ser informado.")
    @Positive(message = "O número da agência bancária deve ser um valor positivo.")
    private int agencia;

    @JoinColumn(name = "id_instituicao_financeira", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Instituição financeira não pode ser nula.")
    @Valid
    private InstituicaoFinanceira instituicaoFinanceira;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @NotNull(message = "O tipo da conta bancária deve ser informado.")
    private TipoConta tipo;

    @Column(name = "saldo", nullable = false, precision = 19, scale = 2)
    @NotNull(message = "O saldo da conta bancária deve ser informado.")
    private BigDecimal saldo;
}
