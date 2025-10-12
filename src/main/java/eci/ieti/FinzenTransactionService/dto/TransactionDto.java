package eci.ieti.FinzenTransactionService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TransactionDto {
    private Long id;
    private Double amount;
    private String description;
    private Long categoryId;
    private LocalDateTime date;
    private String type;
}