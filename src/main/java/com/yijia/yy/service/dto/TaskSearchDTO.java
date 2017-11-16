package com.yijia.yy.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TaskSearchDTO {
    private Long projectId;
    private Long ownerId;
    private Boolean shownOwnedOnly;
    private Long creatorId;
    private Boolean showCreatedOnly;
    private Integer status;
    private int[] statusIds;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Boolean getShownOwnedOnly() {
        return shownOwnedOnly;
    }

    public void setShownOwnedOnly(Boolean shownOwnedOnly) {
        this.shownOwnedOnly = shownOwnedOnly;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Boolean getShowCreatedOnly() {
        return showCreatedOnly;
    }

    public void setShowCreatedOnly(Boolean showCreatedOnly) {
        this.showCreatedOnly = showCreatedOnly;
    }

    public TaskSearchDTO projectId(Long projectId) {
        this.projectId = projectId;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public int[] getStatusIds() {
        return statusIds;
    }

    public void setStatusIds(int[] statusIds) {
        this.statusIds = statusIds;
    }
}
