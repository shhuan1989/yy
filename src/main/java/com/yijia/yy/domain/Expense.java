package com.yijia.yy.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A Expense.
 */
@Entity
public class Expense  extends ApprovalItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "pay_time")
    private Long payTime;

    @ManyToOne
    private Dictionary payMethod;

    @Transient
    private Double total;

    @ManyToOne
    private Project project;

    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ExpenseItem> items = new ArrayList<>();

    public Long getPayTime() {
        return payTime;
    }

    public Expense payTime(Long payTime) {
        this.payTime = payTime;
        return this;
    }

    public void setPayTime(Long payTime) {
        this.payTime = payTime;
    }

    public Dictionary getPayMethod() {
        return payMethod;
    }

    public Expense payMethod(Dictionary dictionary) {
        this.payMethod = dictionary;
        return this;
    }

    public void setPayMethod(Dictionary dictionary) {
        this.payMethod = dictionary;
    }

    public Project getProject() {
        return project;
    }

    public Expense project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<ExpenseItem> getItems() {
        return items;
    }

    public Expense items(List<ExpenseItem> expenseItems) {
        this.items = expenseItems;
        return this;
    }

    public Expense addItems(ExpenseItem expenseItem) {
        items.add(expenseItem);
        expenseItem.setExpense(this);
        return this;
    }

    public Expense removeItems(ExpenseItem expenseItem) {
        items.remove(expenseItem);
        expenseItem.setExpense(null);
        return this;
    }

    public void setItems(List<ExpenseItem> expenseItems) {
        this.items = expenseItems;
    }

    public Double getTotal() {
        if (items != null) {
            return items.stream()
                .mapToDouble(i -> i.getAmount())
                .sum();
        }
        return 0.0;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Expense expense = (Expense) o;
        if(expense.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), expense.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Expense{" +
            "id=" + getId() +
            ", payTime='" + payTime + "'" +
            '}';
    }
}
