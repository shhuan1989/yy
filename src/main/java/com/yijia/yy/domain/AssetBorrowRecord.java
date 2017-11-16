package com.yijia.yy.domain;


import com.yijia.yy.domain.converter.AssetReturnStatusConverter;
import com.yijia.yy.domain.enumeration.AssetReturnStatus;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A AssetBorrowRecord.
 */
@Entity
public class AssetBorrowRecord extends ApprovalItem implements Serializable {

    private static final long serialVersionUID = -6153695273440576381L;

    @NotNull
    @Column(name = "amount", nullable = true)
    private Integer amount;

    @ManyToOne
    private Asset asset;

    @ManyToOne
    private Employee returner;

    @Column
    @Convert(converter = AssetReturnStatusConverter.class)
    private AssetReturnStatus returnStatus;

    public Integer getAmount() {
        return amount;
    }

    public AssetBorrowRecord amount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Asset getAsset() {
        return asset;
    }

    public AssetBorrowRecord asset(Asset asset) {
        this.asset = asset;
        return this;
    }

    public AssetReturnStatus getReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(AssetReturnStatus returnStatus) {
        this.returnStatus = returnStatus;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public Employee getReturner() {
        return returner;
    }

    public void setReturner(Employee returner) {
        this.returner = returner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        AssetBorrowRecord that = (AssetBorrowRecord) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;
        if (asset != null ? !asset.equals(that.asset) : that.asset != null) return false;
        return returnStatus == that.returnStatus;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getId() != null ? getId().hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (asset != null ? asset.hashCode() : 0);
        result = 31 * result + (returnStatus != null ? returnStatus.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AssetBorrowRecord{" +
            "id=" + getId() +
            ", amount='" + amount + "'" +
            '}';
    }
}
