package eci.ieti.FinzenTransactionService.service;

import eci.ieti.FinzenTransactionService.dto.CategoryDto;
import eci.ieti.FinzenTransactionService.mappers.TransactionMapper;
import eci.ieti.FinzenTransactionService.model.Category;
import eci.ieti.FinzenTransactionService.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CategoryServiceTest {

	@Test
	void createCustomCategory_whenNameAvailable_savesCategory() {
		CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
		TransactionMapper mapper = Mockito.mock(TransactionMapper.class);

		CategoryService service = new CategoryService(categoryRepository, mapper);

		CategoryDto dto = new CategoryDto(null, "Travel", false);

		Category catEntity = new Category();
		catEntity.setName("Travel");

		Mockito.when(categoryRepository.findByName("Travel")).thenReturn(Optional.empty());
		Mockito.when(mapper.toCategory(dto)).thenReturn(catEntity);
		Mockito.when(categoryRepository.save(Mockito.any(Category.class))).thenAnswer(i -> {
			Category c = i.getArgument(0);
			c.setId(22L);
			return c;
		});

		Category result = service.createCustomCategory(1L, dto);
		assertNotNull(result);
		assertEquals(22L, result.getId());
		assertFalse(result.isPredefined());
	}
}
