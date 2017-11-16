package com.yijia.yy.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Asset.
 */
@Entity
@Table(name = "asset")
public class Asset implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "need_return")
    private Boolean needReturn;

    @Column(name = "last_modified_time")
    private Long lastModifiedTime;

    @Column
    private Long createTime;

    @ManyToOne
    private Employee owner;

    @Column
    private String brand;

    @Column
    private String sncode;

    @Column
    private String vendor;

    @Column
    private Double price;

    @Column(length = 500)
    private String memo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Asset name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAmount() {
        return amount;
    }

    public Asset amount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Boolean isNeedReturn() {
        return needReturn;
    }

    public Asset needReturn(Boolean needReturn) {
        this.needReturn = needReturn;
        return this;
    }

    public void setNeedReturn(Boolean needReturn) {
        this.needReturn = needReturn;
    }

    public Long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public Asset lastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
        return this;
    }

    public void setLastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public Boolean getNeedReturn() {
        return needReturn;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Employee getOwner() {
        return owner;
    }

    public void setOwner(Employee owner) {
        this.owner = owner;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSncode() {
        return sncode;
    }

    public void setSncode(String sncode) {
        this.sncode = sncode;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Asset asset = (Asset) o;
        if(asset.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, asset.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Asset{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", amount='" + amount + "'" +
            ", needReturn='" + needReturn + "'" +
            ", lastModifiedTime='" + lastModifiedTime + "'" +
            '}';
    }
}
