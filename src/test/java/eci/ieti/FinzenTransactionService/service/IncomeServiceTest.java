package eci.ieti.FinzenTransactionService.service;

import eci.ieti.FinzenTransactionService.dto.IncomeDto;
import eci.ieti.FinzenTransactionService.exceptions.category.CategoryNotFoundException;
import eci.ieti.FinzenTransactionService.exceptions.income.IncomeNotFoundException;
import eci.ieti.FinzenTransactionService.mappers.TransactionMapper;
import eci.ieti.FinzenTransactionService.model.Category;
import eci.ieti.FinzenTransactionService.model.Income;
import eci.ieti.FinzenTransactionService.repository.CategoryRepository;
import eci.ieti.FinzenTransactionService.repository.IncomeRepository;
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
class IncomeServiceTest {

    @Mock
    private IncomeRepository incomeRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TransactionMapper mapper;

    @InjectMocks
    private IncomeService incomeService;

    private Category testCategory;
    private Income testIncome;
    private IncomeDto testIncomeDto;
    private final Long USER_ID = 1L;

    @BeforeEach
    void setUp() {
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Salary");
        testCategory.setType("INCOME");

        testIncome = new Income();
        testIncome.setId(1L);
        testIncome.setAmount(new BigDecimal("1000.00"));
        testIncome.setDescription("Monthly salary");
        testIncome.setCategory(testCategory);
        testIncome.setUserId(USER_ID);
        testIncome.setDate(LocalDateTime.now());
        testIncome.setCreatedAt(LocalDateTime.now().minusHours(12));

        testIncomeDto = new IncomeDto(
                1L,
                new BigDecimal("1000.00"),
                "Monthly salary",
                1L,
                LocalDateTime.now(),
                LocalDateTime.now().minusHours(12)
        );
    }

    @Test
    void testCreateIncome_Success() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(mapper.toIncome(any(IncomeDto.class))).thenReturn(testIncome);
        when(incomeRepository.save(any(Income.class))).thenReturn(testIncome);

        // Act
        Income result = incomeService.create(testIncomeDto, USER_ID);

        // Assert
        assertNotNull(result);
        assertEquals(USER_ID, result.getUserId());
        verify(incomeRepository, times(1)).save(any(Income.class));
    }

    @Test
    void testCreateIncome_CategoryNotFound() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CategoryNotFoundException.class, () -> {
            incomeService.create(testIncomeDto, USER_ID);
        });
    }

    @Test
    void testGetIncomesByUserId() {
        // Arrange
        List<Income> incomes = Arrays.asList(testIncome);
        when(incomeRepository.findByUserId(USER_ID)).thenReturn(incomes);

        // Act
        List<Income> result = incomeService.getIncomesByUserId(USER_ID);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindByUserId() {
        // Arrange
        List<Income> incomes = Arrays.asList(testIncome);
        List<IncomeDto> incomeDtos = Arrays.asList(testIncomeDto);
        when(incomeRepository.findByUserId(USER_ID)).thenReturn(incomes);
        when(mapper.toIncomeDtos(incomes)).thenReturn(incomeDtos);

        // Act
        List<IncomeDto> result = incomeService.findByUserId(USER_ID);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(mapper, times(1)).toIncomeDtos(incomes);
    }

    @Test
    void testGetTotalIncome() {
        // Arrange
        BigDecimal total = new BigDecimal("5000.00");
        when(incomeRepository.sumByUserId(USER_ID)).thenReturn(total);

        // Act
        BigDecimal result = incomeService.getTotalIncome(USER_ID);

        // Assert
        assertNotNull(result);
        assertEquals(total, result);
    }

    @Test
    void testDelete_Success() {
        // Arrange
        when(incomeRepository.findById(1L)).thenReturn(Optional.of(testIncome));
        doNothing().when(incomeRepository).delete(testIncome);

        // Act
        incomeService.delete(1L, USER_ID);

        // Assert
        verify(incomeRepository, times(1)).delete(testIncome);
    }

    @Test
    void testDelete_IncomeNotFound() {
        // Arrange
        when(incomeRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IncomeNotFoundException.class, () -> {
            incomeService.delete(1L, USER_ID);
        });
    }

    @Test
    void testDelete_WrongUser() {
        // Arrange
        testIncome.setUserId(999L); // Different user
        when(incomeRepository.findById(1L)).thenReturn(Optional.of(testIncome));

        // Act & Assert
        assertThrows(IncomeNotFoundException.class, () -> {
            incomeService.delete(1L, USER_ID);
        });
    }

    @Test
    void testDelete_ExpiredPeriod() {
        // Arrange
        testIncome.setCreatedAt(LocalDateTime.now().minusHours(25)); // More than 24 hours
        when(incomeRepository.findById(1L)).thenReturn(Optional.of(testIncome));

        // Act & Assert
        assertThrows(Exception.class, () -> {
            incomeService.delete(1L, USER_ID);
        });
    }
}