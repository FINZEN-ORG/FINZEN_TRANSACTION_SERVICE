package eci.ieti.FinzenTransactionService.exceptions.expense;

import eci.ieti.FinzenTransactionService.exceptions.EntityNotFoundException;

public class ExpenseNotFoundException extends EntityNotFoundException {
    public ExpenseNotFoundException(Long id) {
        super("Expense not found with id: " + id);
    }
}