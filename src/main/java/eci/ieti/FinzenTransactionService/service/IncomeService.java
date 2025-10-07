package eci.ieti.FinzenTransactionService.service;

import eci.ieti.FinzenTransactionService.dto.IncomeDto;
import eci.ieti.FinzenTransactionService.exceptions.category.CategoryNotFoundException;
import eci.ieti.FinzenTransactionService.exceptions.income.IncomeNotFoundException;
import eci.ieti.FinzenTransactionService.exceptions.transaction.ShortPeriodExpiredException;
import eci.ieti.FinzenTransactionService.mappers.TransactionMapper;
import eci.ieti.FinzenTransactionService.model.Category;
import eci.ieti.FinzenTransactionService.model.Income;
import eci.ieti.FinzenTransactionService.repository.CategoryRepository;
import eci.ieti.FinzenTransactionService.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionMapper mapper;

    public Income create(IncomeDto dto, Long userId) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(dto.getCategoryId()));
        Income income = mapper.toIncome(dto);
        income.setUserId(userId);
        income.setDate(LocalDateTime.now());
        income.setCategory(category);
        return incomeRepository.save(income);
    }
    public List<Income> getIncomesByUserId(Long userId) {
        return incomeRepository.findByUserId(userId);
    }

    public List<IncomeDto> findByUserId(Long userId) {
        return mapper.toIncomeDtos(incomeRepository.findByUserId(userId));
    }

    public Double getTotalIncome(Long userId) {
        return incomeRepository.sumByUserId(userId);
    }

    public void delete(Long id, Long userId) {
        Income income = incomeRepository.findById(id)
                .filter(i -> i.getUserId().equals(userId))
                .orElseThrow(() -> new IncomeNotFoundException(id));
        if (LocalDateTime.now().minusHours(24).isAfter(income.getCreatedAt())) {
            throw new ShortPeriodExpiredException("Income", id);
        }
        incomeRepository.delete(income);
    }
}