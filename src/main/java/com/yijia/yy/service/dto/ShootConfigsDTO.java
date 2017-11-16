package com.yijia.yy.service.dto;

import java.util.List;

/**
 * Created by palad on 2017/1/8.
 */
public class ShootConfigsDTO {

    private Long projectId;

    private String projectIdNumber;

    private String projectName;

    List<ShootConfigDTO> shootConfigs;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectIdNumber() {
        return projectIdNumber;
    }

    public void setProjectIdNumber(String projectIdNumber) {
        this.projectIdNumber = projectIdNumber;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<ShootConfigDTO> getShootConfigs() {
        return shootConfigs;
    }

    public void setShootConfigs(List<ShootConfigDTO> shootConfigs) {
        this.shootConfigs = shootConfigs;
    }
}
