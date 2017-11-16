package com.yijia.yy.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * dto for searching contracts
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ContractSearchDTO {
    private String projectIdNumber;
    private String idNumber;
    private String projectName;
    private String clientName;
    private Long signTimeFrom;
    private Long signTimeTo;
    private Long payTimeFrom;
    private Long payTimeTo;
    private Integer paymentStatusId;

    public String getProjectIdNumber() {
        return projectIdNumber;
    }

    public void setProjectIdNumber(String projectIdNumber) {
        this.projectIdNumber = projectIdNumber;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Long getSignTimeFrom() {
        return signTimeFrom;
    }

    public void setSignTimeFrom(Long signTimeFrom) {
        this.signTimeFrom = signTimeFrom;
    }

    public Long getSignTimeTo() {
        return signTimeTo;
    }

    public void setSignTimeTo(Long signTimeTo) {
        this.signTimeTo = signTimeTo;
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

    public Integer getPaymentStatusId() {
        return paymentStatusId;
    }

    public void setPaymentStatusId(Integer paymentStatusId) {
        this.paymentStatusId = paymentStatusId;
    }
}
