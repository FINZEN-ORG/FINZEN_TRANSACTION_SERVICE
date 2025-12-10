package eci.ieti.FinzenTransactionService.service;
import eci.ieti.FinzenTransactionService.dto.CategoryTotalDto;
import eci.ieti.FinzenTransactionService.dto.ExpenseDto;
import eci.ieti.FinzenTransactionService.mappers.TransactionMapper;
import eci.ieti.FinzenTransactionService.model.Category;
import eci.ieti.FinzenTransactionService.model.Expense;
import eci.ieti.FinzenTransactionService.repository.CategoryRepository;
import eci.ieti.FinzenTransactionService.repository.ExpenseRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TransactionMapper mapper;

    @InjectMocks
    private ExpenseService expenseService;

    private Category testCategory;
    private Expense testExpense;
    private ExpenseDto testExpenseDto;
    private final Long USER_ID = 1L;

    @BeforeEach
    void setUp() {
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Food");
        testCategory.setType("EXPENSE");

        testExpense = new Expense();
        testExpense.setId(1L);
        testExpense.setAmount(new BigDecimal("50.00"));
        testExpense.setDescription("Lunch");
        testExpense.setCategory(testCategory);
        testExpense.setUserId(USER_ID);
        testExpense.setDate(LocalDateTime.now());

        testExpenseDto = new ExpenseDto(
                1L,
                new BigDecimal("50.00"),
                "Lunch",
                1L,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    void testCreateExpense_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(mapper.toExpense(any(ExpenseDto.class))).thenReturn(testExpense);
        when(expenseRepository.save(any(Expense.class))).thenReturn(testExpense);
        Expense result = expenseService.create(testExpenseDto, USER_ID);
        assertNotNull(result);
        assertEquals(USER_ID, result.getUserId());
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }

    @Test
    void testGetExpensesByUserId() {
        List<Expense> expenses = Arrays.asList(testExpense);
        when(expenseRepository.findByUserId(USER_ID)).thenReturn(expenses);
        List<Expense> result = expenseService.getExpensesByUserId(USER_ID);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindByUserId() {
        List<Expense> expenses = Arrays.asList(testExpense);
        List<ExpenseDto> expenseDtos = Arrays.asList(testExpenseDto);
        when(expenseRepository.findByUserId(USER_ID)).thenReturn(expenses);
        when(mapper.toExpenseDtos(expenses)).thenReturn(expenseDtos);
        List<ExpenseDto> result = expenseService.findByUserId(USER_ID);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetTotalExpense() {
        BigDecimal expectedTotal = new BigDecimal("50.00");
        when(expenseRepository.sumByUserId(USER_ID)).thenReturn(expectedTotal);
        BigDecimal result = expenseService.getTotalExpense(USER_ID);
        assertNotNull(result);
        assertEquals(0, expectedTotal.compareTo(result));
    }

    @Test
    void testGetCategorySummaries() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(30);
        LocalDateTime endDate = LocalDateTime.now();

        List<CategoryTotalDto> summaries = Arrays.asList(
                new CategoryTotalDto(1L, new BigDecimal("200.00"))
        );
        when(expenseRepository.sumExpensesByCategoryAndDateRange(
                eq(USER_ID), eq(startDate), eq(endDate)))
                .thenReturn(summaries);
        List<CategoryTotalDto> result = expenseService.getCategorySummaries(USER_ID, startDate, endDate);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testDelete_Success() {
        testExpense.setCreatedAt(LocalDateTime.now().minusHours(12));
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(testExpense));
        doNothing().when(expenseRepository).delete(testExpense);

        expenseService.delete(1L, USER_ID);

        verify(expenseRepository, times(1)).delete(testExpense);
    }

    @Test
    void testDelete_ExpenseNotFound() {
        when(expenseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> {
            expenseService.delete(1L, USER_ID);
        });
    }

    @Test
    void testDelete_WrongUser() {
        testExpense.setUserId(999L);
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(testExpense));

        assertThrows(Exception.class, () -> {
            expenseService.delete(1L, USER_ID);
        });
    }

    @Test
    void testDelete_ExpiredPeriod() {
        testExpense.setCreatedAt(LocalDateTime.now().minusHours(25));
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(testExpense));

        assertThrows(Exception.class, () -> {
            expenseService.delete(1L, USER_ID);
        });
    }

    @Test
    void testCreateExpense_CategoryNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> {
            expenseService.create(testExpenseDto, USER_ID);
        });
    }
}