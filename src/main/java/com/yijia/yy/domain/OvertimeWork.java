package com.yijia.yy.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A OvertimeWork.
 */
@Entity
public class OvertimeWork extends ApprovalItem {

    private static final long serialVersionUID = 1853845378496846469L;

    @Column(name = "hours")
    private Integer hours;

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }
}
