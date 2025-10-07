package eci.ieti.FinzenTransactionService.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;  // 0 para categorias predefinidas globales, userId para personalizadas

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private boolean predefined = false;  // Indica si es una categoría inicial
}