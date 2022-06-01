package est.money.mannager.api.controllers;

import est.money.mannager.api.dtos.BudgetDto;
import est.money.mannager.api.dtos.BudgetForCreate;
import est.money.mannager.api.dtos.BudgetForUpdate;
import est.money.mannager.api.models.Budget;
import est.money.mannager.api.models.Category;
import est.money.mannager.api.models.User;
import est.money.mannager.api.services.BudgetService;
import est.money.mannager.api.services.CategoryService;
import est.money.mannager.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CONFLICT;

@RestController
@RequestMapping("/budgets")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<BudgetDto> findAll() {
        return budgetService.findAll().stream().map(BudgetDto::from).collect(Collectors.toList());
    }

    @PostMapping
    public BudgetDto save(@RequestBody BudgetForCreate bfc) {

        User u = userService.find(bfc.userId);
        Category c = categoryService.find(bfc.categoryId);
        boolean collision = u.getBudgets().stream().map(b -> b.getCategory().getId()).anyMatch(v -> v == bfc.categoryId);

        if(collision){
            throw  new ResponseStatusException(CONFLICT);
        }

        return BudgetDto.from(budgetService.save(new Budget(bfc.value, u, c)));
    }

    @GetMapping("/{id}")
    public BudgetDto find(@PathVariable long id) {
        return BudgetDto.from(budgetService.find(id));
    }

    @PutMapping("/{id}")
    public BudgetDto update(@PathVariable Long id, @RequestBody BudgetForUpdate newValue) {

        Budget b = new Budget();

        User u = userService.findOrNull(newValue.userId);
        Category c = categoryService.find(newValue.categoryId);

        b.setUser(u);
        b.setCategory(c);
        b.setValue(newValue.value);

        return BudgetDto.from(budgetService.update(id, b));
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        budgetService.delete(id);
    }
}
