package com.yijia.yy.service.dto;

public class InvoiceStatisticsDTO {
    private Double totalAmount;
    private Double invoiceAmount;

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(Double invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public InvoiceStatisticsDTO(Double totalAmount, Double invoiceAmount) {
        this.totalAmount = totalAmount;
        this.invoiceAmount = invoiceAmount;
    }
}
