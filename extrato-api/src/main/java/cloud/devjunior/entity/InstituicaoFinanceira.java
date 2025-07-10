package cloud.devjunior.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "instituicoes_bancarias")
@Data
public class InstituicaoFinanceira {

    @Id
    private long id;

    @Column(nullable = false)
    @NotNull(message = "O nome da instituição financeira não pode ser nulo.")
    private String nome;

    @Column(nullable = false, unique = true, updatable = false)
    @NotNull(message = "O CNPJ da instituição financeira não pode ser nulo.")
    private String cnpj;

    private String classe;
    private String segmento;
}
