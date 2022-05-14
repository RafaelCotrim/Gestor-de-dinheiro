package est.money.mannager.api.services;

import est.money.mannager.api.models.Transaction;
import est.money.mannager.api.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class TransactionService extends BaseService<Transaction, TransactionRepository>{

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction update(Long id, Transaction newValue){

        return transactionRepository.findById(id)
                .map(transaction -> {
                    transaction.setCategory(newValue.getCategory());
                    transaction.setValue(newValue.getValue());
                    return transactionRepository.save(transaction);
                })
                .orElseGet(() -> {
                    newValue.setId(id);
                    return transactionRepository.save(newValue);
                });
    }
}
