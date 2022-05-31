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

    public Transaction update(Long id, TransactionForUpdate newValue){


        return repo.findById(id)
                .map(transaction -> {
                    transaction.setCategory(categoryService.findOrNull(newValue.categoryId));
                    transaction.setValue(newValue.value);
                    transaction.setDate(newValue.date);
                    return repo.save(transaction);
                })
                .orElseGet(() -> {
                    User u = userService.find(newValue.userId);
                    Category c = categoryService.findOrNull(newValue.categoryId);
                    Transaction t = new Transaction(newValue.value, newValue.date, u, c);
                    t.setId(id);
                    return repo.save(t);
                });
    }

    public List<Transaction> findByDateAndUserId(Date date, long userId){
        return repo.findByDateAndUserId(date, userId);
    }
}
