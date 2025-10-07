package eci.ieti.FinzenTransactionService.mappers;

import eci.ieti.FinzenTransactionService.dto.*;
import eci.ieti.FinzenTransactionService.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class}) // Usa sub-mappers si crecen
public interface TransactionMapper { // Mapper principal, pero lo renombramos implícitamente
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    // Para Income (nuevo)
    Income toIncome(CreateIncomeDto dto);
    IncomeDto toIncomeDto(Income entity);
    List<IncomeDto> toIncomeDtos(List<Income> entities);

    // Para Expense (nuevo)
    Expense toExpense(CreateExpenseDto dto);
    ExpenseDto toExpenseDto(Expense entity);
    List<ExpenseDto> toExpenseDtos(List<Expense> entities);

    // Legacy para reports (combina Income+Expense como "Transaction")
    TransactionDto toTransactionDto(Income income); // Mapea fields comunes
    TransactionDto toTransactionDto(Expense expense);
    List<TransactionDto> toTransactionDtos(List<Income> incomes, List<Expense> expenses); // Combina

    // Budget (existente)
    Budget toBudget(CreateBudgetDto dto); // Nuevo DTO de entrada
    BudgetDto toBudgetDto(Budget entity);

    // Category (existente)
    Category toCategory(CreateCategoryDto dto); // Nuevo DTO
    CategoryDto toCategoryDto(Category entity);
}