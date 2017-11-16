package com.yijia.yy.service.dto;

public class PerformanceBonusSearchDTO {

    private Long projectId;
    private Long ownerId;
    private Boolean shownOwnedOnly;

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
}
