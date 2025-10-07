package eci.ieti.FinzenTransactionService.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExpenseDto {
    private Long id;

    @NotNull @Min(value = 0.01)
    private Double amount;

    private String description;

    private LocalDateTime date;

    @NotNull
    private Long categoryId;

    private LocalDateTime createdAt;
}