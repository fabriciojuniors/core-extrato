package cloud.devjunior.repository;

import cloud.devjunior.entity.Movimentacao;
import cloud.devjunior.entity.QMovimentacao;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.SimpleExpression;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MovimentacaoRepository extends QuerydslBaseRepository<Movimentacao, Long> {

    @Override
    protected EntityPath<Movimentacao> getEntityPath() {
        return QMovimentacao.movimentacao;
    }

    @Override
    protected SimpleExpression<Long> getIdPath() {
        return QMovimentacao.movimentacao.id;
    }
}
