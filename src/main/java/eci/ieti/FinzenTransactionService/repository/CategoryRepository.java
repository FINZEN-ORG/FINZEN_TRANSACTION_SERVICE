package eci.ieti.FinzenTransactionService.repository;

import eci.ieti.FinzenTransactionService.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUserIdOrUserId(Long userId, Long globalId);
    Optional<Category> findByName(String name);
}