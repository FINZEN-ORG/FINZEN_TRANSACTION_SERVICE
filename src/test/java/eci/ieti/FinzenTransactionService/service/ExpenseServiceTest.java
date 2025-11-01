package eci.ieti.FinzenTransactionService.service;

import eci.ieti.FinzenTransactionService.dto.ExpenseDto;
import eci.ieti.FinzenTransactionService.mappers.TransactionMapper;
import eci.ieti.FinzenTransactionService.model.Category;
import eci.ieti.FinzenTransactionService.model.Expense;
import eci.ieti.FinzenTransactionService.repository.CategoryRepository;
import eci.ieti.FinzenTransactionService.repository.ExpenseRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseServiceTest {

	@Test
	void create_whenCategoryExists_savesExpenseAndUpdatesBudgetIfPresent() {
		ExpenseRepository expenseRepository = Mockito.mock(ExpenseRepository.class);
		CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
		TransactionMapper mapper = Mockito.mock(TransactionMapper.class);

		BudgetService dummyBudgetService = Mockito.mock(BudgetService.class);

		ExpenseService service = new ExpenseService(expenseRepository, categoryRepository, dummyBudgetService, mapper);

		ExpenseDto dto = new ExpenseDto(null, 30.0, "Snack", 2L, null, null);

		Category cat = new Category();
		cat.setId(2L);

		Expense expenseEntity = new Expense();
		expenseEntity.setAmount(30.0);

		Mockito.when(categoryRepository.findById(2L)).thenReturn(Optional.of(cat));
		Mockito.when(mapper.toExpense(dto)).thenReturn(expenseEntity);
		Mockito.when(expenseRepository.save(Mockito.any(Expense.class))).thenAnswer(i -> {
			Expense e = i.getArgument(0);
			e.setId(15L);
			e.setCreatedAt(LocalDateTime.now());
			return e;
		});

		Expense result = service.create(dto, 1L);
		assertNotNull(result);
		assertEquals(15L, result.getId());
	}
}
