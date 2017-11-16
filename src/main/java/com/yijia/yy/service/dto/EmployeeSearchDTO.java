package com.yijia.yy.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yijia.yy.domain.City;
import com.yijia.yy.domain.Dept;
import com.yijia.yy.domain.JobTitle;
import com.yijia.yy.domain.converter.EducationConverter;
import com.yijia.yy.domain.converter.GenderConverter;
import com.yijia.yy.domain.converter.JobPositionStatusConverter;
import com.yijia.yy.domain.enumeration.BooleanEnum;
import com.yijia.yy.domain.enumeration.Education;
import com.yijia.yy.domain.enumeration.Gender;
import com.yijia.yy.domain.enumeration.JobPositionStatus;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EmployeeSearchDTO {

    private String name;
    private Integer genderId;
    private Integer ageFrom;
    private Integer ageTo;
    private Long nativePlaceId;
    private Integer educationId;
    private Integer jobPositionStatusId;
    private Long deptId;
    private Long jobTitleId;
    private Long hireDateFrom;
    private Long hireDateTo;
    private Integer hasDeduct;
    private Boolean hasLogin;
    private Boolean deleted;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGenderId() {
        return genderId;
    }

    public Gender getGender() {
        if (this.genderId == null) {
            return null;
        }
        return new GenderConverter().convertToEntityAttribute(this.genderId);
    }

    public Education getEducation() {
        if (this.educationId == null) {
            return null;
        }
        return new EducationConverter().convertToEntityAttribute(this.educationId);
    }

    public JobPositionStatus getJobPositionStatus() {
        if (this.jobPositionStatusId == null) {
            return null;
        }
        return new JobPositionStatusConverter().convertToEntityAttribute(this.jobPositionStatusId);
    }

    public City getNativePlace() {
        return new City().withId(this.nativePlaceId);
    }

    public JobTitle getJobTitle() {
        return new JobTitle().withId(this.jobTitleId);
    }

    public Dept getDept() {
        return new Dept().withId(this.deptId);
    }

    public void setGenderId(Integer genderId) {
        this.genderId = genderId;
    }

    public Integer getAgeFrom() {
        return ageFrom;
    }

    public void setAgeFrom(Integer ageFrom) {
        this.ageFrom = ageFrom;
    }

    public Integer getAgeTo() {
        return ageTo;
    }

    public void setAgeTo(Integer ageTo) {
        this.ageTo = ageTo;
    }

    public Long getNativePlaceId() {
        return nativePlaceId;
    }

    public void setNativePlaceId(Long nativePlaceId) {
        this.nativePlaceId = nativePlaceId;
    }

    public Integer getEducationId() {
        return educationId;
    }

    public void setEducationId(Integer educationId) {
        this.educationId = educationId;
    }

    public Integer getJobPositionStatusId() {
        return jobPositionStatusId;
    }

    public void setJobPositionStatusId(Integer jobPositionStatusId) {
        this.jobPositionStatusId = jobPositionStatusId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getJobTitleId() {
        return jobTitleId;
    }

    public void setJobTitleId(Long jobTitleId) {
        this.jobTitleId = jobTitleId;
    }

    public Long getHireDateFrom() {
        return hireDateFrom;
    }

    public void setHireDateFrom(Long hireDateFrom) {
        this.hireDateFrom = hireDateFrom;
    }

    public Long getHireDateTo() {
        return hireDateTo;
    }

    public void setHireDateTo(Long hireDateTo) {
        this.hireDateTo = hireDateTo;
    }

    public Integer getHasDeduct() {
        return hasDeduct;
    }

    public void setHasDeduct(Integer hasDeduct) {
        this.hasDeduct = hasDeduct;
    }

    public Boolean getHasLogin() {
        return hasLogin;
    }

    public void setHasLogin(Boolean hasLogin) {
        this.hasLogin = hasLogin;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "EmployeeSearchDTO{" +
            "name='" + name + '\'' +
            ", genderId=" + genderId +
            ", ageFrom=" + ageFrom +
            ", ageTo=" + ageTo +
            ", nativePlaceId=" + nativePlaceId +
            ", educationId=" + educationId +
            ", jobPositionStatusId=" + jobPositionStatusId +
            ", deptId=" + deptId +
            ", jobTitleId=" + jobTitleId +
            ", hireDateFrom=" + hireDateFrom +
            ", hireDateTo=" + hireDateTo +
            '}';
    }
}
