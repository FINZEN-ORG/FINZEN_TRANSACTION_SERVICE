package eci.ieti.FinzenTransactionService.mappers;

import eci.ieti.FinzenTransactionService.dto.*;
import eci.ieti.FinzenTransactionService.model.*;
import org.mapstruct.*;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    // ===== INCOME MAPPINGS =====

    @Mapping(source = "category.id", target = "categoryId")
    IncomeDto toIncomeDto(Income entity);

    List<IncomeDto> toIncomeDtos(List<Income> entities);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Income toIncome(IncomeDto dto);

    // ===== EXPENSE MAPPINGS =====

    @Mapping(source = "category.id", target = "categoryId")
    ExpenseDto toExpenseDto(Expense entity);

    List<ExpenseDto> toExpenseDtos(List<Expense> entities);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Expense toExpense(ExpenseDto dto);

    // ===== BUDGET MAPPINGS =====

    @Mapping(source = "category.id", target = "categoryId")
    BudgetDto toBudgetDto(Budget entity);

    List<BudgetDto> toBudgetDtos(List<Budget> entities);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Budget toBudget(BudgetDto dto);

    // ===== CATEGORY MAPPINGS =====

    CategoryDto toCategoryDto(Category entity);

    List<CategoryDto> toCategoryDtos(List<Category> entities);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Category toCategory(CategoryDto dto);

    // ===== TRANSACTION DTO MAPPINGS (for reports) =====

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(target = "type", constant = "INCOME")
    TransactionDto toTransactionDto(Income income);

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(target = "type", constant = "EXPENSE")
    TransactionDto toTransactionDto(Expense expense);

    // Método default para combinar incomes y expenses
    default List<TransactionDto> toTransactionDtos(List<Income> incomes, List<Expense> expenses) {
        List<TransactionDto> result = new java.util.ArrayList<>();
        if (incomes != null) {
            result.addAll(incomes.stream()
                    .map(this::toTransactionDto)
                    .collect(Collectors.toList()));
        }
        if (expenses != null) {
            result.addAll(expenses.stream()
                    .map(this::toTransactionDto)
                    .collect(Collectors.toList()));
        }
        return result;
    }
}