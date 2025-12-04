package eci.ieti.FinzenTransactionService.service;

import eci.ieti.FinzenTransactionService.dto.CategoryTotalDto;
import eci.ieti.FinzenTransactionService.dto.ExpenseDto;
import eci.ieti.FinzenTransactionService.exceptions.category.CategoryNotFoundException;
import eci.ieti.FinzenTransactionService.exceptions.expense.ExpenseNotFoundException;
import eci.ieti.FinzenTransactionService.exceptions.transaction.ShortPeriodExpiredException;
import eci.ieti.FinzenTransactionService.mappers.TransactionMapper;
import eci.ieti.FinzenTransactionService.model.Category;
import eci.ieti.FinzenTransactionService.model.Expense;
import eci.ieti.FinzenTransactionService.repository.CategoryRepository;
import eci.ieti.FinzenTransactionService.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionMapper mapper;

    public Expense create(ExpenseDto dto, Long userId) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(dto.getCategoryId()));
        Expense expense = mapper.toExpense(dto);
        expense.setUserId(userId);
        expense.setDate(LocalDateTime.now());
        expense.setCategory(category);
        return expenseRepository.save(expense);
    }

    public List<Expense> getExpensesByUserId(Long userId) {
        return expenseRepository.findByUserId(userId);
    }

    public List<ExpenseDto> findByUserId(Long userId) {
        return mapper.toExpenseDtos(expenseRepository.findByUserId(userId));
    }

    public BigDecimal getTotalExpense(Long userId) {
        return expenseRepository.sumByUserId(userId);
    }

    public List<CategoryTotalDto> getCategorySummaries(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return expenseRepository.sumExpensesByCategoryAndDateRange(userId, startDate, endDate);
    }

    public void delete(Long id, Long userId) {
        Expense expense = expenseRepository.findById(id)
                .filter(e -> e.getUserId().equals(userId))
                .orElseThrow(() -> new ExpenseNotFoundException(id));
        if (LocalDateTime.now().minusHours(24).isAfter(expense.getCreatedAt())) {
            throw new ShortPeriodExpiredException("Expense", id);
        }
        expenseRepository.delete(expense);
    }
}