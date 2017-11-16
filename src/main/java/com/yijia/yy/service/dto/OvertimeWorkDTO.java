package com.yijia.yy.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the OvertimeWork entity.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OvertimeWorkDTO implements Serializable {

    private Long id;

    @NotNull
    private Long startTime;

    @NotNull
    private Long endTime;

    @NotNull
    private Integer hours;

    private String info;

    private Long createTime;

    private EmployeeDTO owner;

    private DictionaryDTO approvalStatus;

    private ApprovalRequestDTO approvalRequest;

    private Boolean autoStart;

    private Long actualEndTime;

    private Long actualStartTime;

    private String correlationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
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

        OvertimeWorkDTO overtimeWorkDTO = (OvertimeWorkDTO) o;

        if ( ! Objects.equals(id, overtimeWorkDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OvertimeWorkDTO{" +
            "id=" + id +
            ", startTime='" + startTime + "'" +
            ", endTime='" + endTime + "'" +
            ", hours='" + hours + "'" +
            ", info='" + info + "'" +
            ", createTime='" + createTime + "'" +
            '}';
    }
}
