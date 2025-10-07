package eci.ieti.FinzenTransactionService.service;

import eci.ieti.FinzenTransactionService.dto.CategoryDto;
import eci.ieti.FinzenTransactionService.model.Category;
import eci.ieti.FinzenTransactionService.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    public List<CategoryDto> findByUserId(Long userId) {
        return categoryRepository.findByUserIdOrUserId(userId, 0L).stream()
                .map(c -> new CategoryDto(c.getId(), c.getName(), c.isPredefined()))
                .collect(Collectors.toList());
    }

    public Category createCustomCategory(Long userId, String name) {
        Category category = Category.builder()
                .userId(userId)
                .name(name)
                .predefined(false)
                .build();
        return categoryRepository.save(category);
    }
}