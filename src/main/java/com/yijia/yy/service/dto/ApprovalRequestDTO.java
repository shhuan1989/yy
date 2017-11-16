package com.yijia.yy.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;


/**
 * A DTO for the ApprovalRequest entity.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApprovalRequestDTO implements Serializable {

    private Long id;

    @NotNull
    private Long createTime;

    @NotNull
    private Long lastModifiedTime;

    private String name;

    private DictionaryDTO status;

    private Long applicantId;

    public Long getId() {
        return id;
    }

    private ApprovalDTO approvalRoot;

    private String result;

    private Boolean active;

    private String correlationId;

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
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DictionaryDTO getStatus() {
        return status;
    }

    public void setStatus(DictionaryDTO status) {
        this.status = status;
    }

    public Long getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(Long employeeId) {
        this.applicantId = employeeId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ApprovalDTO getApprovalRoot() {
        return approvalRoot;
    }

    public void setApprovalRoot(ApprovalDTO approvalRoot) {
        this.approvalRoot = approvalRoot;
    }


    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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

        ApprovalRequestDTO approvalRequestDTO = (ApprovalRequestDTO) o;

        if ( ! Objects.equals(id, approvalRequestDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ApprovalRequestDTO{" +
            "id=" + id +
            ", createTime='" + createTime + "'" +
            ", lastModifiedTime='" + lastModifiedTime + "'" +
            ", name='" + name + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
