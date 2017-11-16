package com.yijia.yy.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ShootAgenda.
 */
@Entity
@Table(name = "shoot_agenda")
public class ShootAgenda implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private Long startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private Long endTime;

    @Column(name = "background_color")
    private String backgroundColor;

    @Column(name = "border_color")
    private String borderColor;

    @Column(name = "all_day")
    private Boolean allDay;

    @Column(name = "url")
    private String url;

    @Column(name = "location")
    private String location;

    @Column
    private String tooltip;

    @ManyToOne
    private Employee creator;

    @ManyToOne
    private Project project;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public ShootAgenda title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getStartTime() {
        return startTime;
    }

    public ShootAgenda startTime(Long startTime) {
        this.startTime = startTime;
        return this;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public ShootAgenda endTime(Long endTime) {
        this.endTime = endTime;
        return this;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public ShootAgenda backgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Boolean getAllDay() {
        return allDay;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public ShootAgenda borderColor(String borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public Boolean isAllDay() {
        return allDay;
    }

    public ShootAgenda allDay(Boolean allDay) {
        this.allDay = allDay;
        return this;
    }

    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
    }

    public String getUrl() {
        return url;
    }

    public ShootAgenda url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocation() {
        return location;
    }

    public ShootAgenda location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Employee getCreator() {
        return creator;
    }

    public ShootAgenda creator(Employee employee) {
        this.creator = employee;
        return this;
    }

    public void setCreator(Employee employee) {
        this.creator = employee;
    }

    public Project getProject() {
        return project;
    }

    public ShootAgenda project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ShootAgenda shootAgenda = (ShootAgenda) o;
        if(shootAgenda.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, shootAgenda.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ShootAgenda{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", startTime='" + startTime + "'" +
            ", endTime='" + endTime + "'" +
            ", backgroundColor='" + backgroundColor + "'" +
            ", borderColor='" + borderColor + "'" +
            ", allDay='" + allDay + "'" +
            ", url='" + url + "'" +
            ", location='" + location + "'" +
            '}';
    }
}
