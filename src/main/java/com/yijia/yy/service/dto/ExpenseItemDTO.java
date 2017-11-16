package com.yijia.yy.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the ExpenseItem entity.
 */
public class ExpenseItemDTO implements Serializable {

    private Long id;

    private String purpose;

    private Double amount;


    private Long expenseId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Long expenseId) {
        this.expenseId = expenseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ExpenseItemDTO expenseItemDTO = (ExpenseItemDTO) o;

        if ( ! Objects.equals(id, expenseItemDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ExpenseItemDTO{" +
            "id=" + id +
            ", purpose='" + purpose + "'" +
            ", amount='" + amount + "'" +
            '}';
    }
}
