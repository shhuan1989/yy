package com.yijia.yy.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the ProjectProgressItem entity.
 */
public class ProjectProgressItemDTO implements Serializable {

    private Long id;

    private Long eta;

    private Long finishTime;

    private Integer ord;

    private Long ownerId;

    private String ownerName;

    private Long projectProgressTableId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getEta() {
        return eta;
    }

    public void setEta(Long eta) {
        this.eta = eta;
    }
    public Long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Long finishTime) {
        this.finishTime = finishTime;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long employeeId) {
        this.ownerId = employeeId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Long getProjectProgressTableId() {
        return projectProgressTableId;
    }

    public void setProjectProgressTableId(Long projectProgressTableId) {
        this.projectProgressTableId = projectProgressTableId;
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

        ProjectProgressItemDTO projectProgressItemDTO = (ProjectProgressItemDTO) o;

        if ( ! Objects.equals(id, projectProgressItemDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProjectProgressItemDTO{" +
            "id=" + id +
            ", eta='" + eta + "'" +
            ", finishTime='" + finishTime + "'" +
            '}';
    }
}
