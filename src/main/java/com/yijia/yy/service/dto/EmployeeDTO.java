package com.yijia.yy.service.dto;

import com.yijia.yy.domain.City;
import com.yijia.yy.domain.Dept;
import com.yijia.yy.domain.JobTitle;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yijia.yy.domain.Province;

/**
 * A DTO for the Employee entity.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EmployeeDTO implements Serializable {

    private Long id;

    private String name;

    private DictionaryDTO gender;

    private Integer age;

    private DictionaryDTO education;

    private String empNumber;

    private Province nativePlace;

    private String nationality;

    private String idNumber;

    private String basicInfo;

    private Long hireDate;

    private Double probationSalary;

    private Double salary;

    private DictionaryDTO jobPositionStatus;

    private String jobPositionInfo;

    private Long birthDate;

    private DictionaryDTO marriage;

    private DictionaryDTO childbearing;

    private String hukou;

    private String pensionAccount;

    private String houseFoundAccount;

    private DictionaryDTO hasDriverLicense;

    private String cellphone;

    private String mail;

    private String qq;

    private String workQq;

    private String workQqPassword;

    private String wechart;

    private String address;

    private String tel;

    private String emergencyTel;

    private String emergencyContact;

    private String relationshipWithEmergencyContact;

    private String additionalInfo;

    private String computerLevel;

    private String foreignLanguageLevel;

    private Set<JobTitleDTO> jobTitles;

    private Dept dept;

    private Double probationDeduction;

    private Double employeeDeduction;

    private String login;

    private PictureInfoDTO photo;

    private Long lastUpdateTime;

    private Boolean deleted;

    private String uniqueName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DictionaryDTO getGender() {
        return gender;
    }

    public void setGender(DictionaryDTO gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public DictionaryDTO getEducation() {
        return education;
    }

    public void setEducation(DictionaryDTO education) {
        this.education = education;
    }

    public String getEmpNumber() {
        return empNumber;
    }

    public void setEmpNumber(String empNumber) {
        this.empNumber = empNumber;
    }

    public Province getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(Province nativePlace) {
        this.nativePlace = nativePlace;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getBasicInfo() {
        return basicInfo;
    }

    public void setBasicInfo(String basicInfo) {
        this.basicInfo = basicInfo;
    }

    public Long getHireDate() {
        return hireDate;
    }

    public void setHireDate(Long hireDate) {
        this.hireDate = hireDate;
    }

    public Double getProbationSalary() {
        return probationSalary;
    }

    public void setProbationSalary(Double probationSalary) {
        this.probationSalary = probationSalary;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public DictionaryDTO getJobPositionStatus() {
        return jobPositionStatus;
    }

    public void setJobPositionStatus(DictionaryDTO jobPositionStatus) {
        this.jobPositionStatus = jobPositionStatus;
    }

    public String getJobPositionInfo() {
        return jobPositionInfo;
    }

    public void setJobPositionInfo(String jobPositionInfo) {
        this.jobPositionInfo = jobPositionInfo;
    }

    public Long getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Long birthDate) {
        this.birthDate = birthDate;
    }

    public DictionaryDTO getMarriage() {
        return marriage;
    }

    public void setMarriage(DictionaryDTO marriage) {
        this.marriage = marriage;
    }

    public DictionaryDTO getChildbearing() {
        return childbearing;
    }

    public void setChildbearing(DictionaryDTO childbearing) {
        this.childbearing = childbearing;
    }

    public String getHukou() {
        return hukou;
    }

    public void setHukou(String hukou) {
        this.hukou = hukou;
    }

    public String getPensionAccount() {
        return pensionAccount;
    }

    public void setPensionAccount(String pensionAccount) {
        this.pensionAccount = pensionAccount;
    }

    public String getHouseFoundAccount() {
        return houseFoundAccount;
    }

    public void setHouseFoundAccount(String houseFoundAccount) {
        this.houseFoundAccount = houseFoundAccount;
    }

    public DictionaryDTO getHasDriverLicense() {
        return hasDriverLicense;
    }

    public void setHasDriverLicense(DictionaryDTO hasDriverLicense) {
        this.hasDriverLicense = hasDriverLicense;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWorkQq() {
        return workQq;
    }

    public void setWorkQq(String workQq) {
        this.workQq = workQq;
    }

    public String getWorkQqPassword() {
        return workQqPassword;
    }

    public void setWorkQqPassword(String workQqPassword) {
        this.workQqPassword = workQqPassword;
    }

    public String getWechart() {
        return wechart;
    }

    public void setWechart(String wechart) {
        this.wechart = wechart;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmergencyTel() {
        return emergencyTel;
    }

    public void setEmergencyTel(String emergencyTel) {
        this.emergencyTel = emergencyTel;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getRelationshipWithEmergencyContact() {
        return relationshipWithEmergencyContact;
    }

    public void setRelationshipWithEmergencyContact(String relationshipWithEmergencyContact) {
        this.relationshipWithEmergencyContact = relationshipWithEmergencyContact;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getComputerLevel() {
        return computerLevel;
    }

    public void setComputerLevel(String computerLevel) {
        this.computerLevel = computerLevel;
    }

    public String getForeignLanguageLevel() {
        return foreignLanguageLevel;
    }

    public void setForeignLanguageLevel(String foreignLanguageLevel) {
        this.foreignLanguageLevel = foreignLanguageLevel;
    }

    public Set<JobTitleDTO> getJobTitles() {
        return jobTitles == null ? new HashSet<>() : jobTitles;
    }

    public void setJobTitles(Set<JobTitleDTO> jobTitles) {
        this.jobTitles = jobTitles;
    }

    public Dept getDept() {
        return dept;
    }

    public void setDept(Dept dept) {
        this.dept = dept;
    }

    public Double getProbationDeduction() {
        return probationDeduction;
    }

    public void setProbationDeduction(Double probationDeduction) {
        this.probationDeduction = probationDeduction;
    }

    public Double getEmployeeDeduction() {
        return employeeDeduction;
    }

    public void setEmployeeDeduction(Double employeeDeduction) {
        this.employeeDeduction = employeeDeduction;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public PictureInfoDTO getPhoto() {
        return photo;
    }

    public void setPhoto(PictureInfoDTO photo) {
        this.photo = photo;
    }

    public Long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EmployeeDTO employeeDTO = (EmployeeDTO) o;

        if ( ! Objects.equals(id, employeeDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EmployeeDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", gender=" + gender +
            ", age=" + age +
            ", education=" + education +
            ", empNumber='" + empNumber + '\'' +
            ", nativePlace=" + nativePlace +
            ", nationality='" + nationality + '\'' +
            ", idNumber='" + idNumber + '\'' +
            ", basicInfo='" + basicInfo + '\'' +
            ", hireDate=" + hireDate +
            ", probationSalary=" + probationSalary +
            ", salary=" + salary +
            ", jobPositionStatus=" + jobPositionStatus +
            ", jobPositionInfo='" + jobPositionInfo + '\'' +
            ", birthDate=" + birthDate +
            ", marriage=" + marriage +
            ", childbearing=" + childbearing +
            ", hukou='" + hukou + '\'' +
            ", pensionAccount='" + pensionAccount + '\'' +
            ", houseFoundAccount='" + houseFoundAccount + '\'' +
            ", hasDriverLicense=" + hasDriverLicense +
            ", cellphone='" + cellphone + '\'' +
            ", mail='" + mail + '\'' +
            ", qq='" + qq + '\'' +
            ", workQq='" + workQq + '\'' +
            ", workQqPassword='" + workQqPassword + '\'' +
            ", wechart='" + wechart + '\'' +
            ", address='" + address + '\'' +
            ", tel='" + tel + '\'' +
            ", emergencyTel='" + emergencyTel + '\'' +
            ", emergencyContact='" + emergencyContact + '\'' +
            ", relationshipWithEmergencyContact='" + relationshipWithEmergencyContact + '\'' +
            ", additionalInfo='" + additionalInfo + '\'' +
            ", computerLevel='" + computerLevel + '\'' +
            ", foreignLanguageLevel='" + foreignLanguageLevel + '\'' +
            ", dept=" + dept +
            ", probationDeduction=" + probationDeduction +
            ", employeeDeduction=" + employeeDeduction +
            '}';
    }
}
