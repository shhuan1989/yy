package com.yijia.yy.service.dto;

/**
 * DTO for searching expense
 */
public class ExpenseSearchDTO {
    private String projectIdNumber;

    private String contractIdNumber;

    private String projectName;

    private Integer approvalStatusId;

    private Long projectId;

    private Long payMethodId;

    private Long paymentStatusId;

    private Long payTimeFrom;

    private Long payTimeTo;

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

    public Integer getApprovalStatusId() {
        return approvalStatusId;
    }

    public void setApprovalStatusId(Integer approvalStatusId) {
        this.approvalStatusId = approvalStatusId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getPayMethodId() {
        return payMethodId;
    }

    public void setPayMethodId(Long payMethodId) {
        this.payMethodId = payMethodId;
    }

    public Long getPaymentStatusId() {
        return paymentStatusId;
    }

    public void setPaymentStatusId(Long paymentStatusId) {
        this.paymentStatusId = paymentStatusId;
    }

    public Long getPayTimeFrom() {
        return payTimeFrom;
    }

    public void setPayTimeFrom(Long payTimeFrom) {
        this.payTimeFrom = payTimeFrom;
    }

    public Long getPayTimeTo() {
        return payTimeTo;
    }

    public void setPayTimeTo(Long payTimeTo) {
        this.payTimeTo = payTimeTo;
    }
}
