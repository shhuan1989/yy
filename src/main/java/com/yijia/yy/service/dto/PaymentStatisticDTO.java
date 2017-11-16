package com.yijia.yy.service.dto;

public class PaymentStatisticDTO {
    private Double paidAmount;
    private Double amount;

    public Double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public PaymentStatisticDTO(Double paidAmount, Double amount) {
        this.paidAmount = paidAmount;
        this.amount = amount;
    }
}
