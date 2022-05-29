package est.money.mannager.api.controllers;

import est.money.mannager.api.dtos.CategoryDto;
import est.money.mannager.api.dtos.TransactionDto;
import est.money.mannager.api.models.Budget;
import est.money.mannager.api.models.Category;
import est.money.mannager.api.models.Transaction;
import est.money.mannager.api.models.User;
import est.money.mannager.api.repositories.BudgetRepository;
import est.money.mannager.api.services.CategoryService;
import est.money.mannager.api.services.TransactionService;
import est.money.mannager.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CategoryService categoryService;

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
    public List<TransactionDto> findUserTransactions(@PathVariable long id,
                                                     @RequestParam(name="category-info", required = false) boolean categoryInfo,
                                                     @RequestParam(name="date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        userService.existsOrThrow(id);
        List<Transaction> transactions;

        if(date != null){
            transactions =  transactionService.findByDateAndUserId(date, id);
        } else {
            transactions = userService.find(id).getTransactions();
        }

        return transactions.stream().map(x -> TransactionDto.from(x, false, categoryInfo)).collect(Collectors.toList());
    }

    /*
    @PostMapping("/{id}/transactions")
    public TransactionDto addTransactionToUser(@PathVariable Long id, @RequestBody TransactionForCreate tfuc){
        User u = userService.find(id);
        Category c = categoryService.find(tfuc.categoryId);
        Transaction t = new Transaction(tfuc.value, u, c);
        return TransactionDto.from(transactionService.save(t));
    }
    */


    @GetMapping("/{id}/categories")
    public List<CategoryDto> findUserCategories(@PathVariable long id) {
        return userService.find(id).getCategories().stream().map(CategoryDto::from).collect(Collectors.toList());
    }

    /*
    @PostMapping("/{id}/categories")
    public Category addCategoryToUser(@PathVariable Long id, @RequestBody Category newCategory){
        User u = userService.find(id);
        newCategory.setUser(u);
        return categoryService.save(newCategory);
    }
    */

    @GetMapping("/{id}/budgets")
    public List<Budget> getUserBudgets(@PathVariable long id) {
        return userService.find(id).getBudgets();
    }

    /*
    @PostMapping("/{id}/budgets")
    public Budget addBudgetToUser(@PathVariable Long id, @RequestBody Budget newBudget){
        User u = userService.find(id);
        newBudget.setUser(u);
        return budgetRepository.save(newBudget);
    }
    */
}
