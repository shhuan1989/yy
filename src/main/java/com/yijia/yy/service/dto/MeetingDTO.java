package com.yijia.yy.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;


/**
 * A DTO for the Meeting entity.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MeetingDTO implements Serializable {

    private Long id;

    private String name;

    @NotNull
    private Long startTime;

    @NotNull
    private Long endTime;

    private String info;

    private EmployeeDTO creator;

    private DictionaryDTO room;

    private Long projectId;

    private DictionaryDTO status;

    private Set<Long> memberIds;

    private Set<String> memberNames;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public EmployeeDTO getCreator() {
        return creator;
    }

    public void setCreator(EmployeeDTO creator) {
        this.creator = creator;
    }

    public DictionaryDTO getRoom() {
        return room;
    }

    public void setRoom(DictionaryDTO room) {
        this.room = room;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public DictionaryDTO getStatus() {
        return status;
    }

    public void setStatus(DictionaryDTO status) {
        this.status = status;
    }

    public Set<Long> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(Set<Long> memberIds) {
        this.memberIds = memberIds;
    }

    public Set<String> getMemberNames() {
        return memberNames;
    }

    public void setMemberNames(Set<String> memberNames) {
        this.memberNames = memberNames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MeetingDTO meetingDTO = (MeetingDTO) o;

        if (!Objects.equals(id, meetingDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MeetingDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", startTime='" + startTime + "'" +
            ", endTime='" + endTime + "'" +
            ", info='" + info + "'" +
            '}';
    }
}
