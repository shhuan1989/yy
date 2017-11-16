package com.yijia.yy.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "contract_installment")
public class ContractInstallment implements Serializable, Comparable{

    private static final long serialVersionUID = 5891512076854446714L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private Integer orderNumber;

    @Column
    private Double amount;

    @Column
    private Double actualAmount;

    @Column(nullable = false)
    private Long eta;

    @Column
    private Long actualPayTime;

    @ManyToOne
    private Dictionary payMethod;

    @ManyToOne
    @JsonBackReference
    private Contract contract;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(Double actualAmount) {
        this.actualAmount = actualAmount;
    }

    public Long getEta() {
        return eta;
    }

    public void setEta(Long eta) {
        this.eta = eta;
    }

    public Long getActualPayTime() {
        return actualPayTime;
    }

    public void setActualPayTime(Long actualPayTime) {
        this.actualPayTime = actualPayTime;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Dictionary getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(Dictionary payMethod) {
        this.payMethod = payMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContractInstallment that = (ContractInstallment) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (orderNumber != null ? !orderNumber.equals(that.orderNumber) : that.orderNumber != null) return false;
        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;
        if (actualAmount != null ? !actualAmount.equals(that.actualAmount) : that.actualAmount != null) return false;
        if (eta != null ? !eta.equals(that.eta) : that.eta != null) return false;
        if (actualPayTime != null ? !actualPayTime.equals(that.actualPayTime) : that.actualPayTime != null)
            return false;
        return contract != null ? contract.equals(that.contract) : that.contract == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (orderNumber != null ? orderNumber.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (actualAmount != null ? actualAmount.hashCode() : 0);
        result = 31 * result + (eta != null ? eta.hashCode() : 0);
        result = 31 * result + (actualPayTime != null ? actualPayTime.hashCode() : 0);
        result = 31 * result + (contract != null ? contract.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ContractInstallment{" +
            "id=" + id +
            ", amount=" + amount +
            ", eta=" + eta +
            ", actualPayTime=" + actualPayTime +
            ", contract=" + contract +
            '}';
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof ContractInstallment) {
            ContractInstallment other = (ContractInstallment)o;
            if (other.getEta() != null && eta != null) {
                return eta.compareTo(other.getEta());
            } else if (eta != null) {
                return -1;
            }
            return 1;
        }

        return -1;
    }
}
