package eci.ieti.FinzenTransactionService;

import eci.ieti.FinzenTransactionService.model.Category;
import eci.ieti.FinzenTransactionService.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.Map;

@SpringBootApplication
public class FinzenTransactionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinzenTransactionServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner initCategories(CategoryRepository categoryRepository) {
		return args -> {
			// Solo inicializamos si la tabla está vacía
			if (categoryRepository.count() == 0) {

				// 1. Definir Categorías de GASTOS (EXPENSE) con sus emojis
				Map<String, String> expenses = Map.of(
						"Food", "🍔",
						"Transport", "⛽",
						"Entertainment", "🎬",
						"Health", "🏥",
						"Housing", "🏠",
						"Education", "🎓",
						"Other", "📦"
				);

				expenses.forEach((name, icon) -> {
					Category cat = new Category();
					cat.setUserId(0L); // ID de sistema
					cat.setName(name);
					cat.setIcon(icon);
					cat.setType("EXPENSE");
					cat.setPredefined(true);
					categoryRepository.save(cat);
				});

				// 2. Definir Categorías de INGRESOS (INCOME) con sus emojis
				Map<String, String> incomes = Map.of(
						"Salary", "💼",
						"Business", "🏪",
						"Gift", "🎁",
						"Investment", "📈",
						"Other", "💰"
				);

				incomes.forEach((name, icon) -> {
					Category cat = new Category();
					cat.setUserId(0L);
					cat.setName(name);
					cat.setIcon(icon);
					cat.setType("INCOME");
					cat.setPredefined(true);
					categoryRepository.save(cat);
				});

				System.out.println("✅ Categorías predefinidas inicializadas con éxito.");
			}
		};
	}
}