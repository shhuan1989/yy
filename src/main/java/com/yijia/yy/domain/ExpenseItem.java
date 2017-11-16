package com.yijia.yy.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ExpenseItem.
 */
@Entity
@Table(name = "expense_item")
public class ExpenseItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "purpose")
    private String purpose;

    @Column(name = "amount")
    private Double amount;

    @ManyToOne
    @JsonBackReference
    private Expense expense;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPurpose() {
        return purpose;
    }

    public ExpenseItem purpose(String purpose) {
        this.purpose = purpose;
        return this;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public Double getAmount() {
        return amount;
    }

    public ExpenseItem amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Expense getExpense() {
        return expense;
    }

    public ExpenseItem expense(Expense expense) {
        this.expense = expense;
        return this;
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExpenseItem expenseItem = (ExpenseItem) o;
        if(expenseItem.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, expenseItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ExpenseItem{" +
            "id=" + id +
            ", purpose='" + purpose + "'" +
            ", amount='" + amount + "'" +
            '}';
    }
}
