package com.yijia.yy.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A PerformanceBonus.
 */
@Entity
@Table(name = "performance_bonus")
public class PerformanceBonus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Float amount;

    @NotNull
    @Column(name = "create_time", nullable = false)
    private Long createTime;

    @Column(name = "last_modified_time")
    private Long lastModifiedTime;

    @ManyToOne
    private Employee creator;

    @ManyToOne
    private Employee lastModifier;

    @ManyToOne
    private Project project;

    @ManyToOne
    private Employee owner;

    @Column
    private Float workProportion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getAmount() {
        return amount;
    }

    public PerformanceBonus amount(Float amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public PerformanceBonus createTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public PerformanceBonus lastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
        return this;
    }

    public void setLastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public Employee getCreator() {
        return creator;
    }

    public PerformanceBonus creator(Employee employee) {
        this.creator = employee;
        return this;
    }

    public void setCreator(Employee employee) {
        this.creator = employee;
    }

    public Employee getLastModifier() {
        return lastModifier;
    }

    public PerformanceBonus lastModifier(Employee employee) {
        this.lastModifier = employee;
        return this;
    }

    public void setLastModifier(Employee employee) {
        this.lastModifier = employee;
    }

    public Project getProject() {
        return project;
    }

    public PerformanceBonus project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Employee getOwner() {
        return owner;
    }

    public PerformanceBonus owner(Employee employee) {
        this.owner = employee;
        return this;
    }

    public void setOwner(Employee employee) {
        this.owner = employee;
    }

    public Float getWorkProportion() {
        return workProportion;
    }

    public void setWorkProportion(Float workProportion) {
        this.workProportion = workProportion;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PerformanceBonus performanceBonus = (PerformanceBonus) o;
        if(performanceBonus.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, performanceBonus.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PerformanceBonus{" +
            "id=" + id +
            ", amount='" + amount + "'" +
            ", createTime='" + createTime + "'" +
            ", lastModifiedTime='" + lastModifiedTime + "'" +
            '}';
    }
}
