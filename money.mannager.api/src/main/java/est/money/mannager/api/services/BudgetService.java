package est.money.mannager.api.services;

import est.money.mannager.api.models.Budget;
import est.money.mannager.api.models.Transaction;
import est.money.mannager.api.repositories.BudgetRepository;
import est.money.mannager.api.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BudgetService extends BaseService<Budget, BudgetRepository>{

    public Budget update(Long id, Budget newValue){

        return repo.findById(id)
                .map(budget -> {
                    budget.setCategory(newValue.getCategory());
                    budget.setValue(newValue.getValue());
                    budget.setUser(newValue.getUser());
                    return repo.save(budget);
                })
                .orElseGet(() -> {
                    newValue.setId(id);
                    return repo.save(newValue);
                });
    }
}
