package com.yijia.yy.service.dto;


public class ShootConfigSearchDTO {
    private String projectIdNumber;

    private String contractIdNumber;

    private String projectName;

    private Integer approvalStatusId;

    private Integer type;

    private Long projectId;

    private Long lastModifiedTimeFrom;

    private Long lastModifiedTimeTo;

    public String getProjectIdNumber() {
        return projectIdNumber;
    }

    public void setProjectIdNumber(String projectIdNumber) {
        this.projectIdNumber = projectIdNumber;
    }

    public String getContractIdNumber() {
        return contractIdNumber;
    }

    public void setContractIdNumber(String contractIdNumber) {
        this.contractIdNumber = contractIdNumber;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getApprovalStatusId() {
        return approvalStatusId;
    }

    public void setApprovalStatusId(Integer approvalStatusId) {
        this.approvalStatusId = approvalStatusId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getLastModifiedTimeFrom() {
        return lastModifiedTimeFrom;
    }

    public void setLastModifiedTimeFrom(Long lastModifiedTimeFrom) {
        this.lastModifiedTimeFrom = lastModifiedTimeFrom;
    }

    public Long getLastModifiedTimeTo() {
        return lastModifiedTimeTo;
    }

    public void setLastModifiedTimeTo(Long lastModifiedTimeTo) {
        this.lastModifiedTimeTo = lastModifiedTimeTo;
    }
}
