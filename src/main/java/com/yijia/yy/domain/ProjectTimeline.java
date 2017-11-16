package com.yijia.yy.domain;


import com.yijia.yy.domain.converter.ProjectTimelineEventTypeConverter;
import com.yijia.yy.domain.enumeration.ProjectTimelineEventType;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ProjectTimeline.
 */
@Entity
@Table(name = "project_timeline")
public class ProjectTimeline implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "create_time", nullable = false)
    private Long createTime;

    @Column(name = "name")
    private String name;

    @Column(name = "info", length = 4000)
    private String info;

    @ManyToOne
    private Employee creator;

    @ManyToOne
    @NotNull
    private Project project;

    @Column
    @Convert(converter = ProjectTimelineEventTypeConverter.class)
    private ProjectTimelineEventType type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public ProjectTimeline createTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public ProjectTimeline name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public ProjectTimeline info(String info) {
        this.info = info;
        return this;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Employee getCreator() {
        return creator;
    }

    public ProjectTimeline creator(Employee employee) {
        this.creator = employee;
        return this;
    }

    public void setCreator(Employee employee) {
        this.creator = employee;
    }

    public Project getProject() {
        return project;
    }

    public ProjectTimeline project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public ProjectTimelineEventType getType() {
        return type;
    }

    public void setType(ProjectTimelineEventType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProjectTimeline projectTimeline = (ProjectTimeline) o;
        if(projectTimeline.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, projectTimeline.id);
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProjectTimeline{" +
            "id=" + id +
            ", createTime='" + createTime + "'" +
            ", name='" + name + "'" +
            ", info='" + info + "'" +
            '}';
    }
}
