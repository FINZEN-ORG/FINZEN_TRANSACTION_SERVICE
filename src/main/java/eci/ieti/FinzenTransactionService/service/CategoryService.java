package eci.ieti.FinzenTransactionService.service;

import eci.ieti.FinzenTransactionService.dto.CategoryDto;
import eci.ieti.FinzenTransactionService.mappers.TransactionMapper;
import eci.ieti.FinzenTransactionService.model.Category;
import eci.ieti.FinzenTransactionService.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final TransactionMapper mapper;

    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    public List<CategoryDto> findByUserId(Long userId) {
        return mapper.toCategoryDtos(categoryRepository.findByUserIdOrUserId(userId, 0L));
    }

    public Category createCustomCategory(Long userId, CategoryDto dto) {
        if (categoryRepository.findByName(dto.getName()).isPresent()) {
            throw new IllegalArgumentException("Category name already exists");
        }
        Category category = mapper.toCategory(dto);
        category.setUserId(userId);
        category.setPredefined(false);
        return categoryRepository.save(category);
    }
}