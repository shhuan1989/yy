package com.yijia.yy.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Equipment.
 */
@Entity
@Table(name = "equipment")
public class Equipment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "vendor")
    private String vendor;

    @Column(name = "price")
    private Double price;

    @Column(name = "create_time")
    private Long createTime;

    @Column(name = "input_operator")
    private String inputOperator;

    @Column(name = "last_modified_time")
    private Long lastModifiedTime;

    @Column(name = "last_modifier")
    private String lastModifier;

    @ManyToOne(cascade = CascadeType.REFRESH)
    private Dictionary category;

    @Column
    private String info;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Equipment name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVendor() {
        return vendor;
    }

    public Equipment vendor(String vendor) {
        this.vendor = vendor;
        return this;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public Double getPrice() {
        return price;
    }

    public Equipment price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public Equipment createTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getInputOperator() {
        return inputOperator;
    }

    public Equipment inputOperator(String inputOperator) {
        this.inputOperator = inputOperator;
        return this;
    }

    public void setInputOperator(String inputOperator) {
        this.inputOperator = inputOperator;
    }

    public Long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public Equipment lastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
        return this;
    }

    public void setLastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getLastModifier() {
        return lastModifier;
    }

    public Equipment lastModifier(String lastModifier) {
        this.lastModifier = lastModifier;
        return this;
    }

    public void setLastModifier(String lastModifier) {
        this.lastModifier = lastModifier;
    }

    public Dictionary getCategory() {
        return category;
    }

    public Equipment category(Dictionary dictionary) {
        this.category = dictionary;
        return this;
    }

    public void setCategory(Dictionary dictionary) {
        this.category = dictionary;
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
        Equipment equipment = (Equipment) o;
        if(equipment.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, equipment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Equipment{" +
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
