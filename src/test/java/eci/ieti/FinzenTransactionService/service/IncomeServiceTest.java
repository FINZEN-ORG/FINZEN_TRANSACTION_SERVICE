package eci.ieti.FinzenTransactionService.service;

import eci.ieti.FinzenTransactionService.dto.IncomeDto;
import eci.ieti.FinzenTransactionService.mappers.TransactionMapper;
import eci.ieti.FinzenTransactionService.model.Category;
import eci.ieti.FinzenTransactionService.model.Income;
import eci.ieti.FinzenTransactionService.repository.CategoryRepository;
import eci.ieti.FinzenTransactionService.repository.IncomeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class IncomeServiceTest {

	@Test
	void create_whenCategoryExists_savesIncome() {
		IncomeRepository incomeRepository = Mockito.mock(IncomeRepository.class);
		CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
		TransactionMapper mapper = Mockito.mock(TransactionMapper.class);

		IncomeService service = new IncomeService(incomeRepository, categoryRepository, mapper);

		IncomeDto dto = new IncomeDto(null, 120.0, "Gift", 2L, null, null);

		Category cat = new Category();
		cat.setId(2L);

		Income incomeEntity = new Income();
		incomeEntity.setAmount(120.0);

		Mockito.when(categoryRepository.findById(2L)).thenReturn(Optional.of(cat));
		Mockito.when(mapper.toIncome(dto)).thenReturn(incomeEntity);
		Mockito.when(incomeRepository.save(Mockito.any(Income.class))).thenAnswer(i -> {
			Income in = i.getArgument(0);
			in.setId(9L);
			in.setCreatedAt(LocalDateTime.now());
			return in;
		});

		Income result = service.create(dto, 1L);
		assertNotNull(result);
		assertEquals(9L, result.getId());
		assertEquals(120.0, result.getAmount());
	}

	@Test
	void getTotalIncome_returnsSum() {
		IncomeRepository incomeRepository = Mockito.mock(IncomeRepository.class);
		CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
		TransactionMapper mapper = Mockito.mock(TransactionMapper.class);

		IncomeService service = new IncomeService(incomeRepository, categoryRepository, mapper);

		Mockito.when(incomeRepository.sumByUserId(1L)).thenReturn(300.0);

		assertEquals(300.0, service.getTotalIncome(1L));
	}
}
