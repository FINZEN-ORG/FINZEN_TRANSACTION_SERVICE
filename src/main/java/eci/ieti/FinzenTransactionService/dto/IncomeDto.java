package eci.ieti.FinzenTransactionService.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IncomeDto {
    private Long id;

    @NotNull(message = "Amount is required") @Min(value = 0.01, message = "Amount must be positive")
    private Double amount;

    private String description;

    private LocalDateTime date;

    @NotNull(message = "CategoryId is required")
    private Long categoryId;

    private LocalDateTime createdAt;
}