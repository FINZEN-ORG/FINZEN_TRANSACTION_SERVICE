package eci.ieti.FinzenTransactionService.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ExpenseDto {
    private Long id;
    @NotNull(message = "Amount is required") @Min(value = 0)
    private BigDecimal amount;
    private String description;
    @NotNull(message = "CategoryId is required")
    private Long categoryId;
    private LocalDateTime date;
    private LocalDateTime createdAt;
}