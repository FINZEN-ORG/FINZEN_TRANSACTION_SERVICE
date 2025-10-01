package eci.ieti.FinzenTransactionService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "budgets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private Double amount; // Monto restante

    @Column(nullable = false)
    private Double initialAmount; // Monto inicial

    private LocalDate startDate;

    private LocalDate endDate;

    @PrePersist
    @PreUpdate
    private void validateAmounts() {
        if (amount == null || amount < 0 || amount > Double.MAX_VALUE / 2 ||
                initialAmount == null || initialAmount < 0 || initialAmount > Double.MAX_VALUE / 2) {
            throw new IllegalArgumentException("Amount or initialAmount out of valid range");
        }
    }
}