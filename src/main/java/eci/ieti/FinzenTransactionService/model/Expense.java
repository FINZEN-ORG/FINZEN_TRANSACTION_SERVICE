package eci.ieti.FinzenTransactionService.model;

import jakarta.persistence.*;

@Entity
@Table(name = "expenses")
public class Expense extends Transaction {
}