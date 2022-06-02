package est.money.mannager.api.controllers;

import est.money.mannager.api.dtos.CategoryDto;
import est.money.mannager.api.dtos.CategoryForCreate;
import est.money.mannager.api.dtos.CategoryForUpdate;
import est.money.mannager.api.models.Category;
import est.money.mannager.api.models.User;
import est.money.mannager.api.services.CategoryService;
import est.money.mannager.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<CategoryDto> findAll() {
        return categoryService.findAll().stream().map(CategoryDto::from).collect(Collectors.toList());
    }

    @PostMapping
    public CategoryDto save(@RequestBody CategoryForCreate cft) {
        return CategoryDto.from(categoryService.save(new Category(cft.name, userService.find(cft.userId))));
    }

    @GetMapping("/{id}")
    public CategoryDto find(@PathVariable long id) {
        return CategoryDto.from(categoryService.find(id));
    }

    @PutMapping("/{id}")
    public CategoryDto update(@PathVariable Long id, @RequestBody CategoryForUpdate newValue) {

        Category c = categoryService.findOrDefault(id, new Category());

        c.setUser(userService.find(newValue.userId));
        c.setName(newValue.name);

        return CategoryDto.from(categoryService.update(id, c));
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        categoryService.delete(id);
    }
}
