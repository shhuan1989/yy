package com.yijia.yy.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Staff.
 */
@Entity
@Table(name = "staff")
public class Staff implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "tel")
    private String tel;

    @Column(name = "price")
    private Double price;

    @Column(name = "input_operator")
    private String inputOperator;

    @Column(name = "create_time")
    private Long createTime;

    @Column(name = "last_modifier")
    private String lastModifier;

    @Column(name = "last_modified_time")
    private Long lastModifiedTime;

    @Column(length = 500)
    private String memo;

    @ManyToOne
    private Dictionary type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Staff name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public Staff tel(String tel) {
        this.tel = tel;
        return this;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Double getPrice() {
        return price;
    }

    public Staff price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getInputOperator() {
        return inputOperator;
    }

    public Staff inputOperator(String inputOperator) {
        this.inputOperator = inputOperator;
        return this;
    }

    public void setInputOperator(String inputOperator) {
        this.inputOperator = inputOperator;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public Staff createTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getLastModifier() {
        return lastModifier;
    }

    public Staff lastModifier(String lastModifier) {
        this.lastModifier = lastModifier;
        return this;
    }

    public void setLastModifier(String lastModifier) {
        this.lastModifier = lastModifier;
    }

    public Long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public Staff lastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
        return this;
    }

    public void setLastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public Dictionary getType() {
        return type;
    }

    public Staff type(Dictionary dictionary) {
        this.type = dictionary;
        return this;
    }

    public void setType(Dictionary dictionary) {
        this.type = dictionary;
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
        Staff staff = (Staff) o;
        if(staff.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, staff.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Staff{" +
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
