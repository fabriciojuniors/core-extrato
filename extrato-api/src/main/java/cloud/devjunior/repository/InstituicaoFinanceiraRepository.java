package cloud.devjunior.repository;

import cloud.devjunior.entity.InstituicaoFinanceira;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class InstituicaoFinanceiraRepository implements PanacheRepository<InstituicaoFinanceira> {
}
