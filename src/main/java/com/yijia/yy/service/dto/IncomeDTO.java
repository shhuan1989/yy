package com.yijia.yy.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Income entity.
 */
public class IncomeDTO implements Serializable {

    private Long id;

    private Long incomeTime;

    private Double amount;

    private String incomeDesc;

    private String memo;

    private Long incomeMethodId;

    private String incomeMethodName;

    private Long startTime;

    private Long endTime;

    private Long actualStartTime;

    private Long actualEndTime;

    private String info;

    private Long createTime;

    private String correlationId;

    private EmployeeDTO owner;

    private DictionaryDTO approvalStatus;

    private ApprovalRequestDTO approvalRequest;

    private Boolean autoStart = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getIncomeTime() {
        return incomeTime;
    }

    public void setIncomeTime(Long incomeTime) {
        this.incomeTime = incomeTime;
    }
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public String getIncomeDesc() {
        return incomeDesc;
    }

    public void setIncomeDesc(String incomeDesc) {
        this.incomeDesc = incomeDesc;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getIncomeMethodId() {
        return incomeMethodId;
    }

    public void setIncomeMethodId(Long dictionaryId) {
        this.incomeMethodId = dictionaryId;
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

    public Long getActualStartTime() {
        return actualStartTime;
    }

    public void setActualStartTime(Long actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    public Long getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(Long actualEndTime) {
        this.actualEndTime = actualEndTime;
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

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
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

    public String getIncomeMethodName() {
        return incomeMethodName;
    }

    public void setIncomeMethodName(String incomeMethodName) {
        this.incomeMethodName = incomeMethodName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IncomeDTO incomeDTO = (IncomeDTO) o;

        if ( ! Objects.equals(id, incomeDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "IncomeDTO{" +
            "id=" + id +
            ", incomeTime='" + incomeTime + "'" +
            ", amount='" + amount + "'" +
            ", incomeDesc='" + incomeDesc + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
