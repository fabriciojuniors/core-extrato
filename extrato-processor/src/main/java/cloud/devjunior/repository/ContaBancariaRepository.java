package cloud.devjunior.repository;

import cloud.devjunior.entity.ContaBancaria;
import cloud.devjunior.entity.QContaBancaria;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.webcohesion.ofx4j.domain.data.banking.BankAccountDetails;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class ContaBancariaRepository extends QuerydslBaseRepository<ContaBancaria, Long> implements PanacheRepository<ContaBancaria> {

    @Override
    protected EntityPath<ContaBancaria> getEntityPath() {
        return QContaBancaria.contaBancaria;
    }

    @Override
    protected SimpleExpression<Long> getIdPath() {
        return QContaBancaria.contaBancaria.id;
    }

    public Optional<ContaBancaria> findByBankAccountDetails(BankAccountDetails bankAccountDetails) {
        return Optional.ofNullable(
          queryFactory.selectFrom(getEntityPath())
                  .where(QContaBancaria.contaBancaria.numero.stringValue().eq(bankAccountDetails.getAccountNumber().replace("-", ""))
                          .and(QContaBancaria.contaBancaria.agencia.stringValue().eq(bankAccountDetails.getBranchId().replace("-", "")))
                          .and(QContaBancaria.contaBancaria.instituicaoFinanceira.id.eq(Long.parseLong(bankAccountDetails.getBankId()))))
              .fetchFirst()
        );
    }
}
