package eci.ieti.FinzenTransactionService.controller;

import eci.ieti.FinzenTransactionService.dto.BudgetDto;
import eci.ieti.FinzenTransactionService.model.Budget;
import eci.ieti.FinzenTransactionService.model.Category;
import eci.ieti.FinzenTransactionService.service.BudgetService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BudgetControllerTest {

	@Test
	void createOrUpdate_whenValidDto_returnsBudgetDto() {
		BudgetService budgetService = Mockito.mock(BudgetService.class);
		BudgetController controller = new BudgetController(budgetService);

		BudgetDto dto = new BudgetDto(null, 2L, 100.0, 100.0, LocalDate.now(), LocalDate.now().plusDays(30));

		Category cat = new Category();
		cat.setId(2L);

		Budget saved = new Budget();
		saved.setId(10L);
		saved.setCategory(cat);
		saved.setAmount(100.0);
		saved.setInitialAmount(100.0);
		saved.setStartDate(dto.getStartDate());
		saved.setEndDate(dto.getEndDate());

		Mockito.when(budgetService.createOrUpdate(Mockito.any(BudgetDto.class), Mockito.eq(1L)))
				.thenReturn(saved);

		Authentication auth = Mockito.mock(Authentication.class);
		Mockito.when(auth.getName()).thenReturn("1");

		ResponseEntity<BudgetDto> response = controller.createOrUpdate(dto, auth);

		assertEquals(200, response.getStatusCode().value());
		BudgetDto body = response.getBody();
		assertNotNull(body);
		assertEquals(10L, body.getId());
		assertEquals(2L, body.getCategoryId());
	}

	@Test
	void getAll_whenCalled_returnsListOfBudgets() {
		BudgetService budgetService = Mockito.mock(BudgetService.class);
		BudgetController controller = new BudgetController(budgetService);

		BudgetDto dto = new BudgetDto(10L, 2L, 50.0, 50.0, LocalDate.now(), LocalDate.now().plusDays(15));
		Mockito.when(budgetService.findByUserId(1L)).thenReturn(List.of(dto));

		Authentication auth = Mockito.mock(Authentication.class);
		Mockito.when(auth.getName()).thenReturn("1");

		ResponseEntity<List<BudgetDto>> response = controller.getAll(auth);

		assertEquals(200, response.getStatusCode().value());
		List<BudgetDto> body = response.getBody();
		assertNotNull(body);
		assertEquals(1, body.size());
		assertEquals(10L, body.get(0).getId());
	}

}
