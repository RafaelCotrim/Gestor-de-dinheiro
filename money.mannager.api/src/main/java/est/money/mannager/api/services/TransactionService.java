package est.money.mannager.api.services;

import est.money.mannager.api.dtos.TransactionForUpdate;
import est.money.mannager.api.models.Category;
import est.money.mannager.api.models.Transaction;
import est.money.mannager.api.models.User;
import est.money.mannager.api.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;

@Service
public class TransactionService extends BaseService<Transaction, TransactionRepository>{

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    public Transaction update(Long id, Transaction newValue){

        return repo.findById(id)
                .map(transaction -> {
                    transaction.setCategory(newValue.getCategory());
                    transaction.setValue(newValue.getValue());
                    transaction.setDate(newValue.getDate());
                    return repo.save(transaction);
                })
                .orElseGet(() -> {
                    newValue.setId(id);
                    return repo.save(newValue);
                });
    }

    public List<Transaction> findByDateAndUserId(Date date, long userId){
        return repo.findByDateAndUserId(date, userId);
    }
}
