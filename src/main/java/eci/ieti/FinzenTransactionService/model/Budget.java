package eci.ieti.FinzenTransactionService.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "budgets")
@Getter
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
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

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    private void validateAmounts() {
        if (amount == null || amount < 0 || amount > Double.MAX_VALUE / 2 ||
                initialAmount == null || initialAmount < 0 || initialAmount > Double.MAX_VALUE / 2) {
            throw new IllegalArgumentException("Amount or initialAmount out of valid range");
        }
    }
}