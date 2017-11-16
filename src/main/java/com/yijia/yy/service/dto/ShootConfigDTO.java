package com.yijia.yy.service.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;


/**
 * A DTO for the ShootConfig entity.
 */
public class ShootConfigDTO implements Serializable {

    private Long id;

    private Long projectId;

    private String projectIdNumber;

    private String contractIdNumber;

    private String projectName;

    private String clientName;

    private Double contractMoney;

    private List<ShootConfigItemDTO> configItems;

    private Long startTime;

    private Long endTime;

    private String info;

    private Long createTime;

    private EmployeeDTO owner;

    private DictionaryDTO approvalStatus;

    private ApprovalRequestDTO approvalRequest;

    private Boolean autoStart;

    private Long actualEndTime;

    private Long actualStartTime;

    private Double budget;

    private Double actualCost;

    private Integer type;

    private String correlationId;

    private String configCorrelationId;

    private Long lastModifiedTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Double getContractMoney() {
        return contractMoney;
    }

    public void setContractMoney(Double contractMoney) {
        this.contractMoney = contractMoney;
    }

    public List<ShootConfigItemDTO> getConfigItems() {
        return configItems;
    }

    public void setConfigItems(List<ShootConfigItemDTO> configItems) {
        this.configItems = configItems;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public EmployeeDTO getOwner() {
        return owner;
    }

    public void setOwner(EmployeeDTO owner) {
        this.owner = owner;
    }

    public DictionaryDTO getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(DictionaryDTO approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public ApprovalRequestDTO getApprovalRequest() {
        return approvalRequest;
    }

    public void setApprovalRequest(ApprovalRequestDTO approvalRequest) {
        this.approvalRequest = approvalRequest;
    }

    public Boolean getAutoStart() {
        return autoStart;
    }

    public void setAutoStart(Boolean autoStart) {
        this.autoStart = autoStart;
    }

    public Long getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(Long actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    public Long getActualStartTime() {
        return actualStartTime;
    }

    public void setActualStartTime(Long actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public Double getActualCost() {
        return actualCost;
    }

    public void setActualCost(Double actualCost) {
        this.actualCost = actualCost;
    }

    public String getConfigCorrelationId() {
        return configCorrelationId;
    }

    public void setConfigCorrelationId(String configCorrelationId) {
        this.configCorrelationId = configCorrelationId;
    }

    public Long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ShootConfigDTO shootConfigDTO = (ShootConfigDTO) o;

        if ( ! Objects.equals(id, shootConfigDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ShootConfigDTO{" +
            "id=" + id +
            '}';
    }
}
