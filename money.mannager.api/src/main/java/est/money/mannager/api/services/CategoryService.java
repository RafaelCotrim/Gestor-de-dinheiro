package est.money.mannager.api.services;

import est.money.mannager.api.models.Budget;
import est.money.mannager.api.models.Category;
import est.money.mannager.api.repositories.BudgetRepository;
import est.money.mannager.api.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService extends BaseService<Category, CategoryRepository>{

    public Category update(Long id, Category newValue){

        return repo.findById(id)
                .map(category -> {
                    category.setName(newValue.getName());
                    return repo.save(category);
                })
                .orElseGet(() -> {
                    newValue.setId(id);
                    return repo.save(newValue);
                });
    }
}
