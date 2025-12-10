package eci.ieti.FinzenTransactionService.service;

import eci.ieti.FinzenTransactionService.dto.CategoryDto;
import eci.ieti.FinzenTransactionService.exceptions.EntityNotFoundException;
import eci.ieti.FinzenTransactionService.mappers.TransactionMapper;
import eci.ieti.FinzenTransactionService.model.Category;
import eci.ieti.FinzenTransactionService.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TransactionMapper mapper;

    @InjectMocks
    private CategoryService categoryService;

    private Category testCategory;
    private CategoryDto testCategoryDto;
    private final Long USER_ID = 1L;
    private final Long SYSTEM_ID = 0L;

    @BeforeEach
    void setUp() {
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setUserId(USER_ID);
        testCategory.setName("Test Category");
        testCategory.setType("EXPENSE");
        testCategory.setIcon("test-icon");
        testCategory.setPredefined(false);

        testCategoryDto = new CategoryDto(
                1L,
                "Test Category",
                "EXPENSE",
                "test-icon",
                false
        );
    }

    @Test
    void testCreateCategory() {

        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);
        Category result = categoryService.create(testCategory);
        assertNotNull(result);
        assertEquals("Test Category", result.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testFindByUserIdAndType() {
        List<Category> categories = Arrays.asList(testCategory);
        List<CategoryDto> categoryDtos = Arrays.asList(testCategoryDto);
        when(categoryRepository.findByUserIdAndTypeOrUserIdAndType(
                eq(USER_ID), eq("EXPENSE"), eq(SYSTEM_ID), eq("EXPENSE")))
                .thenReturn(categories);
        when(mapper.toCategoryDtos(categories)).thenReturn(categoryDtos);
        List<CategoryDto> result = categoryService.findByUserIdAndType(USER_ID, "EXPENSE");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Category", result.get(0).getName());
    }

    @Test
    void testDeleteCategory_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        doNothing().when(categoryRepository).delete(testCategory);
        categoryService.delete(1L, USER_ID);
        verify(categoryRepository, times(1)).delete(testCategory);
    }

    @Test
    void testDeleteCategory_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            categoryService.delete(1L, USER_ID);
        });
        assertEquals("Category not found", exception.getMessage());
    }

    @Test
    void testDeleteCategory_WrongUser() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            categoryService.delete(1L, 999L);
        });
        assertEquals("Unauthorized", exception.getMessage());
        verify(categoryRepository, never()).delete(any());
    }

    @Test
    void testDeleteCategory_Predefined() {
        testCategory.setPredefined(true);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            categoryService.delete(1L, USER_ID);
        });
        assertEquals("No puedes eliminar categorías predefinidas del sistema.", exception.getMessage());
        verify(categoryRepository, never()).delete(any());
    }

    @Test
    void testCreateCustomCategory_Success() {
        testCategory.setPredefined(false);
        when(categoryRepository.findByUserIdAndNameAndType(USER_ID, "Test Category", "EXPENSE"))
                .thenReturn(Optional.empty());
        when(categoryRepository.findByUserIdAndNameAndType(SYSTEM_ID, "Test Category", "EXPENSE"))
                .thenReturn(Optional.empty());
        when(mapper.toCategory(testCategoryDto)).thenReturn(testCategory);
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);
        Category result = categoryService.createCustomCategory(USER_ID, testCategoryDto);
        assertNotNull(result);
        assertEquals("Test Category", result.getName());
        assertFalse(result.isPredefined());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testCreateCustomCategory_DuplicateUserCategory() {
        when(categoryRepository.findByUserIdAndNameAndType(USER_ID, "Test Category", "EXPENSE"))
                .thenReturn(Optional.of(testCategory));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            categoryService.createCustomCategory(USER_ID, testCategoryDto);
        });
        assertEquals("Ya existe una categoría con el nombre 'Test Category'", exception.getMessage());
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void testCreateCustomCategory_DuplicateSystemCategory() {
        when(categoryRepository.findByUserIdAndNameAndType(USER_ID, "Test Category", "EXPENSE"))
                .thenReturn(Optional.empty());
        when(categoryRepository.findByUserIdAndNameAndType(SYSTEM_ID, "Test Category", "EXPENSE"))
                .thenReturn(Optional.of(testCategory));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            categoryService.createCustomCategory(USER_ID, testCategoryDto);
        });
        assertEquals("Ya existe una categoría con el nombre 'Test Category'", exception.getMessage());
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void testFindByUserIdAndType_ReturnsEmpty() {
        when(categoryRepository.findByUserIdAndTypeOrUserIdAndType(
                eq(USER_ID), eq("INCOME"), eq(SYSTEM_ID), eq("INCOME")))
                .thenReturn(Arrays.asList());
        when(mapper.toCategoryDtos(Arrays.asList())).thenReturn(Arrays.asList());
        List<CategoryDto> result = categoryService.findByUserIdAndType(USER_ID, "INCOME");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}