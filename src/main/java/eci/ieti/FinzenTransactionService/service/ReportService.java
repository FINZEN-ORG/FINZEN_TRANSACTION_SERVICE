package eci.ieti.FinzenTransactionService.service;

import eci.ieti.FinzenTransactionService.dto.TransactionDto;
import eci.ieti.FinzenTransactionService.mappers.TransactionMapper;
import eci.ieti.FinzenTransactionService.model.Expense;
import eci.ieti.FinzenTransactionService.model.Income;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    private final TransactionMapper mapper;

    public List<TransactionDto> findAllByUserId(Long userId) {
        List<Income> incomes = incomeService.getIncomesByUserId(userId);
        List<Expense> expenses = expenseService.getExpensesByUserId(userId);
        return mapper.toTransactionDtos(incomes, expenses);
    }

    public Double getTotalIncome(Long userId) {
        return incomeService.getTotalIncome(userId);
    }

    public Double getTotalExpense(Long userId) {
        return expenseService.getTotalExpense(userId);
    }
}