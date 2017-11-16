package com.yijia.yy.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yijia.yy.domain.JobTitle;

/**
 * A DTO for the JobTitle entity.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class JobTitleDTO implements Serializable {

    private Long id;

    private String name;

    private Set<Long> subordinateIds;

    private Integer level;

    private Long leaderId;

    private Boolean immutable = true;

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

    public Long getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Long leaderId) {
        this.leaderId = leaderId;
    }

    public Boolean getImmutable() {
        return immutable;
    }

    public void setImmutable(Boolean immutable) {
        this.immutable = immutable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JobTitleDTO jobTitleDTO = (JobTitleDTO) o;

        if ( ! Objects.equals(id, jobTitleDTO.id)) return false;

        return true;
    }

    public Set<Long> getSubordinateIds() {
        return subordinateIds;
    }

    public void setSubordinateIds(Set<Long> subordinateIds) {
        this.subordinateIds = subordinateIds;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "JobTitleDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
