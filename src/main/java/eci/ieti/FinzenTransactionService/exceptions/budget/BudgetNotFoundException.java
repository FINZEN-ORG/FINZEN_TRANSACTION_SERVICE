package eci.ieti.FinzenTransactionService.exceptions.budget;

import eci.ieti.FinzenTransactionService.exceptions.EntityNotFoundException;

public class BudgetNotFoundException extends EntityNotFoundException {
    public BudgetNotFoundException(Long userId, Long categoryId) {
        super("Budget not found for user " + userId + " and category " + categoryId);
    }
}