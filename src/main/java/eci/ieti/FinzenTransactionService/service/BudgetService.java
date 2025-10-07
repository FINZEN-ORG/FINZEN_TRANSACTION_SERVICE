package eci.ieti.FinzenTransactionService.service;

import eci.ieti.FinzenTransactionService.dto.BudgetDto;
import eci.ieti.FinzenTransactionService.exceptions.budget.BudgetNotFoundException;
import eci.ieti.FinzenTransactionService.exceptions.category.CategoryNotFoundException;
import eci.ieti.FinzenTransactionService.mappers.TransactionMapper;
import eci.ieti.FinzenTransactionService.model.Budget;
import eci.ieti.FinzenTransactionService.model.Category;
import eci.ieti.FinzenTransactionService.repository.BudgetRepository;
import eci.ieti.FinzenTransactionService.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionMapper mapper;

    public Budget createOrUpdate(BudgetDto dto, Long userId) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(dto.getCategoryId()));
        Budget budget = mapper.toBudget(dto);
        budget.setUserId(userId);
        budget.setCategory(category);
        Optional<Budget> existing = budgetRepository.findByUserIdAndCategoryId(userId, category.getId());
        if (existing.isPresent()) {
            Budget existingBudget = existing.get();
            existingBudget.setAmount(budget.getAmount());
            existingBudget.setInitialAmount(budget.getInitialAmount());
            existingBudget.setStartDate(budget.getStartDate());
            existingBudget.setEndDate(budget.getEndDate());
            return budgetRepository.save(existingBudget);
        }
        return budgetRepository.save(budget);
    }

    public List<BudgetDto> findByUserId(Long userId) {
        return mapper.toBudgetDtos(budgetRepository.findByUserId(userId));
    }

    public void updateBudgetOnExpense(Long userId, Long categoryId, Double amount) {
        Optional<Budget> opt = budgetRepository.findByUserIdAndCategoryId(userId, categoryId);
        if (opt.isEmpty()) {
            throw new BudgetNotFoundException(userId, categoryId);
        }
        Budget budget = opt.get();
        budget.setAmount(budget.getAmount() - amount);
        if (budget.getAmount() < 0) budget.setAmount(0.0);
        budgetRepository.save(budget);
    }
}