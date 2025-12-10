package eci.ieti.FinzenTransactionService.service;

import eci.ieti.FinzenTransactionService.dto.TransactionDto;
import eci.ieti.FinzenTransactionService.mappers.TransactionMapper;
import eci.ieti.FinzenTransactionService.model.Expense;
import eci.ieti.FinzenTransactionService.model.Income;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private IncomeService incomeService;

    @Mock
    private ExpenseService expenseService;

    @Mock
    private TransactionMapper mapper;

    @InjectMocks
    private ReportService reportService;

    private Income testIncome;
    private Expense testExpense;
    private TransactionDto testTransactionDto;
    private final Long USER_ID = 1L;

    @BeforeEach
    void setUp() {
        testIncome = new Income();
        testIncome.setId(1L);
        testIncome.setAmount(new BigDecimal("1000.00"));
        testIncome.setDescription("Salary");
        testIncome.setUserId(USER_ID);
        testIncome.setDate(LocalDateTime.now());

        testExpense = new Expense();
        testExpense.setId(2L);
        testExpense.setAmount(new BigDecimal("500.00"));
        testExpense.setDescription("Rent");
        testExpense.setUserId(USER_ID);
        testExpense.setDate(LocalDateTime.now());

        testTransactionDto = new TransactionDto(
                1L,
                new BigDecimal("1000.00"),
                "Salary",
                1L,
                LocalDateTime.now(),
                "INCOME"
        );
    }

    @Test
    void testFindAllByUserId() {
        // Arrange
        List<Income> incomes = Arrays.asList(testIncome);
        List<Expense> expenses = Arrays.asList(testExpense);
        List<TransactionDto> transactionDtos = Arrays.asList(testTransactionDto);

        when(incomeService.getIncomesByUserId(USER_ID)).thenReturn(incomes);
        when(expenseService.getExpensesByUserId(USER_ID)).thenReturn(expenses);
        when(mapper.toTransactionDtos(incomes, expenses)).thenReturn(transactionDtos);

        // Act
        List<TransactionDto> result = reportService.findAllByUserId(USER_ID);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(incomeService, times(1)).getIncomesByUserId(USER_ID);
        verify(expenseService, times(1)).getExpensesByUserId(USER_ID);
    }

    @Test
    void testGetTotalIncome() {
        // Arrange
        BigDecimal expectedTotal = new BigDecimal("1500.00");
        when(incomeService.getTotalIncome(USER_ID)).thenReturn(expectedTotal);

        // Act
        BigDecimal result = reportService.getTotalIncome(USER_ID);

        // Assert
        assertNotNull(result);
        assertEquals(0, expectedTotal.compareTo(result));
    }

    @Test
    void testGetTotalExpense() {
        // Arrange
        BigDecimal expectedTotal = new BigDecimal("750.00");
        when(expenseService.getTotalExpense(USER_ID)).thenReturn(expectedTotal);

        // Act
        BigDecimal result = reportService.getTotalExpense(USER_ID);

        // Assert
        assertNotNull(result);
        assertEquals(0, expectedTotal.compareTo(result));
    }

    @Test
    void testFindAllByUserId_NoTransactions() {
        // Arrange
        when(incomeService.getIncomesByUserId(USER_ID)).thenReturn(Arrays.asList());
        when(expenseService.getExpensesByUserId(USER_ID)).thenReturn(Arrays.asList());
        when(mapper.toTransactionDtos(anyList(), anyList())).thenReturn(Arrays.asList());

        // Act
        List<TransactionDto> result = reportService.findAllByUserId(USER_ID);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindAllByUserId_OnlyIncomes() {
        // Arrange
        List<Income> incomes = Arrays.asList(testIncome);
        List<TransactionDto> transactionDtos = Arrays.asList(testTransactionDto);

        when(incomeService.getIncomesByUserId(USER_ID)).thenReturn(incomes);
        when(expenseService.getExpensesByUserId(USER_ID)).thenReturn(Arrays.asList());
        when(mapper.toTransactionDtos(incomes, Arrays.asList())).thenReturn(transactionDtos);

        // Act
        List<TransactionDto> result = reportService.findAllByUserId(USER_ID);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(incomeService, times(1)).getIncomesByUserId(USER_ID);
        verify(expenseService, times(1)).getExpensesByUserId(USER_ID);
    }

    @Test
    void testFindAllByUserId_OnlyExpenses() {
        // Arrange
        List<Expense> expenses = Arrays.asList(testExpense);
        TransactionDto expenseDto = new TransactionDto(
                2L,
                new BigDecimal("500.00"),
                "Rent",
                1L,
                LocalDateTime.now(),
                "EXPENSE"
        );
        List<TransactionDto> transactionDtos = Arrays.asList(expenseDto);

        when(incomeService.getIncomesByUserId(USER_ID)).thenReturn(Arrays.asList());
        when(expenseService.getExpensesByUserId(USER_ID)).thenReturn(expenses);
        when(mapper.toTransactionDtos(Arrays.asList(), expenses)).thenReturn(transactionDtos);

        // Act
        List<TransactionDto> result = reportService.findAllByUserId(USER_ID);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(incomeService, times(1)).getIncomesByUserId(USER_ID);
        verify(expenseService, times(1)).getExpensesByUserId(USER_ID);
    }

    @Test
    void testGetTotalIncome_Zero() {
        // Arrange
        when(incomeService.getTotalIncome(USER_ID)).thenReturn(BigDecimal.ZERO);

        // Act
        BigDecimal result = reportService.getTotalIncome(USER_ID);

        // Assert
        assertNotNull(result);
        assertEquals(0, BigDecimal.ZERO.compareTo(result));
    }

    @Test
    void testGetTotalExpense_Zero() {
        // Arrange
        when(expenseService.getTotalExpense(USER_ID)).thenReturn(BigDecimal.ZERO);

        // Act
        BigDecimal result = reportService.getTotalExpense(USER_ID);

        // Assert
        assertNotNull(result);
        assertEquals(0, BigDecimal.ZERO.compareTo(result));
    }
}