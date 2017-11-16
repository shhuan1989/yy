package com.yijia.yy.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A DTO for the Staff entity.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class StaffDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String tel;

    private Double price;

    private String inputOperator;

    private Long createTime;

    private String lastModifier;

    private Long lastModifiedTime;

    private Long typeId;

    private String typeName;

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
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
    public String getInputOperator() {
        return inputOperator;
    }

    public void setInputOperator(String inputOperator) {
        this.inputOperator = inputOperator;
    }
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
    public String getLastModifier() {
        return lastModifier;
    }

    public void setLastModifier(String lastModifier) {
        this.lastModifier = lastModifier;
    }
    public Long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long dictionaryId) {
        this.typeId = dictionaryId;
    }


    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String dictionaryName) {
        this.typeName = dictionaryName;
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

        StaffDTO staffDTO = (StaffDTO) o;

        if ( ! Objects.equals(id, staffDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StaffDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", tel='" + tel + "'" +
            ", price='" + price + "'" +
            ", inputOperator='" + inputOperator + "'" +
            ", createTime='" + createTime + "'" +
            ", lastModifier='" + lastModifier + "'" +
            ", lastModifiedTime='" + lastModifiedTime + "'" +
            '}';
    }
}
