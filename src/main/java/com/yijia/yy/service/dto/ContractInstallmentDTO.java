package com.yijia.yy.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ContractInstallmentDTO {
    private Long id;

    private Double amount;

    private Double actualAmount;

    @NotNull
    private Long eta;

    private Long actualPayTime;

    private Long contractId;

    private Integer orderNumber;

    private DictionaryDTO payMethod;

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

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public DictionaryDTO getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(DictionaryDTO payMethod) {
        this.payMethod = payMethod;
    }
}
