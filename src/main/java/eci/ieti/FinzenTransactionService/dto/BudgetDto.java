package eci.ieti.FinzenTransactionService.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class BudgetDto {
    private Long id;

    @NotNull @Min(1)
    private Long categoryId;

    @NotNull @Min(0)
    private Double amount;

    @NotNull @Min(0)
    private Double initialAmount;

    private LocalDate startDate;

    private LocalDate endDate;
}