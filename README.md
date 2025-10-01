# FinzenTransactionService

FinzenTransactionService es una aplicación backend desarrollada con Spring Boot para gestionar transacciones financieras, presupuestos y categorías. Utiliza Spring Data JPA para interactuar con una base de datos relacional y Spring Security con JWT para autenticación. Este documento describe las entidades principales del proyecto: `Category`, `Transaction` y `Budget`.

## Entidades

### 1. Category (Archivo: `Category.java`)

**Propósito**: Representa categorías para clasificar transacciones y presupuestos. Las categorías pueden ser **predefinidas** (globales, compartidas por todos los usuarios, con `userId = 0`) o **personalizadas** (creadas por un usuario específico). Esto permite organizar gastos e ingresos en categorías como "Comida", "Transporte", etc.

**Campos clave**:
- `id`: ID único autogenerado (clave primaria).
- `userId`: ID del usuario dueño (0 para categorías predefinidas/globales).
- `name`: Nombre de la categoría (único, no nulo).
- `predefined`: Booleano que indica si es predefinida (por defecto `false` para personalizadas).

**Relaciones**:
- No tiene relaciones directas como `@ManyToOne`, pero es referenciada por `Transaction` y `Budget` (una categoría puede tener múltiples transacciones y presupuestos).

**Validaciones/Comportamiento**:
- El nombre debe ser único en la base de datos.
- En el servicio (`CategoryService`), se pueden crear categorías personalizadas y listar tanto las globales como las del usuario.

**Uso en la aplicación**:
- Usada en transacciones para categorizar gastos o ingresos (e.g., gasto en "Comida").
- Usada en presupuestos para limitar gastos por categoría.

---

### 2. Transaction (Archivo: `Transaction.java`)

**Propósito**: Representa una transacción financiera individual, como un ingreso (`INCOME`) o gasto (`EXPENSE`). Registra movimientos de dinero, actualiza presupuestos automáticamente (resta de presupuestos en gastos) y permite generar reportes de totales.

**Campos clave**:
- `id`: ID único autogenerado.
- `type`: Tipo de transacción (enum: `INCOME` o `EXPENSE`, almacenado como string en la BD).
- `amount`: Monto (double, no nulo, validado para ser positivo y no exceder límites para evitar overflow).
- `description`: Descripción opcional.
- `date`: Fecha y hora de la transacción (`LocalDateTime`, no nulo, por defecto `now()` al crear).
- `category`: Relación con una categoría (`@ManyToOne`, lazy fetch, no nulo).
- `userId`: ID del usuario dueño (no nulo).

**Relaciones**:
- Muchas transacciones por categoría (`ManyToOne` con `Category`).

**Validaciones/Comportamiento**:
- `@PrePersist` y `@PreUpdate`: Valida que `amount` sea no nulo, >=0 y < `Double.MAX_VALUE/2` (para evitar problemas numéricos).
- En el servicio (`TransactionService`): Al crear un `EXPENSE`, actualiza el presupuesto correspondiente restando el monto (usando `BudgetService.updateBudgetOnExpense`). Calcula totales de ingresos y gastos para reportes.

**Uso en la aplicación**:
- Core de la aplicación. Permite crear, listar y eliminar transacciones.
- Genera reportes como el total de ingresos vs. gastos.

---

### 3. Budget (Archivo: `Budget.java`)

**Propósito**: Representa un presupuesto para una categoría específica por usuario. Define límites de gasto en un período (e.g., $500 en "Comida" de enero a febrero). El monto restante se actualiza automáticamente con los gastos.

**Campos clave**:
- `id`: ID único autogenerado.
- `userId`: ID del usuario dueño (no nulo).
- `category`: Relación con una categoría (`@ManyToOne`, lazy fetch, no nulo).
- `amount`: Monto restante actual (double, no nulo).
- `initialAmount`: Monto inicial del presupuesto (double, no nulo).
- `startDate`: Fecha de inicio (`LocalDate`, opcional).
- `endDate`: Fecha de fin (`LocalDate`, opcional).

**Relaciones**:
- Muchos presupuestos por categoría (`ManyToOne` con `Category`).

**Validaciones/Comportamiento**:
- `@PrePersist` y `@PreUpdate`: Valida que `amount` e `initialAmount` sean no nulos, >=0 y < `Double.MAX_VALUE/2`.
- En el servicio (`BudgetService`): Crea o actualiza presupuestos (si existe para user+category, actualiza; sino crea). Lista por usuario. Actualiza `amount` restando gastos (y no permite negativos, setea a 0).

**Uso en la aplicación**:
- Limita y trackea gastos por categoría.
- Integrado con transacciones: cada `EXPENSE` reduce el presupuesto correspondiente.

---

## Notas Generales
- **Base de datos**: Las entidades mapean a las tablas `categories`, `transactions` y `budgets`.
- **Relaciones**: `Category` es "padre" de `Transaction` y `Budget` (vía `@ManyToOne`).
- **Seguridad**: Todas las operaciones requieren autenticación vía JWT (`userId` extraído del token).
- **Tecnologías**: Spring Boot, Spring Data JPA, Spring Security (JWT), Lombok.

## Instalación
1. Clona el repositorio: `git clone <URL-del-repositorio>`
2. Configura la base de datos en `application.properties` (e.g., H2, PostgreSQL).
3. Ejecuta: `mvn spring-boot:run`

## Endpoints
- `/api/categories`: GET (listar), POST (crear categoría personalizada).
- `/api/budgets`: GET (listar), POST (crear/actualizar presupuesto).
- `/api/transactions`: GET (listar), POST (crear), DELETE (eliminar), GET `/reports` (reportes).

Para más detalles, consulta la documentación de los controladores (`BudgetController`, `CategoryController`, `TransactionController`).
