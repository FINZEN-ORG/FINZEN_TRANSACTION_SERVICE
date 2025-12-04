package eci.ieti.FinzenTransactionService.controller;
/*
import eci.ieti.FinzenTransactionService.dto.CategoryDto;
import eci.ieti.FinzenTransactionService.model.Category;
import eci.ieti.FinzenTransactionService.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryControllerTest {

	@Test
	void createCustomCategory_whenValid_returnsCategoryDto() {
		CategoryService categoryService = Mockito.mock(CategoryService.class);
		CategoryController controller = new CategoryController(categoryService);

		CategoryDto dto = new CategoryDto(null, "Groceries", false);

		Category saved = new Category();
		saved.setId(5L);
		saved.setName("Groceries");
		saved.setPredefined(false);

		Mockito.when(categoryService.createCustomCategory(Mockito.eq(1L), Mockito.any(CategoryDto.class)))
				.thenReturn(saved);

		Authentication auth = Mockito.mock(Authentication.class);
		Mockito.when(auth.getName()).thenReturn("1");

		ResponseEntity<CategoryDto> response = controller.createCustomCategory(dto, auth);

		assertEquals(200, response.getStatusCode().value());
		CategoryDto body = response.getBody();
		assertNotNull(body);
		assertEquals(5L, body.getId());
		assertEquals("Groceries", body.getName());
		assertFalse(body.isPredefined());
	}

	@Test
	void getAll_whenCalled_returnsListOfCategoryDtos() {
		CategoryService categoryService = Mockito.mock(CategoryService.class);
		CategoryController controller = new CategoryController(categoryService);

		CategoryDto dto = new CategoryDto(5L, "Groceries", false);
		Mockito.when(categoryService.findByUserId(1L)).thenReturn(List.of(dto));

		Authentication auth = Mockito.mock(Authentication.class);
		Mockito.when(auth.getName()).thenReturn("1");

		ResponseEntity<List<CategoryDto>> response = controller.getAll(auth);

		assertEquals(200, response.getStatusCode().value());
		List<CategoryDto> body = response.getBody();
		assertNotNull(body);
		assertEquals(1, body.size());
		assertEquals(5L, body.get(0).getId());
	}
}
*/