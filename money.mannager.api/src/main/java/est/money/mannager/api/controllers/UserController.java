package est.money.mannager.api.controllers;

import est.money.mannager.api.dtos.*;
import est.money.mannager.api.models.Budget;
import est.money.mannager.api.models.Transaction;
import est.money.mannager.api.models.User;
import est.money.mannager.api.repositories.BudgetRepository;
import est.money.mannager.api.services.CategoryService;
import est.money.mannager.api.services.TransactionService;
import est.money.mannager.api.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @Operation(summary = "Get all users")
    @GetMapping
    public List<UserDTO> findAll() {
        return userService.findAll().stream().map(UserDTO::from).collect(Collectors.toList());
    }

    @Operation(summary = "Get user by id")
    @GetMapping("/{id}")
    public UserDTO findUser(@Parameter(description = "Id of the user searched")  @PathVariable long id) {
        return UserDTO.from(userService.find(id));
    }

    @Operation(summary = "Update a user")
    @PutMapping("/{id}")
    public UserDTO updateUser(@Parameter(description = "Id of the updated user") @PathVariable Long id,
                              @RequestBody UserForUpdate userForUpdate) {
        return UserDTO.from(userService.update(id, userForUpdate));
    }

    @Operation(summary = "Delete user")
    @DeleteMapping("/{id}")
    void deleteUser(@Parameter(description = "Id of the deleted user") @PathVariable Long id) {
        userService.delete(id);
    }

    @Operation(summary = "Get transaction of a single user")
    @GetMapping("/{id}/transactions")
    public List<TransactionDto> findUserTransactions(
            @Parameter(description = "Id of the searched user") @PathVariable long id,
            @Parameter(description = "Whether or not the response should contain category data") @RequestParam(name="category-info", required = false) boolean categoryInfo,
            @Parameter(description = "The date of when the transactions took place") @RequestParam(name="date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
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
    public List<BudgetDto> getUserBudgets(@PathVariable long id,
                                          @RequestParam(name="category-info", required = false) boolean categoryInfo,
                                          @RequestParam(name="date-start", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateStart,
                                          @RequestParam(name="date-end", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateEnd) {
        return userService.find(id).getBudgets().stream().map(b -> BudgetDto.from(b, categoryInfo, dateStart, dateEnd)).collect(Collectors.toList());
    }

}
