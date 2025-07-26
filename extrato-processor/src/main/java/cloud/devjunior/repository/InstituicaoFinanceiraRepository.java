package cloud.devjunior.repository;

import cloud.devjunior.entity.InstituicaoFinanceira;
import cloud.devjunior.entity.QInstituicaoFinanceira;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.SimpleExpression;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InstituicaoFinanceiraRepository extends QuerydslBaseRepository<InstituicaoFinanceira, Long> implements PanacheRepository<InstituicaoFinanceira> {

    @Override
    protected EntityPath<InstituicaoFinanceira> getEntityPath() {
        return QInstituicaoFinanceira.instituicaoFinanceira;
    }

    @Override
    protected SimpleExpression<Long> getIdPath() {
        return QInstituicaoFinanceira.instituicaoFinanceira.id;
    }
}
