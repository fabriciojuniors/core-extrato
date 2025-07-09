package cloud.devjunior.entity;

import cloud.devjunior.enums.TipoConta;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "contas_bancarias")
@Data
public class ContaBancaria extends Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "id_usuario", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuario;

    @Column(name = "numero", nullable = false)
    private int numero;

    @Column(name = "agencia", nullable = false)
    private int agencia;

    @JoinColumn(name = "id_instituicao_financeira", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private InstituicaoFinanceira instituicaoFinanceira;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private TipoConta tipo;

    @Column(name = "saldo", nullable = false, precision = 19, scale = 2)
    private BigDecimal saldo;
}
