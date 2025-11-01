package eci.ieti.FinzenTransactionService.service;

import eci.ieti.FinzenTransactionService.dto.TransactionDto;
import eci.ieti.FinzenTransactionService.mappers.TransactionMapper;
import eci.ieti.FinzenTransactionService.model.Expense;
import eci.ieti.FinzenTransactionService.model.Income;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ReportServiceTest {

    @Test
    void findAllByUserId_combinesIncomesAndExpenses() {
        IncomeService incomeService = Mockito.mock(IncomeService.class);
        ExpenseService expenseService = Mockito.mock(ExpenseService.class);
        TransactionMapper mapper = Mockito.mock(TransactionMapper.class);

        ReportService service = new ReportService(incomeService, expenseService, mapper);

        Income inc = new Income();
        inc.setId(1L);
        inc.setAmount(100.0);
        inc.setCreatedAt(LocalDateTime.now());

        Expense exp = new Expense();
        exp.setId(2L);
        exp.setAmount(50.0);
        exp.setCreatedAt(LocalDateTime.now());

        when(incomeService.getIncomesByUserId(1L)).thenReturn(List.of(inc));
        when(expenseService.getExpensesByUserId(1L)).thenReturn(List.of(exp));

        TransactionDto td1 = new TransactionDto(1L, 100.0, "a", 1L, LocalDateTime.now(), "INCOME");
        TransactionDto td2 = new TransactionDto(2L, 50.0, "b", 1L, LocalDateTime.now(), "EXPENSE");

        when(mapper.toTransactionDtos(List.of(inc), List.of(exp))).thenReturn(List.of(td1, td2));

        var result = service.findAllByUserId(1L);
        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
