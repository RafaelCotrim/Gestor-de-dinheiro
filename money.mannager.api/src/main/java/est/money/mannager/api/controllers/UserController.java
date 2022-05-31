package est.money.mannager.api.controllers;

import est.money.mannager.api.dtos.*;
import est.money.mannager.api.models.Budget;
import est.money.mannager.api.models.Transaction;
import est.money.mannager.api.models.User;
import est.money.mannager.api.repositories.BudgetRepository;
import est.money.mannager.api.services.CategoryService;
import est.money.mannager.api.services.TransactionService;
import est.money.mannager.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<UserDTO> findAll() {
        return userService.findAll().stream().map(UserDTO::from).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDTO findUser(@PathVariable long id) {
        return UserDTO.from(userService.find(id));
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(@RequestBody UserForUpdate userForUpdate, @PathVariable Long id) {
        return UserDTO.from(userService.update(id, userForUpdate));
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

    @GetMapping("/{id}/categories")
    public List<CategoryDto> findUserCategories(@PathVariable long id) {
        return userService.find(id).getCategories().stream().map(CategoryDto::from).collect(Collectors.toList());
    }

    @GetMapping("/{id}/statistics")
    public List<StatisticsDto> findUserStatistics(@PathVariable long id) {
        return userService.find(id)
                .getTransactions()
                .stream()
                .filter(t -> t.getValue() < 0 && t.getCategory() != null)
                .collect(Collectors.groupingBy(t -> t.getCategory().getName()))
                .entrySet()
                .stream()
                .map(e -> new StatisticsDto(
                         e.getKey(),
                         e.getValue().stream().map(t -> t.getValue() * -1).reduce(0.0, Double::sum)))
                .sorted(Comparator.comparingDouble(StatisticsDto::getValue).reversed())
                .toList();
    }

    @GetMapping("/{id}/budgets")
    public List<Budget> getUserBudgets(@PathVariable long id) {
        return userService.find(id).getBudgets();
    }

}
