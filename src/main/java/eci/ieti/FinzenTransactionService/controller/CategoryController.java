package eci.ieti.FinzenTransactionService.controller;

import eci.ieti.FinzenTransactionService.dto.CategoryDto;
import eci.ieti.FinzenTransactionService.model.Category;
import eci.ieti.FinzenTransactionService.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAll(Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());
        return ResponseEntity.ok(categoryService.findByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCustomCategory(@RequestBody Map<String, String> request, Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());
        String name = request.get("name");
        if (name == null || name.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Category category = categoryService.createCustomCategory(userId, name);
        return ResponseEntity.ok(new CategoryDto(category.getId(), category.getName(), category.isPredefined()));
    }
}