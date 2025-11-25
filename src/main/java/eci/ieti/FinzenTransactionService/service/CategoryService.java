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

    public List<CategoryDto> findByUserIdAndType(Long userId, String type) {
        // Busca las del usuario O las predefinidas (userId=0) que coincidan con el tipo
        return mapper.toCategoryDtos(
                categoryRepository.findByUserIdAndTypeOrUserIdAndType(userId, type, 0L, type)
        );
    }

    public Category createCustomCategory(Long userId, CategoryDto dto) {
        // Validar duplicados por nombre y tipo
        if (categoryRepository.findByUserIdAndNameAndType(userId, dto.getName(), dto.getType()).isPresent()) {
            throw new IllegalArgumentException("Category already exists");
        }
        Category category = mapper.toCategory(dto);
        category.setUserId(userId);
        category.setPredefined(false);
        category.setType(dto.getType());
        category.setIcon(dto.getIcon());
        return categoryRepository.save(category);
    }
}