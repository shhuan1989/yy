package com.yijia.yy.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ProjectPayment.
 */
@Entity
@Table(name = "project_payment")
public class ProjectPayment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "appointed_time", nullable = false)
    private Long appointedTime;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "amount", nullable = false)
    private Float amount;

    @Column(name = "pay_time")
    private Long payTime;

    @ManyToOne
    private Project project;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAppointedTime() {
        return appointedTime;
    }

    public ProjectPayment appointedTime(Long appointedTime) {
        this.appointedTime = appointedTime;
        return this;
    }

    public void setAppointedTime(Long appointedTime) {
        this.appointedTime = appointedTime;
    }

    public Float getAmount() {
        return amount;
    }

    public ProjectPayment amount(Float amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Long getPayTime() {
        return payTime;
    }

    public ProjectPayment payTime(Long payTime) {
        this.payTime = payTime;
        return this;
    }

    public void setPayTime(Long payTime) {
        this.payTime = payTime;
    }

    public Project getProject() {
        return project;
    }

    public ProjectPayment project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProjectPayment projectPayment = (ProjectPayment) o;
        if(projectPayment.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, projectPayment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProjectPayment{" +
            "id=" + id +
            ", appointedTime='" + appointedTime + "'" +
            ", amount='" + amount + "'" +
            ", payTime='" + payTime + "'" +
            '}';
    }
}
