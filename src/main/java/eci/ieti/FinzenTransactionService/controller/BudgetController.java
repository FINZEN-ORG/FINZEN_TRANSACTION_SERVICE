package eci.ieti.FinzenTransactionService.controller;

import eci.ieti.FinzenTransactionService.dto.BudgetDto;
import eci.ieti.FinzenTransactionService.model.Budget;
import eci.ieti.FinzenTransactionService.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public ResponseEntity<BudgetDto> createOrUpdate(@Valid @RequestBody BudgetDto dto, Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());
        Budget saved = budgetService.createOrUpdate(dto, userId);
        return ResponseEntity.ok(new BudgetDto(saved.getId(), saved.getCategory().getId(), saved.getAmount(), saved.getInitialAmount(), saved.getStartDate(), saved.getEndDate()));
    }

    @GetMapping
    public ResponseEntity<List<BudgetDto>> getAll(Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());
        return ResponseEntity.ok(budgetService.findByUserId(userId));
    }
}