package com.yijia.yy.service.dto;

import com.yijia.yy.domain.Dictionary;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Approval entity.
 */
public class ApprovalDTO implements Serializable {

    private Long id;

    @NotNull
    private Long createTime;

    @NotNull
    private Long lastModifiedTime;

    private DictionaryDTO status;

    private ApprovalDTO nextApproval;

    private Long approverId;

    private String correlationId;

    private String result;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
    public Long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public DictionaryDTO getStatus() {
        return status;
    }

    public void setStatus(DictionaryDTO status) {
        this.status = status;
    }

    public ApprovalDTO getNextApproval() {
        return nextApproval;
    }

    public void setNextApproval(ApprovalDTO nextApproval) {
        this.nextApproval = nextApproval;
    }

    public Long getApproverId() {
        return approverId;
    }

    public void setApproverId(Long approverId) {
        this.approverId = approverId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ApprovalDTO approvalDTO = (ApprovalDTO) o;

        if ( ! Objects.equals(id, approvalDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ApprovalDTO{" +
            "id=" + id +
            ", createTime='" + createTime + "'" +
            ", lastModifiedTime='" + lastModifiedTime + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
