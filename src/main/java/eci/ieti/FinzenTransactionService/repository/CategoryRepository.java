package eci.ieti.FinzenTransactionService.repository;

import eci.ieti.FinzenTransactionService.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUserIdOrPredefined(Long userId, boolean predefined);
    Optional<Category> findByUserIdAndName(Long userId, String name);
    Optional<Category> findByName(String name);
    // Buscar por usuario y tipo (incluyendo las predefinidas del sistema userId=0)
    List<Category> findByUserIdAndTypeOrUserIdAndType(Long userId, String type1, Long systemId, String type2);
    Optional<Category> findByUserIdAndNameAndType(Long userId, String name, String type);
}