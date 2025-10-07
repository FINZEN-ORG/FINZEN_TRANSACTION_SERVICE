package eci.ieti.FinzenTransactionService.exceptions.transaction;

public class ShortPeriodExpiredException extends RuntimeException {
    public ShortPeriodExpiredException(String entityType, Long id) {
        super(entityType + " with id " + id + " cannot be modified: 24h period expired");
    }
}