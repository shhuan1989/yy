package com.yijia.yy.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * dto for searching rates of project
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProjectRateSearchDTO {
    private Long projectId;
    private Boolean finished;
    private Long ownerId;
    private Boolean onlyOwned;
    private Boolean isAvg;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Boolean getOnlyOwned() {
        return onlyOwned;
    }

    public void setOnlyOwned(Boolean onlyOwned) {
        this.onlyOwned = onlyOwned;
    }

    public Boolean getAvg() {
        return isAvg;
    }

    public void setAvg(Boolean avg) {
        isAvg = avg;
    }
}
