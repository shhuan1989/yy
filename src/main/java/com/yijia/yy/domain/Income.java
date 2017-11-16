package com.yijia.yy.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Income.
 */
@Entity
public class Income extends ApprovalItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "income_time")
    private Long incomeTime;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "income_desc")
    private String incomeDesc;

    @Column(name = "memo")
    private String memo;

    @ManyToOne
    private Dictionary incomeMethod;

    public Long getIncomeTime() {
        return incomeTime;
    }

    public Income incomeTime(Long incomeTime) {
        this.incomeTime = incomeTime;
        return this;
    }

    public void setIncomeTime(Long incomeTime) {
        this.incomeTime = incomeTime;
    }

    public Double getAmount() {
        return amount;
    }

    public Income amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getIncomeDesc() {
        return incomeDesc;
    }

    public Income incomeDesc(String incomeDesc) {
        this.incomeDesc = incomeDesc;
        return this;
    }

    public void setIncomeDesc(String incomeDesc) {
        this.incomeDesc = incomeDesc;
    }

    public String getMemo() {
        return memo;
    }

    public Income memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Dictionary getIncomeMethod() {
        return incomeMethod;
    }

    public Income incomeMethod(Dictionary dictionary) {
        this.incomeMethod = dictionary;
        return this;
    }

    public void setIncomeMethod(Dictionary dictionary) {
        this.incomeMethod = dictionary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Income income = (Income) o;
        if(income.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), income.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Income{" +
            "id=" + getId() +
            ", incomeTime='" + incomeTime + "'" +
            ", amount='" + amount + "'" +
            ", incomeDesc='" + incomeDesc + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
