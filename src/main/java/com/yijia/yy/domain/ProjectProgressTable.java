package com.yijia.yy.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A ProjectProgressTable.
 */
@Entity
@Table(name = "project_progress_table")
public class ProjectProgressTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "create_time")
    private Long createTime;

    @Column(name = "memo")
    private String memo;

    @OneToMany(mappedBy = "projectProgressTable", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonManagedReference
    private List<ProjectProgressItem> items = new ArrayList<>();

    @OneToOne
    private Project project;

    @ManyToOne
    private Employee creator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public ProjectProgressTable createTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getMemo() {
        return memo;
    }

    public ProjectProgressTable memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public List<ProjectProgressItem> getItems() {
        return items;
    }

    public ProjectProgressTable items(List<ProjectProgressItem> projectProgressItems) {
        this.items = projectProgressItems;
        return this;
    }

    public ProjectProgressTable addItems(ProjectProgressItem projectProgressItem) {
        items.add(projectProgressItem);
        projectProgressItem.setProjectProgressTable(this);
        return this;
    }

    public ProjectProgressTable removeItems(ProjectProgressItem projectProgressItem) {
        items.remove(projectProgressItem);
        projectProgressItem.setProjectProgressTable(null);
        return this;
    }

    public void setItems(List<ProjectProgressItem> projectProgressItems) {
        this.items = projectProgressItems;
    }

    public Employee getCreator() {
        return creator;
    }

    public ProjectProgressTable creator(Employee employee) {
        this.creator = employee;
        return this;
    }

    public void setCreator(Employee employee) {
        this.creator = employee;
    }

    public Project getProject() {
        return project;
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
        ProjectProgressTable projectProgressTable = (ProjectProgressTable) o;
        if(projectProgressTable.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, projectProgressTable.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProjectProgressTable{" +
            "id=" + id +
            ", createTime='" + createTime + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
