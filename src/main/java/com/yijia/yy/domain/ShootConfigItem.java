package com.yijia.yy.domain;


import com.yijia.yy.domain.converter.BooleanEnumConverter;
import com.yijia.yy.domain.enumeration.BooleanEnum;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ShootConfigItem.
 */
@Entity
@Table(name = "shoot_config")
public class ShootConfigItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "cat_1")
    private String cat1;

    @Column(name = "cat_2")
    private String cat2;

    @Column(name = "name")
    private String name;

    @Column(name = "amount")
    private Integer amount;

    @Column
    private Integer actualAmount;

    @Column(name = "days")
    private Integer days;

    @Column
    private Integer actualDays;

    @Column(name = "unit_price")
    private Double unitPrice;

    @Column(name = "cost")
    private Double cost;

    @Column
    private Double actualCost;

    @Column(name = "default_unit_price")
    private Double defaultUnitPrice;

    @Column
    private String vendor;

    @Column
    private Boolean paymentStatus;

    @ManyToOne
    private ShootConfig shootConfig;

    @Column
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

    public ShootConfigItem cat1(String cat1) {
        this.cat1 = cat1;
        return this;
    }

    public void setCat1(String cat1) {
        this.cat1 = cat1;
    }

    public String getCat2() {
        return cat2;
    }

    public ShootConfigItem cat2(String cat2) {
        this.cat2 = cat2;
        return this;
    }

    public void setCat2(String cat2) {
        this.cat2 = cat2;
    }

    public String getName() {
        return name;
    }

    public ShootConfigItem name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAmount() {
        return amount;
    }

    public ShootConfigItem amount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getDays() {
        return days;
    }

    public ShootConfigItem days(Integer days) {
        this.days = days;
        return this;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public ShootConfigItem unitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public ShootConfig getShootConfig() {
        return shootConfig;
    }

    public void setShootConfig(ShootConfig shootConfig) {
        this.shootConfig = shootConfig;
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
        ShootConfigItem shootConfigItem = (ShootConfigItem) o;
        if(shootConfigItem.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, shootConfigItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ShootConfigItem{" +
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
