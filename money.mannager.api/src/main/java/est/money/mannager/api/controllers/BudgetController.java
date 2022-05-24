package est.money.mannager.api.controllers;

import est.money.mannager.api.models.Budget;
import est.money.mannager.api.services.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/budgets")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @GetMapping
    public List<Budget> findAll() {
        return budgetService.findAll();
    }

    @PostMapping
    public Budget save(@RequestBody Budget budget) {
        return budgetService.save(budget);
    }

    @GetMapping("/{id}")
    public Budget find(@PathVariable long id) {
        return budgetService.find(id);
    }

    @PutMapping("/{id}")
    public Budget update(@PathVariable Long id, @RequestBody Budget newValue) {
        return budgetService.update(id, newValue);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        budgetService.delete(id);
    }
}
