package com.yijia.yy.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A DTO for the Equipment entity.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EquipmentDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String vendor;

    private Double price;

    private Long createTime;

    private String inputOperator;

    private Long lastModifiedTime;

    private String lastModifier;


    private Long categoryId;

    private String info;

    private String categoryName;

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
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
    public String getInputOperator() {
        return inputOperator;
    }

    public void setInputOperator(String inputOperator) {
        this.inputOperator = inputOperator;
    }
    public Long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }
    public String getLastModifier() {
        return lastModifier;
    }

    public void setLastModifier(String lastModifier) {
        this.lastModifier = lastModifier;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long dictionaryId) {
        this.categoryId = dictionaryId;
    }


    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String dictionaryName) {
        this.categoryName = dictionaryName;
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

        EquipmentDTO equipmentDTO = (EquipmentDTO) o;

        if ( ! Objects.equals(id, equipmentDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EquipmentDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", vendor='" + vendor + "'" +
            ", price='" + price + "'" +
            ", createTime='" + createTime + "'" +
            ", inputOperator='" + inputOperator + "'" +
            ", lastModifiedTime='" + lastModifiedTime + "'" +
            ", lastModifier='" + lastModifier + "'" +
            '}';
    }
}
