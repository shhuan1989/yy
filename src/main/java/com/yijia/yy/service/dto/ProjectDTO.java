package com.yijia.yy.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yijia.yy.domain.Employee;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the Project entity.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProjectDTO implements Serializable {

    private Long id;

    private String no;

    private String name;

    private DictionaryDTO hasContract;

    private DictionaryDTO isNewClient;

    private DictionaryDTO status;

    private DictionaryDTO rateStatus;

    private String budget;

    private String negotiator;

    private String telNegotiator;

    private String qqNegotiator;

    private String wechartNegotiator;

    private String mailNegotiator;

    private String description;

    private String idNumber;

    private ClientDTO client;

    private ContractDTO contract;

    private EmployeeDTO director;

    private String creatorName;

    private Long creatorId;

    private Long createTime;

    private Long archiveTime;

    private float progress;

    private Integer sortOrder;

    private Long lastUpdateTime;

    private Set<EmployeeDTO> aes = new HashSet<>();

    private Set<EmployeeDTO> members = new HashSet<>();

    private EmployeeDTO lastModifier;

    private double cost;

    private String workLink;

    private Integer participantsCount;

    private Set<EmployeeDTO> secondLevelRateMembers;

    private Set<EmployeeDTO> firstLevelRateMembers;

    private Set<EmployeeDTO> viewers;

    private DictionaryDTO bonusStatus;

    private Set<PerformanceApprovalRequestDTO> bonusApprovals;

    private Boolean isHuaWei;

    private Boolean bonusIssued;

    private Boolean allTaskDone;

    private Integer progressStatus;

    private Boolean allRatesDone;

    private Boolean rateClosed;

    private Double totalBonus;

    private Double bonusPool;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DictionaryDTO getHasContract() {
        return hasContract;
    }

    public void setHasContract(DictionaryDTO hasContract) {
        this.hasContract = hasContract;
    }

    public DictionaryDTO getIsNewClient() {
        return isNewClient;
    }

    public void setIsNewClient(DictionaryDTO isNewClient) {
        this.isNewClient = isNewClient;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getNegotiator() {
        return negotiator;
    }

    public void setNegotiator(String negotiator) {
        this.negotiator = negotiator;
    }
    public String getTelNegotiator() {
        return telNegotiator;
    }

    public void setTelNegotiator(String telNegotiator) {
        this.telNegotiator = telNegotiator;
    }
    public String getQqNegotiator() {
        return qqNegotiator;
    }

    public void setQqNegotiator(String qqNegotiator) {
        this.qqNegotiator = qqNegotiator;
    }
    public String getWechartNegotiator() {
        return wechartNegotiator;
    }

    public void setWechartNegotiator(String wechartNegotiator) {
        this.wechartNegotiator = wechartNegotiator;
    }
    public String getMailNegotiator() {
        return mailNegotiator;
    }

    public void setMailNegotiator(String mailNegotiator) {
        this.mailNegotiator = mailNegotiator;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public ContractDTO getContract() {
        return contract;
    }

    public void setContract(ContractDTO contract) {
        this.contract = contract;
    }

    public EmployeeDTO getDirector() {
        return director;
    }

    public void setDirector(EmployeeDTO director) {
        this.director = director;
    }

    public Set<EmployeeDTO> getAes() {
        return aes;
    }

    public void setAes(Set<EmployeeDTO> employees) {
        this.aes = employees;
    }

    public Set<EmployeeDTO> getMembers() {
        return members;
    }

    public void setMembers(Set<EmployeeDTO> employees) {
        this.members = employees;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public DictionaryDTO getStatus() {
        return status;
    }

    public void setStatus(DictionaryDTO status) {
        this.status = status;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Long getArchiveTime() {
        return archiveTime;
    }

    public void setArchiveTime(Long archiveTime) {
        this.archiveTime = archiveTime;
    }

    public Long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public EmployeeDTO getLastModifier() {
        return lastModifier;
    }

    public void setLastModifier(EmployeeDTO lastModifier) {
        this.lastModifier = lastModifier;
    }

    public String getWorkLink() {
        return workLink;
    }

    public void setWorkLink(String workLink) {
        this.workLink = workLink;
    }

    public DictionaryDTO getRateStatus() {
        return rateStatus;
    }

    public void setRateStatus(DictionaryDTO rateStatus) {
        this.rateStatus = rateStatus;
    }

    public Integer getParticipantsCount() {
        return participantsCount;
    }

    public void setParticipantsCount(Integer participantsCount) {
        this.participantsCount = participantsCount;
    }

    public Set<EmployeeDTO> getSecondLevelRateMembers() {
        return secondLevelRateMembers;
    }

    public void setSecondLevelRateMembers(Set<EmployeeDTO> secondLevelRateMembers) {
        this.secondLevelRateMembers = secondLevelRateMembers;
    }

    public Set<EmployeeDTO> getFirstLevelRateMembers() {
        return firstLevelRateMembers;
    }

    public void setFirstLevelRateMembers(Set<EmployeeDTO> firstLevelRateMembers) {
        this.firstLevelRateMembers = firstLevelRateMembers;
    }

    public Set<EmployeeDTO> getViewers() {
        return viewers;
    }

    public void setViewers(Set<EmployeeDTO> viewers) {
        this.viewers = viewers;
    }

    public DictionaryDTO getBonusStatus() {
        return bonusStatus;
    }

    public void setBonusStatus(DictionaryDTO bonusStatus) {
        this.bonusStatus = bonusStatus;
    }

    public Set<PerformanceApprovalRequestDTO> getBonusApprovals() {
        return bonusApprovals;
    }

    public void setBonusApprovals(Set<PerformanceApprovalRequestDTO> bonusApprovals) {
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

    public Boolean getBonusIssued() {
        return bonusIssued;
    }

    public void setBonusIssued(Boolean bonusIssued) {
        this.bonusIssued = bonusIssued;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Boolean getAllTaskDone() {
        return allTaskDone;
    }

    public void setAllTaskDone(Boolean allTaskDone) {
        this.allTaskDone = allTaskDone;
    }

    public Boolean getAllRatesDone() {
        return allRatesDone;
    }

    public void setAllRatesDone(Boolean allRatesDone) {
        this.allRatesDone = allRatesDone;
    }

    public Set<EmployeeDTO> getAllMembers() {
        Set<EmployeeDTO> result = new HashSet<>();
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

    public Double getBonusPool() {
        return bonusPool;
    }

    public void setBonusPool(Double bonusPool) {
        this.bonusPool = bonusPool;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProjectDTO projectDTO = (ProjectDTO) o;

        if ( ! Objects.equals(id, projectDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProjectDTO{" +
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
}
