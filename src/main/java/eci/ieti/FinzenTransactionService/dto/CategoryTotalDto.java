package eci.ieti.FinzenTransactionService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryTotalDto {
    private Long categoryId;
    private BigDecimal totalAmount;
}