package com.yijia.yy.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AssetBorrowRecordSearchDTO {
    private Boolean shownOwnedOnly;
    private Long ownerId;
    private Integer approvalStatusId;
    private Long timeFrom;
    private Long timeTo;
    private Long deptId;
    private Long jobTitleId;

    public Boolean getShownOwnedOnly() {
        return shownOwnedOnly;
    }

    public void setShownOwnedOnly(Boolean shownOwnedOnly) {
        this.shownOwnedOnly = shownOwnedOnly;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getApprovalStatusId() {
        return approvalStatusId;
    }

    public void setApprovalStatusId(Integer approvalStatusId) {
        this.approvalStatusId = approvalStatusId;
    }

    public Long getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(Long timeFrom) {
        this.timeFrom = timeFrom;
    }

    public Long getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(Long timeTo) {
        this.timeTo = timeTo;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getJobTitleId() {
        return jobTitleId;
    }

    public void setJobTitleId(Long jobTitleId) {
        this.jobTitleId = jobTitleId;
    }
}
