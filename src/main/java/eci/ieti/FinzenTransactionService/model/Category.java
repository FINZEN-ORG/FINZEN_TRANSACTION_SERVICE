package eci.ieti.FinzenTransactionService.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "categories", indexes = {
        @Index(columnList = "userId,name,type", name = "idx_user_category_name_type")
})
@Getter
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String type; // VALORES: "INCOME" o "EXPENSE"
    @Column(nullable = false)
    private String icon;
    @Column(nullable = false)
    private boolean predefined = false;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}