package eci.ieti.FinzenTransactionService.controller;

import eci.ieti.FinzenTransactionService.dto.ExpenseDto;
import eci.ieti.FinzenTransactionService.dto.IncomeDto;
import eci.ieti.FinzenTransactionService.dto.TransactionDto;
import eci.ieti.FinzenTransactionService.mappers.TransactionMapper;
import eci.ieti.FinzenTransactionService.model.Expense;
import eci.ieti.FinzenTransactionService.model.Income;
import eci.ieti.FinzenTransactionService.model.Category;
import eci.ieti.FinzenTransactionService.service.ExpenseService;
import eci.ieti.FinzenTransactionService.service.IncomeService;
import eci.ieti.FinzenTransactionService.service.ReportService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TransactionControllerTest {

	@Test
	void createIncome_whenValid_returnsIncomeDto() {
		IncomeService incomeService = Mockito.mock(IncomeService.class);
		ExpenseService expenseService = Mockito.mock(ExpenseService.class);
		ReportService reportService = Mockito.mock(ReportService.class);
		TransactionMapper mapper = Mockito.mock(TransactionMapper.class);

		TransactionController controller = new TransactionController(incomeService, expenseService, reportService, mapper);

		IncomeDto request = new IncomeDto(null, 200.0, "Salary", 3L, null, null);

		Category cat = new Category();
		cat.setId(3L);

		Income saved = new Income();
		saved.setId(11L);
		saved.setAmount(200.0);
		saved.setDescription("Salary");
		saved.setCategory(cat);
		saved.setCreatedAt(LocalDateTime.now());

		IncomeDto responseDto = new IncomeDto(11L, 200.0, "Salary", 3L, LocalDateTime.now(), LocalDateTime.now());

		when(incomeService.create(request, 1L)).thenReturn(saved);
		when(mapper.toIncomeDto(saved)).thenReturn(responseDto);

		Authentication auth = Mockito.mock(Authentication.class);
		when(auth.getName()).thenReturn("1");

		ResponseEntity<IncomeDto> resp = controller.createIncome(request, auth);

		assertEquals(200, resp.getStatusCodeValue());
		assertNotNull(resp.getBody());
		assertEquals(11L, resp.getBody().getId());
	}

	@Test
	void createExpense_whenValid_returnsExpenseDto() {
		IncomeService incomeService = Mockito.mock(IncomeService.class);
		ExpenseService expenseService = Mockito.mock(ExpenseService.class);
		ReportService reportService = Mockito.mock(ReportService.class);
		TransactionMapper mapper = Mockito.mock(TransactionMapper.class);

		TransactionController controller = new TransactionController(incomeService, expenseService, reportService, mapper);

		ExpenseDto request = new ExpenseDto(null, 50.0, "Lunch", 4L, null, null);

		Category cat = new Category();
		cat.setId(4L);

		Expense saved = new Expense();
		saved.setId(21L);
		saved.setAmount(50.0);
		saved.setDescription("Lunch");
		saved.setCategory(cat);
		saved.setCreatedAt(LocalDateTime.now());

		ExpenseDto responseDto = new ExpenseDto(21L, 50.0, "Lunch", 4L, LocalDateTime.now(), LocalDateTime.now());

		when(expenseService.create(request, 1L)).thenReturn(saved);
		when(mapper.toExpenseDto(saved)).thenReturn(responseDto);

		Authentication auth = Mockito.mock(Authentication.class);
		when(auth.getName()).thenReturn("1");

		ResponseEntity<ExpenseDto> resp = controller.createExpense(request, auth);

		assertEquals(200, resp.getStatusCodeValue());
		assertNotNull(resp.getBody());
		assertEquals(21L, resp.getBody().getId());
	}

	@Test
	void getAll_returnsTransactionDtos() {
		IncomeService incomeService = Mockito.mock(IncomeService.class);
		ExpenseService expenseService = Mockito.mock(ExpenseService.class);
		ReportService reportService = Mockito.mock(ReportService.class);
		TransactionMapper mapper = Mockito.mock(TransactionMapper.class);

		TransactionController controller = new TransactionController(incomeService, expenseService, reportService, mapper);

		TransactionDto t1 = new TransactionDto(1L, 100.0, "x", 2L, LocalDateTime.now(), "INCOME");
		TransactionDto t2 = new TransactionDto(2L, 50.0, "y", 3L, LocalDateTime.now(), "EXPENSE");

		when(reportService.findAllByUserId(1L)).thenReturn(List.of(t1, t2));

		Authentication auth = Mockito.mock(Authentication.class);
		when(auth.getName()).thenReturn("1");

		ResponseEntity<List<TransactionDto>> resp = controller.getAll(auth);

		assertEquals(200, resp.getStatusCodeValue());
		List<TransactionDto> body = resp.getBody();
		assertNotNull(body);
		assertEquals(2, body.size());
	}

	@Test
	void deleteIncome_callsServiceAndReturnsOk() {
		IncomeService incomeService = Mockito.mock(IncomeService.class);
		ExpenseService expenseService = Mockito.mock(ExpenseService.class);
		ReportService reportService = Mockito.mock(ReportService.class);
		TransactionMapper mapper = Mockito.mock(TransactionMapper.class);

		TransactionController controller = new TransactionController(incomeService, expenseService, reportService, mapper);

		Authentication auth = Mockito.mock(Authentication.class);
		when(auth.getName()).thenReturn("1");

		ResponseEntity<?> resp = controller.deleteIncome(5L, auth);

		assertEquals(200, resp.getStatusCodeValue());
		verify(incomeService).delete(5L, 1L);
	}

	@Test
	void deleteExpense_callsServiceAndReturnsOk() {
		IncomeService incomeService = Mockito.mock(IncomeService.class);
		ExpenseService expenseService = Mockito.mock(ExpenseService.class);
		ReportService reportService = Mockito.mock(ReportService.class);
		TransactionMapper mapper = Mockito.mock(TransactionMapper.class);

		TransactionController controller = new TransactionController(incomeService, expenseService, reportService, mapper);

		Authentication auth = Mockito.mock(Authentication.class);
		when(auth.getName()).thenReturn("1");

		ResponseEntity<?> resp = controller.deleteExpense(7L, auth);

		assertEquals(200, resp.getStatusCodeValue());
		verify(expenseService).delete(7L, 1L);
	}

	@Test
	void getReports_returnsTotalsMap() {
		IncomeService incomeService = Mockito.mock(IncomeService.class);
		ExpenseService expenseService = Mockito.mock(ExpenseService.class);
		ReportService reportService = Mockito.mock(ReportService.class);
		TransactionMapper mapper = Mockito.mock(TransactionMapper.class);

		TransactionController controller = new TransactionController(incomeService, expenseService, reportService, mapper);

		when(reportService.getTotalIncome(1L)).thenReturn(1000.0);
		when(reportService.getTotalExpense(1L)).thenReturn(500.0);

		Authentication auth = Mockito.mock(Authentication.class);
		when(auth.getName()).thenReturn("1");

		ResponseEntity<?> resp = controller.getReports(auth);
		assertEquals(200, resp.getStatusCodeValue());
		Object body = resp.getBody();
		assertTrue(body instanceof Map);
		Map<?, ?> map = (Map<?, ?>) body;
		assertEquals(1000.0, map.get("totalIncome"));
		assertEquals(500.0, map.get("totalExpense"));
	}

}
