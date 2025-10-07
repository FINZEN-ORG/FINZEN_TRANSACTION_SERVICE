// Income.java
package eci.ieti.FinzenTransactionService.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "incomes")
@DiscriminatorValue("INCOME")

public class Income extends Transaction {
}