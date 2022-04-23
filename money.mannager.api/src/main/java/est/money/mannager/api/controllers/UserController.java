package est.money.mannager.api.controllers;

import est.money.mannager.api.models.Budget;
import est.money.mannager.api.models.Category;
import est.money.mannager.api.models.Transaction;
import est.money.mannager.api.models.User;
import est.money.mannager.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    @PostMapping
    public User newUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping("/{id}")
    public User findUser(@PathVariable long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found"));
    }

    @PutMapping("/{id}")
    User updateUser(@RequestBody User newUser, @PathVariable Long id) {

        return userRepository.findById(id)
                .map(user -> {
                    user.setName(newUser.getName());
                    user.setEmail(newUser.getEmail());
                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    newUser.setId(id);
                    return userRepository.save(newUser);
                });
    }

    @DeleteMapping("/{id}")
    void deleteUser(@PathVariable Long id) {

        if(userRepository.findById(id).isPresent()){
            userRepository.deleteById(id);
            return;
        }

        throw new ResponseStatusException(NOT_FOUND, "User not found");

    }

    @GetMapping("/{id}/transactions")
    public List<Transaction> findUserTransactions(@PathVariable long id) {
        return findUser(id).getTransactions();
    }

    @GetMapping("/{id}/categories")
    public List<Category> findUserCategories(@PathVariable long id) {
        return findUser(id).getCategories();
    }

    @GetMapping("/{id}/budgets")
    public List<Budget> getUserBudgets(@PathVariable long id) {
        return findUser(id).getBudgets();
    }

}
