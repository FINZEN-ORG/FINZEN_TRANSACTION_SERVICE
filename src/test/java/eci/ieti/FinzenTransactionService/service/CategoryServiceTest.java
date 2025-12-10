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
}