package com.yijia.yy.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yijia.yy.domain.converter.BooleanEnumConverter;
import com.yijia.yy.domain.converter.EducationConverter;
import com.yijia.yy.domain.converter.GenderConverter;
import com.yijia.yy.domain.converter.JobPositionStatusConverter;
import com.yijia.yy.domain.enumeration.BooleanEnum;
import com.yijia.yy.domain.enumeration.Education;
import com.yijia.yy.domain.enumeration.Gender;
import com.yijia.yy.domain.enumeration.JobPositionStatus;
import io.jsonwebtoken.lang.Collections;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * A Employee.
 */
@Entity
@Table(name = "employee")
public class Employee implements Serializable {

    private static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Convert(converter = GenderConverter.class)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "age")
    private Integer age;

    @Convert(converter = EducationConverter.class)
    @Column(name = "education")
    private Education education;

    @Column(name = "emp_number")
    private String empNumber;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "id_number")
    private String idNumber;

    @Column(name = "basic_info")
    private String basicInfo;

    @Column(name = "hire_date")
    private Long hireDate;

    @Column(name = "probation_salary")
    private Double probationSalary;

    @Column(name = "salary")
    private Double salary;

    @Convert(converter = JobPositionStatusConverter.class)
    @Column(name = "job_position_status")
    private JobPositionStatus jobPositionStatus;

    @Column(name = "job_position_info")
    private String jobPositionInfo;

    @Column(name = "birth_date")
    private Long birthDate;

    @Column(name = "marriage")
    @Convert(converter = BooleanEnumConverter.class)
    private BooleanEnum marriage;

    @Column(name = "childbearing")
    @Convert(converter = BooleanEnumConverter.class)
    private BooleanEnum childbearing;

    @Column(name = "hukou")
    private String hukou;

    @Column(name = "pension_account")
    private String pensionAccount;

    @Column(name = "house_found_account")
    private String houseFoundAccount;

    @Column(name = "has_driver_license")
    @Convert(converter = BooleanEnumConverter.class)
    private BooleanEnum hasDriverLicense;

    @Column(name = "cellphone")
    private String cellphone;

    @Column(name = "mail")
    private String mail;

    @Column(name = "qq")
    private String qq;

    @Column(name = "work_qq")
    private String workQq;

    @Column(name = "work_qq_password")
    private String workQqPassword;

    @Column(name = "wechart")
    private String wechart;

    @Column(name = "address", length = 1000)
    private String address;

    @Column(name = "tel")
    private String tel;

    @Column(name = "emergency_tel")
    private String emergencyTel;

    @Column(name = "emergency_contact")
    private String emergencyContact;

    @Column(name = "relationship_with_emergency_contact")
    private String relationshipWithEmergencyContact;

    @Column(name = "additional_info", length = 4000)
    private String additionalInfo;

    @Column(name = "computer_level")
    private String computerLevel;

    @Column(name = "foreign_language_level")
    private String foreignLanguageLevel;

    @Column(name = "probation_deduction")
    private Double probationDeduction;

    @Column(name = "employee_deduction")
    private Double employeeDeduction;

    @Column
    private Long lastUpdateTime;

    @OneToOne
    private PictureInfo photo;

    @ManyToMany
    private Set<JobTitle> jobTitles;

    @ManyToOne
    private Province nativePlace;

    @OneToOne
    private Dept dept;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.REFRESH)
    @JsonBackReference
    private User user;

    @ManyToMany(mappedBy = "aes")
    @JsonIgnore
    private Set<Project> aedProjects = new HashSet<>();

    @ManyToMany(mappedBy = "members")
    @JsonIgnore
    private Set<Project> participatedProjects = new HashSet<>();

    @Column()
    private Boolean deleted;

    public Employee id(Long id) {
        this.id = id;
        return this;
    }

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

    public Employee name(String name) {
        this.name = name;
        return this;
    }

    public Employee gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public BooleanEnum getHasDriverLicense() {
        return hasDriverLicense;
    }

    public void setHasDriverLicense(BooleanEnum hasDriverLicense) {
        this.hasDriverLicense = hasDriverLicense;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Employee age(Integer age) {
        this.age = age;
        return this;
    }

    public Education getEducation() {
        return education;
    }

    public void setEducation(Education education) {
        this.education = education;
    }

    public Employee education(Education education) {
        this.education = education;
        return this;
    }

    public String getEmpNumber() {
        return empNumber;
    }

    public void setEmpNumber(String empNumber) {
        this.empNumber = empNumber;
    }

    public Employee empNumber(String empNumber) {
        this.empNumber = empNumber;
        return this;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Employee nationality(String nationality) {
        this.nationality = nationality;
        return this;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public Employee idNumber(String idNumber) {
        this.idNumber = idNumber;
        return this;
    }

    public String getBasicInfo() {
        return basicInfo;
    }

    public void setBasicInfo(String basicInfo) {
        this.basicInfo = basicInfo;
    }

    public Employee basicInfo(String basicInfo) {
        this.basicInfo = basicInfo;
        return this;
    }

    public Long getHireDate() {
        return hireDate;
    }

    public void setHireDate(Long hireDate) {
        this.hireDate = hireDate;
    }

    public Employee hireDate(Long hireDate) {
        this.hireDate = hireDate;
        return this;
    }

    public Double getProbationSalary() {
        return probationSalary;
    }

    public void setProbationSalary(Double probationSalary) {
        this.probationSalary = probationSalary;
    }

    public Employee probationSalary(Double probationSalary) {
        this.probationSalary = probationSalary;
        return this;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Employee salary(Double salary) {
        this.salary = salary;
        return this;
    }

    public Employee jobPositionStatus(JobPositionStatus jobPositionStatus) {
        this.jobPositionStatus = jobPositionStatus;
        return this;
    }

    public String getJobPositionInfo() {
        return jobPositionInfo;
    }

    public void setJobPositionInfo(String jobPositionInfo) {
        this.jobPositionInfo = jobPositionInfo;
    }

    public Employee jobPositionInfo(String jobPositionInfo) {
        this.jobPositionInfo = jobPositionInfo;
        return this;
    }

    public Long getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Long birthDate) {
        this.birthDate = birthDate;
    }

    public Employee birthDate(Long birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public Employee marriage(BooleanEnum marriage) {
        this.marriage = marriage;
        return this;
    }

    public Employee childbearing(BooleanEnum childbearing) {
        this.childbearing = childbearing;
        return this;
    }

    public String getHukou() {
        return hukou;
    }

    public void setHukou(String hukou) {
        this.hukou = hukou;
    }

    public Employee hukou(String hukou) {
        this.hukou = hukou;
        return this;
    }

    public String getPensionAccount() {
        return pensionAccount;
    }

    public void setPensionAccount(String pensionAccount) {
        this.pensionAccount = pensionAccount;
    }

    public Employee pensionAccount(String pensionAccount) {
        this.pensionAccount = pensionAccount;
        return this;
    }

    public String getHouseFoundAccount() {
        return houseFoundAccount;
    }

    public void setHouseFoundAccount(String houseFoundAccount) {
        this.houseFoundAccount = houseFoundAccount;
    }

    public Employee houseFoundAccount(String houseFoundAccount) {
        this.houseFoundAccount = houseFoundAccount;
        return this;
    }

    public Employee hasDriverLicense(BooleanEnum hasDriverLicense) {
        this.hasDriverLicense = hasDriverLicense;
        return this;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public Employee cellphone(String cellphone) {
        this.cellphone = cellphone;
        return this;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Employee mail(String mail) {
        this.mail = mail;
        return this;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public Employee qq(String qq) {
        this.qq = qq;
        return this;
    }

    public String getWorkQq() {
        return workQq;
    }

    public void setWorkQq(String workQq) {
        this.workQq = workQq;
    }

    public Employee workQq(String workQq) {
        this.workQq = workQq;
        return this;
    }

    public String getWorkQqPassword() {
        return workQqPassword;
    }

    public void setWorkQqPassword(String workQqPassword) {
        this.workQqPassword = workQqPassword;
    }

    public Employee workQqPassword(String workQqPassword) {
        this.workQqPassword = workQqPassword;
        return this;
    }

    public String getWechart() {
        return wechart;
    }

    public void setWechart(String wechart) {
        this.wechart = wechart;
    }

    public Employee wechart(String wechart) {
        this.wechart = wechart;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Employee address(String address) {
        this.address = address;
        return this;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Employee tel(String tel) {
        this.tel = tel;
        return this;
    }

    public String getEmergencyTel() {
        return emergencyTel;
    }

    public void setEmergencyTel(String emergencyTel) {
        this.emergencyTel = emergencyTel;
    }

    public Employee emergencyTel(String emergencyTel) {
        this.emergencyTel = emergencyTel;
        return this;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public Employee emergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
        return this;
    }

    public String getRelationshipWithEmergencyContact() {
        return relationshipWithEmergencyContact;
    }

    public void setRelationshipWithEmergencyContact(String relationshipWithEmergencyContact) {
        this.relationshipWithEmergencyContact = relationshipWithEmergencyContact;
    }

    public Employee relationshipWithEmergencyContact(String relationshipWithEmergencyContact) {
        this.relationshipWithEmergencyContact = relationshipWithEmergencyContact;
        return this;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public Employee additionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
        return this;
    }

    public String getComputerLevel() {
        return computerLevel;
    }

    public void setComputerLevel(String computerLevel) {
        this.computerLevel = computerLevel;
    }

    public Employee computerLevel(String computerLevel) {
        this.computerLevel = computerLevel;
        return this;
    }

    public String getForeignLanguageLevel() {
        return foreignLanguageLevel;
    }

    public void setForeignLanguageLevel(String foreignLanguageLevel) {
        this.foreignLanguageLevel = foreignLanguageLevel;
    }

    public Employee foreignLanguageLevel(String foreignLanguageLevel) {
        this.foreignLanguageLevel = foreignLanguageLevel;
        return this;
    }

    public Set<JobTitle> getJobTitles() {
        return jobTitles == null ? new HashSet<>() : jobTitles;
    }

    public void setJobTitles(Set<JobTitle> jobTitles) {
        this.jobTitles = jobTitles;
    }

    public Employee jobTitles(Set<JobTitle> jobTitles) {
        this.jobTitles = jobTitles;
        return this;
    }

    public Province getNativePlace() {
        return nativePlace;
    }

    public Employee nativePlace(Province province) {
        this.nativePlace = province;
        return this;
    }

    public Dept getDept() {
        return dept;
    }

    public void setDept(Dept dept) {
        this.dept = dept;
    }

    public boolean sameDept(Employee other) {
        return (dept == null && other.dept == null) || (dept != null && other.dept != null && dept.getId() == other.dept.getId());
    }

    public Employee dept(Dept dept) {
        this.dept = dept;
        return this;
    }

    public Set<Project> getAedProjects() {
        return aedProjects;
    }

    public void setAedProjects(Set<Project> projects) {
        this.aedProjects = projects;
    }

    public Employee aedProjects(Set<Project> projects) {
        this.aedProjects = projects;
        return this;
    }

    public Employee addAedProjects(Project project) {
        aedProjects.add(project);
        project.getAes().add(this);
        return this;
    }

    public Employee removeAedProjects(Project project) {
        aedProjects.remove(project);
        project.getAes().remove(this);
        return this;
    }

    public Set<Project> getParticipatedProjects() {
        return participatedProjects;
    }

    public void setParticipatedProjects(Set<Project> projects) {
        this.participatedProjects = projects;
    }

    public Employee participatedProjects(Set<Project> projects) {
        this.participatedProjects = projects;
        return this;
    }

    public Employee addParticipatedProjects(Project project) {
        participatedProjects.add(project);
        project.getMembers().add(this);
        return this;
    }

    public Employee removeParticipatedProjects(Project project) {
        participatedProjects.remove(project);
        project.getMembers().remove(this);
        return this;
    }

    public JobPositionStatus getJobPositionStatus() {
        return jobPositionStatus;
    }

    public void setJobPositionStatus(JobPositionStatus jobPositionStatus) {
        this.jobPositionStatus = jobPositionStatus;
    }

    public BooleanEnum getMarriage() {
        return marriage;
    }

    public void setMarriage(BooleanEnum marriage) {
        this.marriage = marriage;
    }

    public BooleanEnum getChildbearing() {
        return childbearing;
    }

    public void setChildbearing(BooleanEnum childbearing) {
        this.childbearing = childbearing;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public void setNativePlace(Province nativePlace) {
        this.nativePlace = nativePlace;
    }

    public boolean isGroundLevel() {
        return !Collections.isEmpty(jobTitles)
            && !jobTitles.stream().filter(t -> !Collections.isEmpty(t.getSubordinates())).findAny().isPresent();
    }

    public int getHighestLevel() {
        if (Collections.isEmpty(jobTitles)) {
            return Integer.MAX_VALUE;
        }

        return jobTitles.stream().mapToInt(t -> t.getLevel()).min().orElse(Integer.MAX_VALUE);
    }

    public boolean hasTitle(String title) {
        return jobTitles != null
            && jobTitles.stream()
            .filter(t -> t.getName().equalsIgnoreCase(title))
            .findAny()
            .isPresent();
    }

    public String getUniqueName() {
        return dept != null ? name + " - " + dept.getName() : name;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Employee employee = (Employee) o;
        if(employee.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, employee.id);
    }

    public PictureInfo getPhoto() {
        return photo;
    }

    public void setPhoto(PictureInfo photo) {
        this.photo = photo;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Employee{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", gender='" + gender + "'" +
            ", age='" + age + "'" +
            ", education='" + education + "'" +
            ", empNumber='" + empNumber + "'" +
            ", nativePlace='" + nativePlace + "'" +
            ", nationality='" + nationality + "'" +
            ", idNumber='" + idNumber + "'" +
            ", basicInfo='" + basicInfo + "'" +
            ", hireDate='" + hireDate + "'" +
            ", probationSalary='" + probationSalary + "'" +
            ", salary='" + salary + "'" +
            ", jobPositionStatus='" + jobPositionStatus + "'" +
            ", jobPositionInfo='" + jobPositionInfo + "'" +
            ", birthDate='" + birthDate + "'" +
            ", marriage='" + marriage + "'" +
            ", childbearing='" + childbearing + "'" +
            ", hukou='" + hukou + "'" +
            ", pensionAccount='" + pensionAccount + "'" +
            ", houseFoundAccount='" + houseFoundAccount + "'" +
            ", hasDriverLicense='" + hasDriverLicense + "'" +
            ", cellphone='" + cellphone + "'" +
            ", mail='" + mail + "'" +
            ", qq='" + qq + "'" +
            ", workQq='" + workQq + "'" +
            ", workQqPassword='" + workQqPassword + "'" +
            ", wechart='" + wechart + "'" +
            ", address='" + address + "'" +
            ", tel='" + tel + "'" +
            ", emergencyTel='" + emergencyTel + "'" +
            ", emergencyContact='" + emergencyContact + "'" +
            ", relationshipWithEmergencyContact='" + relationshipWithEmergencyContact + "'" +
            ", additionalInfo='" + additionalInfo + "'" +
            ", computerLevel='" + computerLevel + "'" +
            ", foreignLanguageLevel='" + foreignLanguageLevel + "'" +
            ", probationDeduction='" + probationDeduction + "'" +
            ", employeeDeduction='" + employeeDeduction + "'" +
            '}';
    }
}
