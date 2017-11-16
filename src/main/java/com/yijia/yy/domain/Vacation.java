package com.yijia.yy.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Vacation.
 */
@Entity
public class Vacation extends ApprovalItem {

    private static final long serialVersionUID = 1120058887479345405L;

    @NotNull
    @Column(name = "days")
    private Float days;

    @ManyToOne(cascade = CascadeType.REFRESH)
    private Dictionary type;

    public Float getDays() {
        return days;
    }

    public void setDays(Float days) {
        this.days = days;
    }

    public Dictionary getType() {
        return type;
    }

    public void setType(Dictionary type) {
        this.type = type;
    }
}
