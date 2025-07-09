package cloud.devjunior.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "instituicoes_bancarias")
@Data
public class InstituicaoFinanceira {

    @Id
    private long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true, updatable = false)
    private String cnpj;

    private String classe;
    private String segmento;
}
