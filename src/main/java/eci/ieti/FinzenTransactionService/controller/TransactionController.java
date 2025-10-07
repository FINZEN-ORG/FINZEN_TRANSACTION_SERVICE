package eci.ieti.FinzenTransactionService.controller;

import eci.ieti.FinzenTransactionService.dto.TransactionDto;
import eci.ieti.FinzenTransactionService.model.Category;
import eci.ieti.FinzenTransactionService.model.TransactionType;
import eci.ieti.FinzenTransactionService.repository.CategoryRepository;
import eci.ieti.FinzenTransactionService.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final CategoryRepository categoryRepository;

    public TransactionController(TransactionService transactionService, CategoryRepository categoryRepository) {
        this.transactionService = transactionService;
        this.categoryRepository = categoryRepository;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, Object> dto, Authentication authentication) {
        try {
            Long userId = Long.valueOf(authentication.getName());
            // Extraer campos del JSON como Map
            String typeStr = (String) dto.get("type");
            if (typeStr == null) {
                throw new IllegalArgumentException("Type is required");
            }
            Double amount = ((Number) dto.get("amount")).doubleValue();
            Long categoryId = ((Number) dto.get("categoryId")).longValue();
            String description = (String) dto.get("description");

            // Validar y obtener category
            Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
            Category category = categoryOpt.orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + categoryId));
            TransactionType type = TransactionType.valueOf(typeStr.toUpperCase());

            Transaction transaction = Transaction.builder()
                    .userId(userId)
                    .type(type)
                    .amount(amount)
                    .category(category)
                    .description(description)
                    .date(LocalDateTime.now())
                    .build();
            Transaction saved = transactionService.create(transaction);
            return ResponseEntity.ok(new TransactionDto(saved.getId(), saved.getType(), saved.getAmount(), saved.getDate(), saved.getCategory().getId(), saved.getDescription()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid request data: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<TransactionDto>> getAll(Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());
        return ResponseEntity.ok(transactionService.findByUserId(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());
        transactionService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reports")
    public ResponseEntity<?> getReports(Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());
        return ResponseEntity.ok(Map.of(
                "totalIncome", transactionService.getTotalIncome(userId),
                "totalExpense", transactionService.getTotalExpense(userId)
        ));
    }
}