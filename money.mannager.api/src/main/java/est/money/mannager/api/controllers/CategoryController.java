package est.money.mannager.api.controllers;

import est.money.mannager.api.dtos.CategoryDto;
import est.money.mannager.api.dtos.CategoryForCreate;
import est.money.mannager.api.dtos.CategoryForUpdate;
import est.money.mannager.api.models.Category;
import est.money.mannager.api.models.User;
import est.money.mannager.api.services.CategoryService;
import est.money.mannager.api.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Operation(summary = "Get all categories")
    @GetMapping
    public List<CategoryDto> findAll() {
        return categoryService.findAll().stream().map(CategoryDto::from).collect(Collectors.toList());
    }

    @Operation(summary = "Create category")
    @PostMapping
    public CategoryDto save(@RequestBody CategoryForCreate cft) {

        if(cft.name == null || cft.name.trim().isEmpty()){
            throw new ResponseStatusException(BAD_REQUEST);
        }

        return CategoryDto.from(categoryService.save(new Category(cft.name, userService.find(cft.userId))));
    }

    @Operation(summary = "Get category by id")
    @GetMapping("/{id}")
    public CategoryDto find(@Parameter(description = "Id of the category") @PathVariable long id) {
        return CategoryDto.from(categoryService.find(id));
    }

    @Operation(summary = "Update category")
    @PutMapping("/{id}")
    public CategoryDto update(@Parameter(description = "Id of the category") @PathVariable Long id,
                              @RequestBody CategoryForUpdate newValue) {

        Category c = categoryService.findOrDefault(id, new Category());

        c.setUser(userService.find(newValue.userId));
        c.setName(newValue.name);

        return CategoryDto.from(categoryService.update(id, c));
    }

    @Operation(summary = "Delete category")
    @DeleteMapping("/{id}")
    void delete(@Parameter(description = "Id of the category") @PathVariable Long id) {
        categoryService.delete(id);
    }
}
