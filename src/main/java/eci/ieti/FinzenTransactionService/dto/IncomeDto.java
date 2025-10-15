package eci.ieti.FinzenTransactionService.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class IncomeDto {
    private Long id;
    @NotNull(message = "Amount is required") @Min(value = 0)
    private Double amount;
    private String description;
    @NotNull(message = "CategoryId is required")
    private Long categoryId;
    private LocalDateTime date;
    private LocalDateTime createdAt;
}