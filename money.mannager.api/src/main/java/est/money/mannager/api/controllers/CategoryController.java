package est.money.mannager.api.controllers;

import est.money.mannager.api.models.Category;
import est.money.mannager.api.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<Category> findAll() {
        return categoryService.findAll();
    }

    @PostMapping
    public Category save(@RequestBody Category category) {
        return categoryService.save(category);
    }

    @GetMapping("/{id}")
    public Category find(@PathVariable long id) {
        return categoryService.find(id);
    }

    @PutMapping("/{id}")
    public Category update(@PathVariable Long id, @RequestBody Category newValue) {
        return categoryService.update(id, newValue);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        categoryService.delete(id);
    }
}
