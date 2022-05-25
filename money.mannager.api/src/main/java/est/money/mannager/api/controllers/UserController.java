package est.money.mannager.api.controllers;

import est.money.mannager.api.models.Budget;
import est.money.mannager.api.models.Category;
import est.money.mannager.api.models.Transaction;
import est.money.mannager.api.models.User;
import est.money.mannager.api.repositories.BudgetRepository;
import est.money.mannager.api.repositories.CategoryRepository;
import est.money.mannager.api.services.TransactionService;
import est.money.mannager.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    /*
    @PostMapping
    public User save(@RequestBody User user) {
        return userService.save(user);
    }
    */

    @GetMapping("/{id}")
    public User findUser(@PathVariable long id) {
        return userService.find(id);
    }

    @PutMapping("/{id}")
    public User updateUser(@RequestBody User newUser, @PathVariable Long id) {

        return userService.update(id, newUser);
    }

    @DeleteMapping("/{id}")
    void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }

    @GetMapping("/{id}/transactions")
    public List<Transaction> findUserTransactions(@PathVariable long id) {
        return userService.find(id).getTransactions();
    }

    @PostMapping("/{id}/transactions")
    public Transaction addTransactionToUser(@PathVariable Long id, @RequestBody Transaction newTransaction){
        User u = userService.find(id);
        newTransaction.setUser(u);
        return transactionService.save(newTransaction);
    }

    @GetMapping("/{id}/categories")
    public List<Category> findUserCategories(@PathVariable long id) {
        return userService.find(id).getCategories();
    }

    @PostMapping("/{id}/categories")
    public Category addCategoryToUser(@PathVariable Long id, @RequestBody Category newCategory){
        User u = userService.find(id);
        newCategory.setUser(u);
        return categoryRepository.save(newCategory);
    }

    @GetMapping("/{id}/budgets")
    public List<Budget> getUserBudgets(@PathVariable long id) {
        return userService.find(id).getBudgets();
    }

    @PostMapping("/{id}/budgets")
    public Budget addBudgetToUser(@PathVariable Long id, @RequestBody Budget newBudget){
        User u = userService.find(id);
        newBudget.setUser(u);
        return budgetRepository.save(newBudget);
    }
}
