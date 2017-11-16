package com.yijia.yy.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yijia.yy.domain.Dictionary;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the AssetBorrowRecord entity.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AssetBorrowRecordDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer amount;

    private Long startTime;

    private Long endTime;

    private Long createTime;

    private Long actualEndTime;

    private Long actualStartTime;

    private AssetDTO asset;

    private EmployeeDTO owner;

    private EmployeeDTO returner;

    private DictionaryDTO approvalStatus;

    private ApprovalRequestDTO approvalRequest;

    private Boolean autoStart;

    private String info;

    private DictionaryDTO returnStatus;

    private String correlationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
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

    public AssetDTO getAsset() {
        return asset;
    }

    public void setAsset(AssetDTO asset) {
        this.asset = asset;
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

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public DictionaryDTO getReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(DictionaryDTO returnStatus) {
        this.returnStatus = returnStatus;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public EmployeeDTO getReturner() {
        return returner;
    }

    public void setReturner(EmployeeDTO returner) {
        this.returner = returner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AssetBorrowRecordDTO assetBorrowRecordDTO = (AssetBorrowRecordDTO) o;

        if ( ! Objects.equals(id, assetBorrowRecordDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AssetBorrowRecordDTO{" +
            "id=" + id +
            ", amount=" + amount +
            ", startTime=" + startTime +
            ", endTime=" + endTime +
            ", createTime=" + createTime +
            ", actualEndTime=" + actualEndTime +
            ", actualStartTime=" + actualStartTime +
            ", asset=" + asset +
            ", owner=" + owner +
            ", approvalStatus=" + approvalStatus +
            ", approvalRequest=" + approvalRequest +
            ", autoStart=" + autoStart +
            ", info='" + info + '\'' +
            ", returnStatus=" + returnStatus +
            '}';
    }
}
