package com.yijia.yy.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ProjectProgressItem.
 */
@Entity
@Table(name = "project_progress_item")
public class ProjectProgressItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "eta")
    private Long eta;

    @Column(name = "finish_time")
    private Long finishTime;

    @Column
    private Integer ord;

    @ManyToOne
    private Employee owner;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonBackReference
    private ProjectProgressTable projectProgressTable;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEta() {
        return eta;
    }

    public ProjectProgressItem eta(Long eta) {
        this.eta = eta;
        return this;
    }

    public void setEta(Long eta) {
        this.eta = eta;
    }

    public Long getFinishTime() {
        return finishTime;
    }

    public ProjectProgressItem finishTime(Long finishTime) {
        this.finishTime = finishTime;
        return this;
    }

    public void setFinishTime(Long finishTime) {
        this.finishTime = finishTime;
    }

    public Employee getOwner() {
        return owner;
    }

    public ProjectProgressItem owner(Employee employee) {
        this.owner = employee;
        return this;
    }

    public void setOwner(Employee employee) {
        this.owner = employee;
    }

    public ProjectProgressTable getProjectProgressTable() {
        return projectProgressTable;
    }

    public ProjectProgressItem projectProgressTable(ProjectProgressTable projectProgressTable) {
        this.projectProgressTable = projectProgressTable;
        return this;
    }

    public void setProjectProgressTable(ProjectProgressTable projectProgressTable) {
        this.projectProgressTable = projectProgressTable;
    }

    public Integer getOrd() {
        return ord;
    }

    public void setOrd(Integer ord) {
        this.ord = ord;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProjectProgressItem projectProgressItem = (ProjectProgressItem) o;
        if(projectProgressItem.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, projectProgressItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProjectProgressItem{" +
            "id=" + id +
            ", eta='" + eta + "'" +
            ", finishTime='" + finishTime + "'" +
            '}';
    }
}
