package est.money.mannager.api.controllers;

import est.money.mannager.api.models.Transaction;
import est.money.mannager.api.models.User;
import est.money.mannager.api.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public List<Transaction> findAll() {
        return transactionService.findAll();
    }

    @PostMapping
    public Transaction save(@RequestBody Transaction transaction) {
        return transactionService.save(transaction);
    }

    @GetMapping("/{id}")
    public Transaction find(@PathVariable long id) {
        return transactionService.find(id);
    }

    @PutMapping("/{id}")
    public Transaction update(@PathVariable Long id, @RequestBody Transaction newValue) {
        return transactionService.update(id, newValue);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        transactionService.delete(id);
    }
}
