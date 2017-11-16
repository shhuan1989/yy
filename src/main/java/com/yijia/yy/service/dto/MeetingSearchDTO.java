package com.yijia.yy.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MeetingSearchDTO {
    private Long dateFrom;
    private Long dateTo;
    private Long projectId;
    private Integer statusId;
    private int[] statusIds;
    private Long memberId;
    private Boolean currentUserOnly;


    public Long getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Long dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Long getDateTo() {
        return dateTo;
    }

    public void setDateTo(Long dateTo) {
        this.dateTo = dateTo;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public int[] getStatusIds() {
        return statusIds;
    }

    public void setStatusIds(int[] statusIds) {
        this.statusIds = statusIds;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Boolean getCurrentUserOnly() {
        return currentUserOnly;
    }

    public void setCurrentUserOnly(Boolean currentUserOnly) {
        this.currentUserOnly = currentUserOnly;
    }
}
