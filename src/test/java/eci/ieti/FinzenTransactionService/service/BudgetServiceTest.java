package eci.ieti.FinzenTransactionService.service;

import eci.ieti.FinzenTransactionService.dto.BudgetDto;
import eci.ieti.FinzenTransactionService.mappers.TransactionMapper;
import eci.ieti.FinzenTransactionService.model.Budget;
import eci.ieti.FinzenTransactionService.model.Category;
import eci.ieti.FinzenTransactionService.repository.BudgetRepository;
import eci.ieti.FinzenTransactionService.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
 

class BudgetServiceTest {

	@Test
	void createOrUpdate_whenNoExisting_savesNewBudget() {
		BudgetRepository budgetRepository = Mockito.mock(BudgetRepository.class);
		CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
		TransactionMapper mapper = Mockito.mock(TransactionMapper.class);

		BudgetService service = new BudgetService(budgetRepository, categoryRepository, mapper);

		BudgetDto dto = new BudgetDto(null, 2L, 100.0, 100.0, LocalDate.now(), LocalDate.now().plusDays(10));

		Category category = new Category();
		category.setId(2L);

		Budget budgetEntity = new Budget();
		budgetEntity.setAmount(100.0);
		budgetEntity.setInitialAmount(100.0);

		Mockito.when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));
		Mockito.when(mapper.toBudget(dto)).thenReturn(budgetEntity);
		Mockito.when(budgetRepository.findByUserIdAndCategoryId(1L, 2L)).thenReturn(Optional.empty());
		Mockito.when(budgetRepository.save(any(Budget.class))).thenAnswer(i -> {
			Budget b = i.getArgument(0);
			b.setId(7L);
			return b;
		});

		Budget result = service.createOrUpdate(dto, 1L);

		assertNotNull(result);
		assertEquals(7L, result.getId());
		assertEquals(100.0, result.getAmount());
	}

	@Test
	void updateBudgetOnExpense_whenBudgetMissing_throws() {
		BudgetRepository budgetRepository = Mockito.mock(BudgetRepository.class);
		CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
		TransactionMapper mapper = Mockito.mock(TransactionMapper.class);

		BudgetService service = new BudgetService(budgetRepository, categoryRepository, mapper);

		Mockito.when(budgetRepository.findByUserIdAndCategoryId(1L, 2L)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> service.updateBudgetOnExpense(1L, 2L, 10.0));
	}
}
