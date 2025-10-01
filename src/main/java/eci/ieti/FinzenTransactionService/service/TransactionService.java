package eci.ieti.FinzenTransactionService.service;

import eci.ieti.FinzenTransactionService.dto.TransactionDto;
import eci.ieti.FinzenTransactionService.model.Transaction;
import eci.ieti.FinzenTransactionService.model.TransactionType;
import eci.ieti.FinzenTransactionService.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar operaciones relacionadas con transacciones financieras
 */
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final BudgetService budgetService;

    public TransactionService(TransactionRepository transactionRepository, BudgetService budgetService) {
        this.transactionRepository = transactionRepository;
        this.budgetService = budgetService;
    }

    public Transaction create(Transaction transaction) {
        Transaction saved = transactionRepository.save(transaction);
        if (transaction.getType() == TransactionType.EXPENSE) {
            // Actualizar presupuesto restando el monto, usando categoryId
            budgetService.updateBudgetOnExpense(transaction.getUserId(), transaction.getCategory().getId(), transaction.getAmount());
        }
        return saved;
    }

    public List<TransactionDto> findByUserId(Long userId) {
        return transactionRepository.findByUserId(userId).stream()
                .map(t -> new TransactionDto(t.getId(), t.getType(), t.getAmount(), t.getDate(), t.getCategory().getId(), t.getDescription()))
                .collect(Collectors.toList());
    }

    public Double getTotalIncome(Long userId) {
        return transactionRepository.findByUserId(userId).stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public Double getTotalExpense(Long userId) {
        return transactionRepository.findByUserId(userId).stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public void delete(Long id) {
        transactionRepository.deleteById(id);
    }
}