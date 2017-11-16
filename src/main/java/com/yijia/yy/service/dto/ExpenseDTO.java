package com.yijia.yy.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Expense entity.
 */
public class ExpenseDTO implements Serializable {

    private Long id;

    private Long payTime;

    private Double total;

    private Long payMethodId;

    private String payMethodName;

    private Long projectId;

    private String projectIdNumber;

    private String projectName;

    private String correlationId;

    private Long startTime;

    private Long endTime;

    private String info;

    private Long createTime;

    private EmployeeDTO owner;

    private DictionaryDTO approvalStatus;

    private ApprovalRequestDTO approvalRequest;

    private Boolean autoStart;

    private List<ExpenseItemDTO> items;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getPayTime() {
        return payTime;
    }

    public void setPayTime(Long payTime) {
        this.payTime = payTime;
    }

    public Long getPayMethodId() {
        return payMethodId;
    }

    public void setPayMethodId(Long dictionaryId) {
        this.payMethodId = dictionaryId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectIdNumber() {
        return projectIdNumber;
    }

    public void setProjectIdNumber(String projectIdNumber) {
        this.projectIdNumber = projectIdNumber;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
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

    public List<ExpenseItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ExpenseItemDTO> items) {
        this.items = items;
    }

    public String getPayMethodName() {
        return payMethodName;
    }

    public void setPayMethodName(String payMethodName) {
        this.payMethodName = payMethodName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ExpenseDTO expenseDTO = (ExpenseDTO) o;

        if ( ! Objects.equals(id, expenseDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ExpenseDTO{" +
            "id=" + id +
            ", payTime='" + payTime + "'" +
            '}';
    }
}
