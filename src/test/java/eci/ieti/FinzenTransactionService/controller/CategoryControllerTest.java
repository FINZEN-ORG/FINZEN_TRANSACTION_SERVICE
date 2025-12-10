package eci.ieti.FinzenTransactionService.controller;

import eci.ieti.FinzenTransactionService.dto.CategoryDto;
import eci.ieti.FinzenTransactionService.model.Category;
import eci.ieti.FinzenTransactionService.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private CategoryDto testCategoryDto;
    private Category testCategory;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        testCategoryDto = new CategoryDto(
                1L,
                "Test Category",
                "EXPENSE",
                "test-icon",
                false
        );

        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Test Category");
        testCategory.setType("EXPENSE");
        testCategory.setIcon("test-icon");
        testCategory.setPredefined(false);
        testCategory.setUserId(1L);

        authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn("1");
    }

    @Test
    void testGetAll_whenCalled_returnsListOfCategoryDtos() {
        // Arrange
        List<CategoryDto> categoryDtos = Arrays.asList(testCategoryDto);
        when(categoryService.findByUserIdAndType(eq(1L), eq("EXPENSE"))).thenReturn(categoryDtos);

        // Act
        ResponseEntity<List<CategoryDto>> response = categoryController.getAll("EXPENSE", authentication);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        assertEquals("Test Category", response.getBody().get(0).getName());
        verify(categoryService, times(1)).findByUserIdAndType(eq(1L), eq("EXPENSE"));
    }

    @Test
    void testCreateCustomCategory_whenValid_returnsCategoryDto() {
        // Arrange
        when(categoryService.createCustomCategory(eq(1L), any(CategoryDto.class))).thenReturn(testCategory);

        // Act
        ResponseEntity<CategoryDto> response = categoryController.createCustomCategory(testCategoryDto, authentication);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Test Category", response.getBody().getName());
        assertEquals("EXPENSE", response.getBody().getType());
        verify(categoryService, times(1)).createCustomCategory(eq(1L), any(CategoryDto.class));
    }

    @Test
    void testDelete_whenCalled_returnsOk() {
        // Arrange
        doNothing().when(categoryService).delete(eq(1L), eq(1L));

        // Act
        ResponseEntity<Void> response = categoryController.delete(1L, authentication);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        verify(categoryService, times(1)).delete(eq(1L), eq(1L));
    }
}
