package cloud.devjunior.entity;

import cloud.devjunior.enums.SituacaoImportacao;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "importacoes")
@Data
public class Importacao extends Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "id_usuario", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Usuário não pode ser nulo.")
    private Usuario usuario;

    @JoinColumn(name = "id_conta_bancaria", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ContaBancaria contaBancaria;

    @Column(name = "data", nullable = false)
    private LocalDate data = LocalDate.now();

    @Column(name = "arquivo", nullable = false)
    @NotNull(message = "O arquivo de importação não pode ser nulo.")
    private String arquivo;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false)
    private SituacaoImportacao situacao = SituacaoImportacao.PENDENTE;
}
