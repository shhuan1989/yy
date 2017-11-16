package com.yijia.yy.domain;

import com.yijia.yy.domain.converter.ApprovalStatusConverter;
import com.yijia.yy.domain.enumeration.ApprovalStatus;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Approval.
 */
@Entity
@Table(name = "approval")
public class Approval implements Serializable {

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

    @Column(name = "status")
    @Convert(converter = ApprovalStatusConverter.class)
    private ApprovalStatus status = ApprovalStatus.NOT_START;

    @OneToOne(cascade = CascadeType.ALL)
    private Approval nextApproval;

    @Column(length = 300)
    private String correlationId;

    @ManyToOne
    private Employee approver;

    @Column(length = 4000)
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

    public Approval createTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public Approval lastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
        return this;
    }

    public void setLastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public ApprovalStatus getStatus() {
        return status;
    }

    public void setStatus(ApprovalStatus status) {
        this.status = status;
    }

    public Approval status(ApprovalStatus status) {
        this.status = status;
        return this;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Approval getNextApproval() {
        return nextApproval;
    }

    public void setNextApproval(Approval nextApproval) {
        this.nextApproval = nextApproval;
    }

    public Employee getApprover() {
        return approver;
    }

    public void setApprover(Employee approver) {
        this.approver = approver;
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
        Approval approval = (Approval) o;
        if(approval.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, approval.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Approval{" +
            "id=" + id +
            ", createTime='" + createTime + "'" +
            ", lastModifiedTime='" + lastModifiedTime + "'" +
            ", status='" + status + "'" +
            '}';
    }

    public Approval id(Long id) {
        this.id = id;
        return this;
    }
}
