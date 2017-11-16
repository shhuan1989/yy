package com.yijia.yy.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ProjectCost.
 */
@Entity
@Table(name = "project_cost")
public class ProjectCost implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "create_time")
    private Long createTime;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "info")
    private String info;

    @ManyToOne
    private Employee creator;

    @ManyToOne
    private Project project;

    @ManyToOne
    private Dictionary category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public ProjectCost createTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Double getAmount() {
        return amount;
    }

    public ProjectCost amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getInfo() {
        return info;
    }

    public ProjectCost info(String info) {
        this.info = info;
        return this;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Employee getCreator() {
        return creator;
    }

    public ProjectCost creator(Employee employee) {
        this.creator = employee;
        return this;
    }

    public void setCreator(Employee employee) {
        this.creator = employee;
    }

    public Project getProject() {
        return project;
    }

    public ProjectCost project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Dictionary getCategory() {
        return category;
    }

    public void setCategory(Dictionary category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProjectCost projectCost = (ProjectCost) o;
        if(projectCost.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, projectCost.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProjectCost{" +
            "id=" + id +
            ", createTime='" + createTime + "'" +
            ", amount='" + amount + "'" +
            ", info='" + info + "'" +
            '}';
    }
}
