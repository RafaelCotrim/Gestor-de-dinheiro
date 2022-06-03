package est.money.mannager.api.controllers;

import est.money.mannager.api.dtos.TransactionDto;
import est.money.mannager.api.dtos.TransactionForCreate;
import est.money.mannager.api.dtos.TransactionForUpdate;
import est.money.mannager.api.models.Category;
import est.money.mannager.api.models.Transaction;
import est.money.mannager.api.models.User;
import est.money.mannager.api.services.CategoryService;
import est.money.mannager.api.services.TransactionService;
import est.money.mannager.api.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "Get all transactions")
    @GetMapping
    public List<TransactionDto> findAll(@Parameter(description = "Whether or not to include user data in transaction") @RequestParam(name="user-info", required = false) boolean userInfo,
                                        @Parameter(description = "Whether or not to include category data in transaction") @RequestParam(name="category-info", required = false) boolean categoryInfo) {

        return transactionService.findAll()
                .stream()
                .map(x -> TransactionDto.from(x, userInfo, categoryInfo))
                .collect(Collectors.toList());
    }

    @Operation(summary = "Create transaction")
    @PostMapping
    public TransactionDto save(@RequestBody TransactionForCreate tfc) {
        User u = userService.find(tfc.userId);
        Category c = categoryService.findOrNull(tfc.categoryId);
        Date d = tfc.date == null ? new Date() : tfc.date;
        return TransactionDto.from(transactionService.save(new Transaction(tfc.value, d, u, c)));
    }

    @Operation(summary = "Get transaction by id")
    @GetMapping("/{id}")
    public TransactionDto find(@Parameter(description = "Id of the transaction") @PathVariable long id,
                               @RequestParam(name="user-info", required = false) boolean userInfo,
                               @RequestParam(name="category-info", required = false) boolean categoryInfo) {
        return TransactionDto.from(transactionService.find(id), userInfo, categoryInfo);
    }

    @Operation(summary = "Update transaction")
    @PutMapping("/{id}")
    public TransactionDto update(@Parameter(description = "Id of the transaction") @PathVariable Long id,
                                 @RequestBody TransactionForUpdate newValue) {

        Transaction t =  transactionService.findOrDefault(id, new Transaction());
        t.setUser(userService.find(newValue.userId));
        t.setCategory(categoryService.findOrNull(id));
        t.setDate(newValue.date);
        t.setValue(newValue.value);

        return TransactionDto.from(transactionService.update(id, t));
    }

    @Operation(summary = "Delete transaction")
    @DeleteMapping("/{id}")
    void delete(@Parameter(description = "Id of the transaction") @PathVariable Long id) {
        transactionService.delete(id);
    }
}
