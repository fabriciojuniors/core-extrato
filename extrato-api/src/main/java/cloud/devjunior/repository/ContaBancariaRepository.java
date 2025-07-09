package cloud.devjunior.repository;

import cloud.devjunior.entity.ContaBancaria;
import cloud.devjunior.entity.QContaBancaria;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.SimpleExpression;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class ContaBancariaRepository extends QuerydslBaseRepository<ContaBancaria, Long> implements PanacheRepository<ContaBancaria> {

    @Override
    protected EntityPath<ContaBancaria> getEntityPath() {
        return QContaBancaria.contaBancaria;
    }

    @Override
    protected SimpleExpression<Long> getIdPath() {
        return QContaBancaria.contaBancaria.id;
    }

    public boolean existsByNumeroContaAndAgenciaAndInstituicao(int numeroConta, int agencia, Long instituicaoId) {
        return queryFactory.selectFrom(getEntityPath())
                .where(QContaBancaria.contaBancaria.numero.eq(numeroConta)
                        .and(QContaBancaria.contaBancaria.agencia.eq(agencia))
                        .and(QContaBancaria.contaBancaria.instituicaoFinanceira.id.eq(instituicaoId)))
                .fetchFirst() != null;
    }
}
