package cloud.devjunior.repository;

import cloud.devjunior.entity.Importacao;
import cloud.devjunior.entity.QImportacao;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.SimpleExpression;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ImportacaoRepository extends QuerydslBaseRepository<Importacao, Long> implements PanacheRepository<Importacao> {

    @Override
    protected EntityPath<Importacao> getEntityPath() {
        return QImportacao.importacao;
    }

    @Override
    protected SimpleExpression<Long> getIdPath() {
        return QImportacao.importacao.id;
    }
}
