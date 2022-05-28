package est.money.mannager.api.repositories;

import est.money.mannager.api.models.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    List<Transaction> findByDateAndUserId(Date date, long userId);
}
