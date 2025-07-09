package cloud.devjunior.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class Auditoria {

    @Column(nullable = false, updatable = false)
    private String criadoEm;

    @Column(nullable = true)
    private String modificadoEm;
}
