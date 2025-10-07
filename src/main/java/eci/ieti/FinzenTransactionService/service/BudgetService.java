package eci.ieti.FinzenTransactionService.service;

import eci.ieti.FinzenTransactionService.dto.BudgetDto;
import eci.ieti.FinzenTransactionService.model.Budget;
import eci.ieti.FinzenTransactionService.repository.BudgetRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;

    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    public Budget createOrUpdate(Budget budget) {
        Optional<Budget> existing = budgetRepository.findByUserIdAndCategoryId(budget.getUserId(), budget.getCategory().getId());
        if (existing.isPresent()) {
            Budget existingBudget = existing.get();
            existingBudget.setAmount(budget.getAmount());
            existingBudget.setInitialAmount(budget.getInitialAmount());
            existingBudget.setStartDate(budget.getStartDate());
            existingBudget.setEndDate(budget.getEndDate());
            return budgetRepository.save(existingBudget);
        }
        if (budget.getCategory() == null) {
            throw new IllegalArgumentException("Category is required");
        }
        return budgetRepository.save(budget);
    }

    public List<BudgetDto> findByUserId(Long userId) {
        return budgetRepository.findByUserId(userId).stream()
                .map(b -> new BudgetDto(b.getId(), b.getCategory().getId(), b.getAmount(), b.getInitialAmount(), b.getStartDate(), b.getEndDate()))
                .collect(Collectors.toList());
    }

    public void updateBudgetOnExpense(Long userId, Long categoryId, Double amount) {
        budgetRepository.findByUserIdAndCategoryId(userId, categoryId).ifPresent(budget -> {
            budget.setAmount(budget.getAmount() - amount);
            if (budget.getAmount() < 0) budget.setAmount(0.0);
            budgetRepository.save(budget);
        });
    }
}