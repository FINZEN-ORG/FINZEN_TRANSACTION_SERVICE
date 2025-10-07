package eci.ieti.FinzenTransactionService.controller;

import eci.ieti.FinzenTransactionService.dto.BudgetDto;
import eci.ieti.FinzenTransactionService.model.Budget;
import eci.ieti.FinzenTransactionService.model.Category;
import eci.ieti.FinzenTransactionService.repository.CategoryRepository;
import eci.ieti.FinzenTransactionService.service.BudgetService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetService budgetService;
    private final CategoryRepository categoryRepository;

    public BudgetController(BudgetService budgetService, CategoryRepository categoryRepository) {
        this.budgetService = budgetService;
        this.categoryRepository = categoryRepository;
    }

    @PostMapping
    public ResponseEntity<BudgetDto> createOrUpdate(@RequestBody BudgetDto dto, Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        Budget budget = Budget.builder()
                .userId(userId)
                .category(category)
                .amount(dto.getAmount())
                .initialAmount(dto.getInitialAmount())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();
        Budget saved = budgetService.createOrUpdate(budget);
        return ResponseEntity.ok(new BudgetDto(saved.getId(), saved.getCategory().getId(), saved.getAmount(), saved.getInitialAmount(), saved.getStartDate(), saved.getEndDate()));
    }

    @GetMapping
    public ResponseEntity<List<BudgetDto>> getAll(Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());
        return ResponseEntity.ok(budgetService.findByUserId(userId));
    }
}