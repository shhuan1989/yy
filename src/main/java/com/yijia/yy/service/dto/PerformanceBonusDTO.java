package com.yijia.yy.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the PerformanceBonus entity.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PerformanceBonusDTO implements Serializable {

    private Long id;

    private Float amount;

    private Long createTime;

    private Long lastModifiedTime;

    private Long creatorId;

    private Long lastModifierId;

    private Long projectId;

    private Long ownerId;

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

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long employeeId) {
        this.creatorId = employeeId;
    }

    public Long getLastModifierId() {
        return lastModifierId;
    }

    public void setLastModifierId(Long employeeId) {
        this.lastModifierId = employeeId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long employeeId) {
        this.ownerId = employeeId;
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

        PerformanceBonusDTO performanceBonusDTO = (PerformanceBonusDTO) o;

        if ( ! Objects.equals(id, performanceBonusDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PerformanceBonusDTO{" +
            "id=" + id +
            ", amount='" + amount + "'" +
            ", createTime='" + createTime + "'" +
            ", lastModifiedTime='" + lastModifiedTime + "'" +
            '}';
    }
}
