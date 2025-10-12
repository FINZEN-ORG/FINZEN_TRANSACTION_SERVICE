package eci.ieti.FinzenTransactionService.mappers;

import eci.ieti.FinzenTransactionService.dto.*;
import eci.ieti.FinzenTransactionService.model.*;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    // Income
    IncomeDto toIncomeDto(Income entity);
    List<IncomeDto> toIncomeDtos(List<Income> entities);
    Income toIncome(IncomeDto dto);

    // Expense
    ExpenseDto toExpenseDto(Expense entity);
    List<ExpenseDto> toExpenseDtos(List<Expense> entities);
    Expense toExpense(ExpenseDto dto);

    // Budget
    BudgetDto toBudgetDto(Budget entity);
    List<BudgetDto> toBudgetDtos(List<Budget> entities);
    Budget toBudget(BudgetDto dto);

    // Category
    CategoryDto toCategoryDto(Category entity);
    List<CategoryDto> toCategoryDtos(List<Category> entities);
    Category toCategory(CategoryDto dto);

    // Legacy reports: Combina en TransactionDto con type
    TransactionDto toTransactionDto(Income income);
    TransactionDto toTransactionDto(Expense expense);

    default List<TransactionDto> toTransactionDtos(List<Income> incomes, List<Expense> expenses) {
        List<TransactionDto> result = new java.util.ArrayList<>();
        if (incomes != null) {
            for (Income i : incomes) {
                TransactionDto dto = toTransactionDto(i);
                dto.setType("INCOME");
                result.add(dto);
            }
        }
        if (expenses != null) {
            for (Expense e : expenses) {
                TransactionDto dto = toTransactionDto(e);
                dto.setType("EXPENSE");
                result.add(dto);
            }
        }
        return result;
    }
}