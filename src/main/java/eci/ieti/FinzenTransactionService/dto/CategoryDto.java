package eci.ieti.FinzenTransactionService.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryDto {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    private boolean predefined;
}