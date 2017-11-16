package com.yijia.yy.service.dto;

/**
 * search dto for income
 */
public class IncomeSearchDTO {

    private Long incomeTimeFrom;
    private Long incomeTimeTo;
    private Long incomeMethodId;
    private Long incomeStatusId;

    private String projectIdNumber;
    private String clientName;
    private String projectName;
    private String idNumber;

    public Long getIncomeTimeFrom() {
        return incomeTimeFrom;
    }

    public void setIncomeTimeFrom(Long incomeTimeFrom) {
        this.incomeTimeFrom = incomeTimeFrom;
    }

    public Long getIncomeTimeTo() {
        return incomeTimeTo;
    }

    public void setIncomeTimeTo(Long incomeTimeTo) {
        this.incomeTimeTo = incomeTimeTo;
    }

    public Long getIncomeMethodId() {
        return incomeMethodId;
    }

    public void setIncomeMethodId(Long incomeMethodId) {
        this.incomeMethodId = incomeMethodId;
    }

    public Long getIncomeStatusId() {
        return incomeStatusId;
    }

    public void setIncomeStatusId(Long incomeStatusId) {
        this.incomeStatusId = incomeStatusId;
    }

    public String getProjectIdNumber() {
        return projectIdNumber;
    }

    public void setProjectIdNumber(String projectIdNumber) {
        this.projectIdNumber = projectIdNumber;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }
}
