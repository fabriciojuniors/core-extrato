package cloud.devjunior.repository;

import cloud.devjunior.entity.Movimentacao;
import cloud.devjunior.entity.QMovimentacao;
import com.mysema.commons.lang.Pair;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.SimpleExpression;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;

import static cloud.devjunior.enums.TipoMovimentacao.ENTRADA;

@ApplicationScoped
public class MovimentacaoRepository extends QuerydslBaseRepository<Movimentacao, Long> implements PanacheRepository<Movimentacao> {

    @Override
    protected EntityPath<Movimentacao> getEntityPath() {
        return QMovimentacao.movimentacao;
    }

    @Override
    protected SimpleExpression<Long> getIdPath() {
        return QMovimentacao.movimentacao.id;
    }

    public BigDecimal calcularSaldoPorContaBancaria(Long contaBancariaId) {
        return queryFactory
                .selectFrom(getEntityPath())
                .where(QMovimentacao.movimentacao.contaBancaria.id.eq(contaBancariaId))
                .stream()
                .map(movimentacao -> Pair.of(movimentacao.getTipoMovimentacao(), movimentacao.getValor()))
                .reduce(BigDecimal.ZERO, (saldo, par) -> {
                    var tipo = par.getFirst();
                    var valor = par.getSecond();
                    return tipo == ENTRADA ? saldo.add(valor) : saldo.subtract(valor);
                }, BigDecimal::add);
    }
}
