package com.yijia.yy.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the Asset entity.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AssetDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Integer amount;

    private Boolean needReturn;

    private Long lastModifiedTime;

    private Long createTime;

    private Long ownerId;

    private String ownerName;

    private String brand;

    private String sncode;

    private String vendor;

    private Double price;

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

    public void setName(String name) {
        this.name = name;
    }
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
    public Boolean getNeedReturn() {
        return needReturn;
    }

    public void setNeedReturn(Boolean needReturn) {
        this.needReturn = needReturn;
    }
    public Long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
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

        AssetDTO assetDTO = (AssetDTO) o;

        if ( ! Objects.equals(id, assetDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AssetDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", amount='" + amount + "'" +
            ", needReturn='" + needReturn + "'" +
            ", lastModifiedTime='" + lastModifiedTime + "'" +
            '}';
    }
}
