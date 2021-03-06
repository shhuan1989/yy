package com.yijia.yy.domain;


import com.yijia.yy.domain.converter.ApprovalStatusConverter;
import com.yijia.yy.domain.enumeration.ApprovalStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ApprovalRequest.
 */
@Entity
@Inheritance
@Table(name = "approval_request")
public class ApprovalRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "create_time", nullable = false)
    private Long createTime;

    @NotNull
    @Column(name = "last_modified_time", nullable = false)
    private Long lastModifiedTime;

    @Column(name = "name")
    private String name;

    @ManyToOne(cascade = CascadeType.REFRESH)
    private Employee applicant;

    // first node of approval chain
    @OneToOne(cascade = CascadeType.ALL)
    private Approval approvalRoot;

    @Column(name = "status")
    @Convert(converter = ApprovalStatusConverter.class)
    private ApprovalStatus status = ApprovalStatus.NOT_START;

    @Column(length = 4000)
    private String result;

    @Column
    private Boolean active;

    @Column(length = 300, unique = true)
    private String correlationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public ApprovalRequest createTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public ApprovalRequest lastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
        return this;
    }

    public void setLastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getName() {
        return name;
    }

    public ApprovalRequest name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Employee getApplicant() {
        return applicant;
    }

    public ApprovalRequest applicant(Employee employee) {
        this.applicant = employee;
        return this;
    }

    public void setApplicant(Employee employee) {
        this.applicant = employee;
    }

    public ApprovalStatus getStatus() {
        return status;
    }

    public void setStatus(ApprovalStatus status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Approval getApprovalRoot() {
        return approvalRoot;
    }

    public void setApprovalRoot(Approval approvalRoot) {
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
        ApprovalRequest approvalRequest = (ApprovalRequest) o;
        if(approvalRequest.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, approvalRequest.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ApprovalRequest{" +
            "id=" + id +
            ", createTime='" + createTime + "'" +
            ", lastModifiedTime='" + lastModifiedTime + "'" +
            ", name='" + name + "'" +
            '}';
    }

    public ApprovalRequest status(ApprovalStatus status) {
        this.status = status;
        return this;
    }
}
