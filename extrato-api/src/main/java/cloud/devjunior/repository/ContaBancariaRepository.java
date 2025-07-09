package cloud.devjunior.repository;

import cloud.devjunior.entity.ContaBancaria;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class ContaBancariaRepository implements PanacheRepository<ContaBancaria> {
}
