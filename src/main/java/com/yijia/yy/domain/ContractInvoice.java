package com.yijia.yy.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ContractInvoice.
 */
@Entity
@Table(name = "contract_invoice")
public class ContractInvoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Double amount;

    @NotNull
    @Column(name = "create_time", nullable = false)
    private Long createTime;

    @ManyToOne
    private Contract contract;

    @ManyToOne
    private Employee creator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public ContractInvoice amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public ContractInvoice createTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Contract getContract() {
        return contract;
    }

    public ContractInvoice contract(Contract contract) {
        this.contract = contract;
        return this;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Employee getCreator() {
        return creator;
    }

    public ContractInvoice creator(Employee employee) {
        this.creator = employee;
        return this;
    }

    public void setCreator(Employee employee) {
        this.creator = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContractInvoice contractInvoice = (ContractInvoice) o;
        if(contractInvoice.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, contractInvoice.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ContractInvoice{" +
            "id=" + id +
            ", amount='" + amount + "'" +
            ", createTime='" + createTime + "'" +
            '}';
    }
}
