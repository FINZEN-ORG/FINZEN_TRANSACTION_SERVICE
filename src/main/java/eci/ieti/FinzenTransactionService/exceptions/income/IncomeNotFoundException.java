package eci.ieti.FinzenTransactionService.exceptions.income;

import eci.ieti.FinzenTransactionService.exceptions.EntityNotFoundException;

public class IncomeNotFoundException extends EntityNotFoundException {
    public IncomeNotFoundException(Long id) {
        super("Income not found with id: " + id);
    }
}