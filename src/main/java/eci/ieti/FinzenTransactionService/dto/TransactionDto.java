package eci.ieti.FinzenTransactionService.dto;

import eci.ieti.FinzenTransactionService.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TransactionDto {
    private Long id;
    private TransactionType type;
    private Double amount;
    private LocalDateTime date;
    private Long categoryId;
    private String description;
}