package com.yijia.yy.service.dto;

import org.apache.xpath.operations.Bool;

import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the ShootConfigItem entity.
 */
public class ShootConfigItemDTO implements Serializable {

    private Long id;

    private String cat1;

    private String cat2;

    private String name;

    private Integer amount;

    private Integer actualAmount;

    private Integer days;

    private Integer actualDays;

    private Double unitPrice;

    private Double cost;

    private Double actualCost;

    private Double defaultUnitPrice;

    private String vendor;

    private Boolean paymentStatus;

    private String info;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getCat1() {
        return cat1;
    }

    public void setCat1(String cat1) {
        this.cat1 = cat1;
    }
    public String getCat2() {
        return cat2;
    }

    public void setCat2(String cat2) {
        this.cat2 = cat2;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }
    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Double getDefaultUnitPrice() {
        return defaultUnitPrice;
    }

    public void setDefaultUnitPrice(Double defaultUnitPrice) {
        this.defaultUnitPrice = defaultUnitPrice;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public Integer getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(Integer actualAmount) {
        this.actualAmount = actualAmount;
    }

    public Integer getActualDays() {
        return actualDays;
    }

    public void setActualDays(Integer actualDays) {
        this.actualDays = actualDays;
    }

    public Double getActualCost() {
        return actualCost;
    }

    public void setActualCost(Double actualCost) {
        this.actualCost = actualCost;
    }

    public Boolean getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ShootConfigItemDTO shootConfigItemDTO = (ShootConfigItemDTO) o;

        if ( ! Objects.equals(id, shootConfigItemDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ShootConfigItemDTO{" +
            "id=" + id +
            ", cat1='" + cat1 + "'" +
            ", cat2='" + cat2 + "'" +
            ", name='" + name + "'" +
            ", amount='" + amount + "'" +
            ", days='" + days + "'" +
            ", unitPrice='" + unitPrice + "'" +
            '}';
    }
}
