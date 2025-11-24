package eci.ieti.FinzenTransactionService.repository;

import eci.ieti.FinzenTransactionService.dto.CategoryTotalDto;
import eci.ieti.FinzenTransactionService.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserId(Long userId);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.userId = :userId")
    BigDecimal sumByUserId(Long userId);

    @Query("SELECT new eci.ieti.FinzenTransactionService.dto.CategoryTotalDto(e.category.id, SUM(e.amount)) " +
            "FROM Expense e " +
            "WHERE e.userId = :userId " +
            "AND e.date BETWEEN :startDate AND :endDate " +
            "GROUP BY e.category.id")
    List<CategoryTotalDto> sumExpensesByCategoryAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}