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

