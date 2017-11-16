package com.yijia.yy.domain;


import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A DirectorNeeds.
 */
@Entity
@Table(name = "director_needs")
public class DirectorNeeds implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private Long createTime;

    @ManyToOne
    private Employee creator;

    @ManyToOne
    private Project project;

    @OneToMany(mappedBy = "directorNeeds", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<DirectorNeedsItem> items;

    @OneToMany(mappedBy = "directorNeeds", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<DirectorNeedsComment> comments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public DirectorNeeds project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Employee getCreator() {
        return creator;
    }

    public void setCreator(Employee creator) {
        this.creator = creator;
    }

    public List<DirectorNeedsComment> getComments() {
        return comments;
    }

    public void setComments(List<DirectorNeedsComment> comments) {
        this.comments = comments;
    }

    public List<DirectorNeedsItem> getItems() {
        return items;
    }

    public void setItems(List<DirectorNeedsItem> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DirectorNeeds directorNeeds = (DirectorNeeds) o;
        if(directorNeeds.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, directorNeeds.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DirectorNeeds{" +
            "id=" + id +
            '}';
    }
}
