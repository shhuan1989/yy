package com.yijia.yy.service.dto;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PerformanceApprovalRequestDTO extends ApprovalRequestDTO implements Serializable {

    private List<PerformanceBonusDTO> bonuses;

    private Long projectId;

    private Long issueTime;

    private Boolean issued;

    private Long issuerId;

    private Float workType;

    private Float productionDifficulty;

    public List<PerformanceBonusDTO> getBonuses() {
        return bonuses;
    }

    public void setBonuses(List<PerformanceBonusDTO> bonuses) {
        this.bonuses = bonuses;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(Long issueTime) {
        this.issueTime = issueTime;
    }

    public Boolean getIssued() {
        return issued;
    }

    public void setIssued(Boolean issued) {
        this.issued = issued;
    }

    public Long getIssuerId() {
        return issuerId;
    }

    public void setIssuerId(Long issuerId) {
        this.issuerId = issuerId;
    }

    public Float getWorkType() {
        return workType;
    }

    public void setWorkType(Float workType) {
        this.workType = workType;
    }

    public Float getProductionDifficulty() {
        return productionDifficulty;
    }

    public void setProductionDifficulty(Float productionDifficulty) {
        this.productionDifficulty = productionDifficulty;
    }
}
