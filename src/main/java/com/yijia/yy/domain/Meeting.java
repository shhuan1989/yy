package com.yijia.yy.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yijia.yy.domain.converter.MeetingStatusConverter;
import com.yijia.yy.domain.enumeration.MeetingStatus;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Meeting.
 */
@Entity
@Table(name = "meeting")
public class Meeting implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private Long startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private Long endTime;

    @Column(name = "info")
    @Lob
    private byte[] info;

    @ManyToOne
    private Employee creator;

    @ManyToMany
    @JsonIgnore
    private Set<Employee> members = new HashSet<>();

    @ManyToOne
    private Dictionary room;

    @ManyToOne
    private Project project;

    @Convert(converter = MeetingStatusConverter.class)
    @Column(name = "status")
    private MeetingStatus status = MeetingStatus.NOT_START;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Meeting name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getStartTime() {
        return startTime;
    }

    public Meeting startTime(Long startTime) {
        this.startTime = startTime;
        return this;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public Meeting endTime(Long endTime) {
        this.endTime = endTime;
        return this;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public byte[] getInfo() {
        return info;
    }

    public Meeting info(byte[] info) {
        this.info = info;
        return this;
    }

    public void setInfo(byte[] info) {
        this.info = info;
    }

    public Employee getCreator() {
        return creator;
    }

    public Meeting creator(Employee employee) {
        this.creator = employee;
        return this;
    }

    public void setCreator(Employee employee) {
        this.creator = employee;
    }

    public Set<Employee> getMembers() {
        return members;
    }

    public Meeting members(Set<Employee> employees) {
        this.members = employees;
        return this;
    }

    public Meeting addMembers(Employee employee) {
        members.add(employee);
        return this;
    }

    public Meeting removeMembers(Employee employee) {
        members.remove(employee);
        return this;
    }

    public void setMembers(Set<Employee> employees) {
        this.members = employees;
    }

    public Dictionary getRoom() {
        return room;
    }

    public Meeting room(Dictionary room) {
        this.room = room;
        return this;
    }

    public void setRoom(Dictionary room) {
        this.room = room;
    }

    public Project getProject() {
        return project;
    }

    public Meeting project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public MeetingStatus getStatus() {
        return status;
    }

    public void setStatus(MeetingStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Meeting meeting = (Meeting) o;
        if (meeting.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, meeting.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Meeting{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", startTime='" + startTime + "'" +
            ", endTime='" + endTime + "'" +
            ", info='" + info + "'" +
            '}';
    }
}
