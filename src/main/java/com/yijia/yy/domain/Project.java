package com.yijia.yy.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.yijia.yy.domain.converter.*;
import com.yijia.yy.domain.enumeration.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A Project.
 */
@Entity
@Table(name = "project")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "no")
    private String no;

    @Column(name = "name")
    private String name;

    @Column(unique = true)
    private String idNumber;

    @Column
    @Convert(converter = ProjectStatusConverter.class)
    private ProjectStatus status;

    @Column
    @Convert(converter = ProjectBonusStatusConverter.class)
    private ProjectBonusStatus bonusStatus;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REFRESH)
    @JsonBackReference
    private Set<PerformanceApprovalRequest> bonusApprovals;

    @Column
    @Convert(converter = ProjectRateStatusConverter.class)
    private ProjectRateStatus rateStatus;

    @Column
    @Convert(converter = HasEnumConverter.class)
    private HasEnum hasContract;

    @Column(name = "is_new_client")
    @Convert(converter = BooleanEnumConverter.class)
    private BooleanEnum isNewClient;

    @Column(name = "budget")
    private String budget;

    @Column(name = "negotiator")
    private String negotiator;

    @Column(name = "tel_negotiator")
    private String telNegotiator;

    @Column(name = "qq_negotiator")
    private String qqNegotiator;

    @Column(name = "wechart_negotiator")
    private String wechartNegotiator;

    @Column(name = "mail_negotiator")
    private String mailNegotiator;

    @Column(name = "description", length = 4000)
    private String description;

    @Column
    private Boolean isHuaWei = false;

    @Column
    private Float progress;

    @ManyToOne(cascade = CascadeType.REFRESH)
    private Employee creator;

    @Column
    private Long createTime;

    @Column
    private Long lastUpdateTime;

    @ManyToOne
    private Employee lastModifier;

    @Column
    private Long archiveTime;

    @ManyToOne
    private Client client;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(unique = true)
    @JsonManagedReference
    private Contract contract;

    @ManyToOne(cascade = CascadeType.REFRESH)
    private Employee director;

    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "project_aes",
               joinColumns = @JoinColumn(name="projects_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="aes_id", referencedColumnName="ID"))
    private Set<Employee> aes = new HashSet<>();

    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "project_members",
               joinColumns = @JoinColumn(name="projects_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="members_id", referencedColumnName="ID"))
    private Set<Employee> members = new HashSet<>();

    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "project_viewers",
        joinColumns = @JoinColumn(name="projects_id", referencedColumnName="ID"),
        inverseJoinColumns = @JoinColumn(name="viewer_id", referencedColumnName="ID"))
    private Set<Employee> viewers = new HashSet<>();

    @Column
    private Integer sortOrder;

    // all project rates are done
    @Column
    private Boolean allRatesDone;

    @Column(length = 500)
    private String workLink;

    @Column
    private Boolean rateClosed;

    @Transient
    private Set<Employee> firstLevelRateMembers;

    @Transient
    private Set<Employee> secondLevelRateMembers;

    @Transient
    private Boolean allTaskDone;

    @OneToMany(mappedBy = "project")
    @JsonIgnore
    private Set<ShootConfig> shootConfigs;

    @Column
    private Integer progressStatus;

    @Column
    private Double totalBonus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getNo() {
        return no;
    }

    public Project no(String no) {
        this.no = no;
        return this;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public Project name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HasEnum getHasContract() {
        return hasContract;
    }

    public void setHasContract(HasEnum hasContract) {
        this.hasContract = hasContract;
    }

    public Project hasContract(HasEnum hasContract) {
        this.hasContract = hasContract;
        return this;
    }

    public BooleanEnum getIsNewClient() {
        return isNewClient;
    }

    public void setIsNewClient(BooleanEnum isNewClient) {
        this.isNewClient = isNewClient;
    }

    public Project isNewClient(BooleanEnum isNewClient) {
        this.isNewClient = isNewClient;
        return this;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public Project budget(String budget) {
        this.budget = budget;
        return this;
    }

    public String getNegotiator() {
        return negotiator;
    }

    public Project negotiator(String negotiator) {
        this.negotiator = negotiator;
        return this;
    }

    public void setNegotiator(String negotiator) {
        this.negotiator = negotiator;
    }

    public String getTelNegotiator() {
        return telNegotiator;
    }

    public Project telNegotiator(String telNegotiator) {
        this.telNegotiator = telNegotiator;
        return this;
    }

    public void setTelNegotiator(String telNegotiator) {
        this.telNegotiator = telNegotiator;
    }

    public String getQqNegotiator() {
        return qqNegotiator;
    }

    public Project qqNegotiator(String qqNegotiator) {
        this.qqNegotiator = qqNegotiator;
        return this;
    }

    public void setQqNegotiator(String qqNegotiator) {
        this.qqNegotiator = qqNegotiator;
    }

    public String getWechartNegotiator() {
        return wechartNegotiator;
    }

    public Project wechartNegotiator(String wechartNegotiator) {
        this.wechartNegotiator = wechartNegotiator;
        return this;
    }

    public void setWechartNegotiator(String wechartNegotiator) {
        this.wechartNegotiator = wechartNegotiator;
    }

    public String getMailNegotiator() {
        return mailNegotiator;
    }

    public Project mailNegotiator(String mailNegotiator) {
        this.mailNegotiator = mailNegotiator;
        return this;
    }

    public void setMailNegotiator(String mailNegotiator) {
        this.mailNegotiator = mailNegotiator;
    }

    public String getDescription() {
        return description;
    }

    public Project description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Client getClient() {
        return client;
    }

    public Project client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Contract getContract() {
        return contract;
    }

    public Project contract(Contract contract) {
        this.contract = contract;
        return this;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Employee getDirector() {
        return director;
    }

    public Project director(Employee employee) {
        this.director = employee;
        return this;
    }

    public void setDirector(Employee employee) {
        this.director = employee;
    }

    public Set<Employee> getAes() {
        return aes;
    }

    public Project aes(Set<Employee> employees) {
        this.aes = employees;
        return this;
    }

    public Project addAes(Employee employee) {
        aes.add(employee);
        employee.getAedProjects().add(this);
        return this;
    }

    public Project removeAes(Employee employee) {
        aes.remove(employee);
        employee.getAedProjects().remove(this);
        return this;
    }

    public void setAes(Set<Employee> employees) {
        this.aes = employees;
    }

    public Set<Employee> getMembers() {
        return members;
    }

    public Set<Employee> getFirstLevelRateMembers() {
        return firstLevelRateMembers;
    }

    public void setFirstLevelRateMembers(Set<Employee> firstLevelRateMembers) {
        this.firstLevelRateMembers = firstLevelRateMembers;
    }

    public Set<Employee> getSecondLevelRateMembers() {
        return secondLevelRateMembers;
    }

    public void setSecondLevelRateMembers(Set<Employee> secondLevelRateMembers) {
        this.secondLevelRateMembers = secondLevelRateMembers;
    }

    public ProjectBonusStatus getBonusStatus() {
        return bonusStatus;
    }

    public void setBonusStatus(ProjectBonusStatus bonusStatus) {
        this.bonusStatus = bonusStatus;
    }

    public Set<PerformanceApprovalRequest> getBonusApprovals() {
        return bonusApprovals;
    }

    public void setBonusApprovals(Set<PerformanceApprovalRequest> bonusApprovals) {
        this.bonusApprovals = bonusApprovals;
    }

    public Boolean getHuaWei() {
        return isHuaWei;
    }

    public Boolean isHuaWei() {
        return isHuaWei;
    }

    public void setHuaWei(Boolean huaWei) {
        isHuaWei = huaWei;
    }

    public Set<ShootConfig> getShootConfigs() {
        return shootConfigs;
    }

    public void setShootConfigs(Set<ShootConfig> shootConfigs) {
        this.shootConfigs = shootConfigs;
    }

    public Boolean getAllTaskDone() {
        return allTaskDone;
    }

    public void setAllTaskDone(Boolean allTaskDone) {
        this.allTaskDone = allTaskDone;
    }

    public Set<Employee> getViewers() {
        return viewers;
    }

    public void setViewers(Set<Employee> viewers) {
        this.viewers = viewers;
    }

    public Boolean getAllRatesDone() {
        return allRatesDone;
    }

    public void setAllRatesDone(Boolean allRatesDone) {
        this.allRatesDone = allRatesDone;
    }

    public Integer getProgressStatus() {
        return progressStatus;
    }

    public void setProgressStatus(Integer progressStatus) {
        this.progressStatus = progressStatus;
    }

    public Boolean getRateClosed() {
        return rateClosed;
    }

    public void setRateClosed(Boolean rateClosed) {
        this.rateClosed = rateClosed;
    }

    public Double getTotalBonus() {
        return totalBonus;
    }

    public void setTotalBonus(Double totalBonus) {
        this.totalBonus = totalBonus;
    }

    /**
     * 获取底层员工，即没有下属的员工
     * @return
     */
    public Set<Employee> getGroundLevelMembers() {
        if (members == null) {
            return new HashSet<>();
        }

        return getAllMembers().stream()
            .filter(Employee::isGroundLevel)
            .collect(Collectors.toSet());
    }

    public Project members(Set<Employee> employees) {
        this.members = employees;
        return this;
    }

    /**
     *
     * @return members include director and aes
     */
    public Set<Employee> getAllMembers() {
        Set<Employee> result = new HashSet<>();
        if (director != null) {
            result.add(director);
        }
        if (aes != null) {
            result.addAll(aes);
        }

        if (members != null) {
            result.addAll(members);
        }

        return result;
    }

    public Project addMembers(Employee employee) {
        members.add(employee);
        employee.getParticipatedProjects().add(this);
        return this;
    }

    public Project removeMembers(Employee employee) {
        members.remove(employee);
        employee.getParticipatedProjects().remove(this);
        return this;
    }

    public void setMembers(Set<Employee> employees) {
        this.members = employees;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public Employee getCreator() {
        return creator;
    }

    public void setCreator(Employee creator) {
        this.creator = creator;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public float getProgress() {
        return progress == null ? 0 : progress;
    }

    public void setProgress(Float progress) {
        this.progress = progress;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Employee getLastModifier() {
        return lastModifier;
    }

    public void setLastModifier(Employee lastModifier) {
        this.lastModifier = lastModifier;
    }

    public String getWorkLink() {
        return workLink;
    }

    public void setWorkLink(String workLink) {
        this.workLink = workLink;
    }

    public Long getArchiveTime() {
        return archiveTime;
    }

    public void setArchiveTime(Long archiveTime) {
        this.archiveTime = archiveTime;
    }

    public ProjectRateStatus getRateStatus() {
        return rateStatus;
    }

    public void setRateStatus(ProjectRateStatus rateStatus) {
        this.rateStatus = rateStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Project project = (Project) o;
        if(project.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, project.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Project{" +
            "id=" + id +
            ", no='" + no + "'" +
            ", name='" + name + "'" +
            ", hasContract='" + hasContract + "'" +
            ", isNewClient='" + isNewClient + "'" +
            ", budget='" + budget + "'" +
            ", negotiator='" + negotiator + "'" +
            ", telNegotiator='" + telNegotiator + "'" +
            ", qqNegotiator='" + qqNegotiator + "'" +
            ", wechartNegotiator='" + wechartNegotiator + "'" +
            ", mailNegotiator='" + mailNegotiator + "'" +
            ", description='" + description + "'" +
            '}';
    }

    public Project id(Long id) {
        this.id = id;
        return this;
    }
}
