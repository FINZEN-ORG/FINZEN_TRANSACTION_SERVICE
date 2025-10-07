package eci.ieti.FinzenTransactionService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class BudgetDto {
    private Long id;
    private Long categoryId;
    private Double amount;        // Monto restante
    private Double initialAmount; // Monto inicial
    private LocalDate startDate;
    private LocalDate endDate;
}