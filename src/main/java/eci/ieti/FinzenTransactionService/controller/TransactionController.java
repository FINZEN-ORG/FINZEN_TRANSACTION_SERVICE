package eci.ieti.FinzenTransactionService.controller;

import eci.ieti.FinzenTransactionService.dto.ExpenseDto;
import eci.ieti.FinzenTransactionService.dto.IncomeDto;
import eci.ieti.FinzenTransactionService.dto.TransactionDto;
import eci.ieti.FinzenTransactionService.mappers.TransactionMapper;
import eci.ieti.FinzenTransactionService.model.Expense;
import eci.ieti.FinzenTransactionService.model.Income;
import eci.ieti.FinzenTransactionService.service.ExpenseService;
import eci.ieti.FinzenTransactionService.service.IncomeService;
import eci.ieti.FinzenTransactionService.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    private final ReportService reportService;
    private final TransactionMapper mapper;

    @PostMapping("/incomes")
    public ResponseEntity<IncomeDto> createIncome(@Valid @RequestBody IncomeDto dto, Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());
        Income saved = incomeService.create(dto, userId);
        return ResponseEntity.ok(mapper.toIncomeDto(saved));
    }

    @PostMapping("/expenses")
    public ResponseEntity<ExpenseDto> createExpense(@Valid @RequestBody ExpenseDto dto, Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());
        Expense saved = expenseService.create(dto, userId);
        return ResponseEntity.ok(mapper.toExpenseDto(saved));
    }

    @GetMapping
    public ResponseEntity<List<TransactionDto>> getAll(Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());
        return ResponseEntity.ok(reportService.findAllByUserId(userId));
    }

    @DeleteMapping("/incomes/{id}")
    public ResponseEntity<?> deleteIncome(@PathVariable Long id, Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());
        incomeService.delete(id, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/expenses/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable Long id, Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());
        expenseService.delete(id, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reports")
    public ResponseEntity<?> getReports(Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());
        return ResponseEntity.ok(Map.of(
                "totalIncome", reportService.getTotalIncome(userId),
                "totalExpense", reportService.getTotalExpense(userId)
        ));
    }
}