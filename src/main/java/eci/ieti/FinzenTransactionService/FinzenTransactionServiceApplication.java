package eci.ieti.FinzenTransactionService;

import eci.ieti.FinzenTransactionService.model.Category;
import eci.ieti.FinzenTransactionService.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FinzenTransactionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinzenTransactionServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner initCategories(CategoryRepository categoryRepository) {
		return args -> {
			if (categoryRepository.findAll().isEmpty()) {
				String[] predefined = {"Food", "Transport", "Entertainment", "Health", "Housing", "Salary", "Other"};
				for (String name : predefined) {
					categoryRepository.save(Category.builder()
							.userId(0L) // Global
							.name(name)
							.predefined(true)
							.build());
				}
			}
		};
	}
}