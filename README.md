# Finzen Transaction Service

Microservicio Spring Boot para gestión de transacciones financieras (ingresos, gastos, presupuestos y categorías). Soporta autenticación JWT, persistencia en PostgreSQL y APIs REST seguras.

## Requisitos
- Java 17+
- Maven 3.8+
- Docker & Docker Compose (para DB local)
- PostgreSQL 14+ (producción)

## Instalación Local
1. Clona el repo: `git clone https://github.com/FINZEN-ORG/FINZEN_TRANSACTION_SERVICE.git`.
2. Copia `.env` y configura vars.
3. Build: `mvn clean install`.
4. Run con Docker: `docker-compose up` (DB en puerto 5433, app en 8082).

## Refactor y Actualizaciones (Versión 0.0.2)
Este refactor se basa en 10 observaciones clave para mejorar seguridad, mantenibilidad y eficiencia. Se implementaron todas, con correcciones iterativas para compilación (e.g., tipos/constructores, acceso a setters), duplicación de código y documentación automática.

### Cambios por Observación
| Observación | Descripción | Cambios Implementados |
|-------------|-------------|-----------------------|
| **1. @Getter/@Setter vs @Data** | Evitar @Data en entidades sensibles (evita toString/hashCode con datos como amounts). | - Entidades (Category, Budget, Income, Expense): Cambiado a `@Getter/@Setter(AccessLevel.PUBLIC)`. <br> - DTOs: Mantuvo `@Data` (no sensibles). <br> - FIX: PUBLIC para acceso desde services/mappers. Seed usa constructores manuales. |
| **2. DTO en lugar de Map** | Front envía DTOs, no Maps (type-safe, @Valid). | - Nuevos DTOs: `IncomeDto`, `ExpenseDto`, `CategoryDto`, `BudgetDto` con `@NotNull/@Min`. <br> - Controllers: `@RequestBody DTO` + `@Valid`. <br> - FIX: Orden fijo en constructores + `@AllArgsConstructor`. |
| **3. Usar mapper (MapStruct)** | Auto-mapeo DTO ↔ Entity, sin manual. | - Nuevo: `TransactionMapper` interface (inyectado en services). <br> - Services/Controllers: `mapper.toDto(entity)` en lugar de `new DTO(...)`. <br> - FIX: `@Mapping` explícito para `categoryId` (evita type mismatch Double→Long). <br> - Config: Ya en `pom.xml`. |
| **4. Tablas separadas (ingresos/gastos)** | Separar responsabilidades (Income/Expense). | - Nuevas entidades/repos: `Income`/`incomes`, `Expense`/`expenses`. <br> - Eliminado: `Transaction` entity/repo. <br> - Endpoints: `/incomes`, `/expenses`. Budget solo linkea a expenses. <br> - Reports: `ReportService` combina para legacy. <br> - **Reducción Duplicación**: Income/Expense heredan de `@MappedSuperclass BaseTransaction` (campos/validaciones comunes). Repos/services no modificados – herencia transparente. SonarQube score mejorado. |
| **5. Campos createdAt/updatedAt** | Trazabilidad en todas tablas. | - Todas entidades: `@CreationTimestamp`/`@UpdateTimestamp` (Hibernate). <br> - Incluidos en DTOs para frontend. |
| **6. Modificar solo en corto periodo** | Updates/deletes solo <24h desde createdAt. | - Services: Chequeo `LocalDateTime.now().minusHours(24).isAfter(createdAt)` en `delete()`. <br> - Excepción: `ShortPeriodExpiredException` (403 Forbidden). |
| **7. Excepciones por entidad** | Custom exceptions organizadas. | - Paquete `exceptions/`: Subpaquetes (`income/`, `expense/`, etc.) con `XxxNotFoundException`. <br> - Base: `EntityNotFoundException`. |
| **8. Lanzar en service, capturar en controller** | Throws en services, manejo HTTP en handlers. | - Services: `throw new XxxException(...)`. <br> - Controllers: Sin try-catch (delegan a global). |
| **9. GlobalHandlerException** | Centraliza manejo de errores. | - Nuevo: `GlobalExceptionHandler` (@ControllerAdvice). <br> - Maneja @Valid (400), NotFound (404), ShortPeriod (403), genéricas (500). <br> - Respuestas: JSON `{ "error": "msg" }`. |
| **10. Lógica en service, controller orquesta** | Controllers delgados, services ricos. | - Controllers: Reciben DTO, llaman service, devuelven response. <br> - Services: Validaciones, mapeos, queries, business rules (e.g., category existe, userId match). |

### Archivos Nuevos/Modificados
- **Nuevos**:
    - Entidades: `Income.java`, `Expense.java`.
    - DTOs: `IncomeDto.java`, `ExpenseDto.java`.
    - Repos: `IncomeRepository.java`, `ExpenseRepository.java`.
    - Services: `IncomeService.java`, `ExpenseService.java`, `ReportService.java`.
    - Mapper: `TransactionMapper.java` (mappers/).
    - Exceptions: Todas en `exceptions/` (e.g., `IncomeNotFoundException.java`).
    - Handler: `GlobalExceptionHandler.java`.
- **Modificados**:
    - Entidades:`Transaction.java`(base heredada), `Budget.java`, `Category.java` (+timestamps, @Setter PUBLIC).
    - DTOs: `BudgetDto.java`, `CategoryDto.java`, `TransactionDto.java` (+validaciones, orden constructores).
    - Services: `BudgetService.java`, `CategoryService.java` (usa mappers/DTOs, métodos auxiliares como `getIncomesByUserId`).
    - Controllers: `BudgetController.java`, `CategoryController.java`, `TransactionController.java` (endpoints separados, mappers sin manual new).
    - Application: Seed con constructores manuales (new Category() + setters).
- **Eliminados**:`TransactionRepository.java` (reemplazados).

