package eci.ieti.FinzenTransactionService.service;

import eci.ieti.FinzenTransactionService.dto.CategoryDto;
import eci.ieti.FinzenTransactionService.exceptions.EntityNotFoundException;
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
        // 1. Verificar si ya existe una categoría con ese nombre y tipo para este usuario
        boolean existsUser = categoryRepository.findByUserIdAndNameAndType(userId, dto.getName(), dto.getType()).isPresent();
        // 2. Verificar si es un nombre reservado del sistema (userId = 0)
        boolean existsSystem = categoryRepository.findByUserIdAndNameAndType(0L, dto.getName(), dto.getType()).isPresent();
        if (existsUser || existsSystem) {
            throw new IllegalArgumentException("Ya existe una categoría con el nombre '" + dto.getName() + "'");
        }
        Category category = mapper.toCategory(dto);
        category.setUserId(userId);
        category.setPredefined(false);
        category.setType(dto.getType());
        category.setIcon(dto.getIcon());
        return categoryRepository.save(category);
    }

    public void delete(Long id, Long userId) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        // Validar propiedad
        if (!category.getUserId().equals(userId) && category.getUserId() != 0L) {
            throw new RuntimeException("Unauthorized");
        }
        // VALIDACIÓN DE ORO: No borrar predefinidas
        if (category.isPredefined()) {
            throw new IllegalArgumentException("No puedes eliminar categorías predefinidas del sistema.");
        }
        categoryRepository.delete(category);
    }
}