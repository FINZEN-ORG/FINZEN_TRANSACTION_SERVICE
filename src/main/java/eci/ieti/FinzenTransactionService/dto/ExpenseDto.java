package eci.ieti.FinzenTransactionService.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ExpenseDto {
    private Long id;
    @NotNull @Min(value = 0)
    private Double amount;
    private String description;
    private LocalDateTime date;
    @NotNull
    private Long categoryId;
    private LocalDateTime createdAt;
}