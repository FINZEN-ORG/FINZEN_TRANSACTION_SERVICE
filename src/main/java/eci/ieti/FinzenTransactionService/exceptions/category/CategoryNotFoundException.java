package eci.ieti.FinzenTransactionService.exceptions.category;

import eci.ieti.FinzenTransactionService.exceptions.EntityNotFoundException;

public class CategoryNotFoundException extends EntityNotFoundException {
    public CategoryNotFoundException(Long id) {
        super("Category not found with id: " + id);
    }
}