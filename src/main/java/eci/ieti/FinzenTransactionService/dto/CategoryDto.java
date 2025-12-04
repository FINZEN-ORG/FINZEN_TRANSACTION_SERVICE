package eci.ieti.FinzenTransactionService.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private Long id;
    @NotBlank(message = "Name is required")
    private String name;
    private String type; // "INCOME" | "EXPENSE"
    private String icon;
    private boolean predefined;
}