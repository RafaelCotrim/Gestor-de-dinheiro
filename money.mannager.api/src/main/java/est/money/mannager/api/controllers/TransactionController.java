package est.money.mannager.api.controllers;

import est.money.mannager.api.dtos.TransactionDto;
import est.money.mannager.api.dtos.TransactionForCreate;
import est.money.mannager.api.models.Category;
import est.money.mannager.api.models.Transaction;
import est.money.mannager.api.models.User;
import est.money.mannager.api.services.CategoryService;
import est.money.mannager.api.services.TransactionService;
import est.money.mannager.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public List<TransactionDto> findAll(@RequestParam(name="user-info", required = false) boolean userInfo,
                                        @RequestParam(name="category-info", required = false) boolean categoryInfo) {
        return transactionService.findAll()
                .stream()
                .map(x -> TransactionDto.from(x, userInfo, categoryInfo))
                .collect(Collectors.toList());
    }

    @PostMapping
    public TransactionDto save(@RequestBody TransactionForCreate tfc) {
        User u = userService.findOrNull(tfc.userId);
        Category c = categoryService.findOrNull(tfc.categoryId);
        return TransactionDto.from(transactionService.save(new Transaction(tfc.value, u, c)));
    }

    @GetMapping("/{id}")
    public TransactionDto find(@PathVariable long id,
                               @RequestParam(name="user-info", required = false) boolean userInfo,
                               @RequestParam(name="category-info", required = false) boolean categoryInfo) {
        return TransactionDto.from(transactionService.find(id), userInfo, categoryInfo);
    }

    @PutMapping("/{id}")
    public TransactionDto update(@PathVariable Long id, @RequestBody Transaction newValue) {
        return TransactionDto.from(transactionService.update(id, newValue));
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        transactionService.delete(id);
    }
}
