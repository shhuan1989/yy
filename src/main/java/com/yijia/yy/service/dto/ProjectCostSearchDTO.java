package com.yijia.yy.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * dto for searching costs of project
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProjectCostSearchDTO {
    private Long projectId;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
