package com.yijia.yy.domain;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 *
 * the base class for items that need approve
 *
 * ApprovalItem contains an ApprovalRequest,
 * which is a list of Approval
 *
 */
@Entity
@Inheritance
@Table(name = "approval_item")
public class ApprovalItem implements Serializable {

    private static final long serialVersionUID = 6037537411344188434L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "start_time")
    private Long startTime;

    @Column(name = "end_time")
    private Long endTime;

    @Column(name = "actual_start_time")
    private Long actualStartTime;

    @Column(name = "actual_end_time")
    private Long actualEndTime;


    @Column(name = "info")
    private String info;

    @Column(name = "create_time")
    private Long createTime;

    @Column(length = 300, unique = true)
    private String correlationId;

    @ManyToOne
    private Employee owner;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private ApprovalRequest approvalRequest;

    @Column
    private Boolean autoStart = false;

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

    public Employee getOwner() {
        return owner;
    }

    public void setOwner(Employee owner) {
        this.owner = owner;
    }

    public ApprovalRequest getApprovalRequest() {
        return approvalRequest;
    }

    public void setApprovalRequest(ApprovalRequest approvalRequest) {
        this.approvalRequest = approvalRequest;
    }

    public Boolean getAutoStart() {
        return autoStart;
    }

    public void setAutoStart(Boolean autoStart) {
        this.autoStart = autoStart;
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


    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApprovalItem that = (ApprovalItem) o;

        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }


}
