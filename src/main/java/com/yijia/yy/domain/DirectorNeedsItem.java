package com.yijia.yy.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DirectorNeedsItem.
 */
@Entity
@Table(name = "director_needs_item")
public class DirectorNeedsItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "memo")
    private String memo;

    @ManyToOne
    @JsonBackReference
    private DirectorNeeds directorNeeds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public DirectorNeedsItem name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAmount() {
        return amount;
    }

    public DirectorNeedsItem amount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getMemo() {
        return memo;
    }

    public DirectorNeedsItem memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public DirectorNeeds getDirectorNeeds() {
        return directorNeeds;
    }

    public DirectorNeedsItem directorNeeds(DirectorNeeds directorNeeds) {
        this.directorNeeds = directorNeeds;
        return this;
    }

    public void setDirectorNeeds(DirectorNeeds directorNeeds) {
        this.directorNeeds = directorNeeds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DirectorNeedsItem directorNeedsItem = (DirectorNeedsItem) o;
        if(directorNeedsItem.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, directorNeedsItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DirectorNeedsItem{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", amount='" + amount + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
