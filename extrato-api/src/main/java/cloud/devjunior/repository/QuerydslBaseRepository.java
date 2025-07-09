package cloud.devjunior.repository;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class QuerydslBaseRepository<T, PK extends Comparable<PK>> {

    @Inject
    EntityManager em;

    protected JPAQueryFactory queryFactory;

    @PostConstruct
    void init() {
        queryFactory = new JPAQueryFactory(em);
    }

    protected abstract EntityPath<T> getEntityPath();

    protected abstract SimpleExpression<PK> getIdPath();

    public T findById(PK id) {
        return queryFactory.selectFrom(getEntityPath())
                .where(getIdPath().eq(id))
                .fetchOne();
    }
}

