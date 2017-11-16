package com.yijia.yy.service.dto;

/**
 * Created by palad on 2017/1/8.
 */
public class DirectorNeedsSearchDTO {
    private Long projectIdNumber;
    private Long contractIdNumber;
    private String projectName;
    private Long creatorId;

    public Long getProjectIdNumber() {
        return projectIdNumber;
    }

    public void setProjectIdNumber(Long projectIdNumber) {
        this.projectIdNumber = projectIdNumber;
    }

    public Long getContractIdNumber() {
        return contractIdNumber;
    }

    public void setContractIdNumber(Long contractIdNumber) {
        this.contractIdNumber = contractIdNumber;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }
}
