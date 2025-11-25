package eci.ieti.FinzenTransactionService.controller;

import eci.ieti.FinzenTransactionService.dto.CategoryDto;
import eci.ieti.FinzenTransactionService.model.Category;
import eci.ieti.FinzenTransactionService.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAll(@RequestParam(defaultValue = "EXPENSE") String type, Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());
        return ResponseEntity.ok(categoryService.findByUserIdAndType(userId, type));
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCustomCategory(@Valid @RequestBody CategoryDto dto, Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());
        Category saved = categoryService.createCustomCategory(userId, dto);
        return ResponseEntity.ok(new CategoryDto(saved.getId(), saved.getName(), saved.getType(), saved.isPredefined()));
    }
}