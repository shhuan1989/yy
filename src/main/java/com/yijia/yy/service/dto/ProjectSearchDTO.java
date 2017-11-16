package com.yijia.yy.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yijia.yy.domain.converter.HasEnumConverter;
import com.yijia.yy.domain.enumeration.HasEnum;

/**
 * DTO for searching project
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProjectSearchDTO {
    String idNumber;
    String contractNumber;
    String name;
    String clientName;
    Integer contractStatusId;
    int[] statusIds;
    Integer statusId;
    Integer bonusStatusId;
    int[] bonusStatusIds;
    Long directorId;
    Long aeId;
    Boolean onlyActiveApproval;
    Boolean hasShootConfig;
    Boolean onlyOwned;
    Long memberId;
    Boolean hasShootconfig;

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Integer getContractStatusId() {
        return contractStatusId;
    }

    public void setContractStatusId(Integer contractStatusId) {
        this.contractStatusId = contractStatusId;
    }

    public Long getDirectorId() {
        return directorId;
    }

    public void setDirectorId(Long directorId) {
        this.directorId = directorId;
    }

    public Long getAeId() {
        return aeId;
    }

    public void setAeId(Long aeId) {
        this.aeId = aeId;
    }

    public HasEnum getHasContact() {
        if (this.contractStatusId == null) {
            return null;
        }

        return new HasEnumConverter().convertToEntityAttribute(this.contractStatusId);
    }

    public int[] getStatusIds() {
        return statusIds;
    }

    public void setStatusIds(int[] statusIds) {
        this.statusIds = statusIds;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getBonusStatusId() {
        return bonusStatusId;
    }

    public void setBonusStatusId(Integer bonusStatusId) {
        this.bonusStatusId = bonusStatusId;
    }

    public int[] getBonusStatusIds() {
        return bonusStatusIds;
    }

    public void setBonusStatusIds(int[] bonusStatusIds) {
        this.bonusStatusIds = bonusStatusIds;
    }

    public Boolean getOnlyActiveApproval() {
        return onlyActiveApproval;
    }

    public void setOnlyActiveApproval(Boolean onlyActiveApproval) {
        this.onlyActiveApproval = onlyActiveApproval;
    }

    public Boolean getHasShootConfig() {
        return hasShootConfig;
    }

    public void setHasShootConfig(Boolean hasShootConfig) {
        this.hasShootConfig = hasShootConfig;
    }

    public Boolean getOnlyOwned() {
        return onlyOwned;
    }

    public void setOnlyOwned(Boolean onlyOwned) {
        this.onlyOwned = onlyOwned;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Boolean getHasShootconfig() {
        return hasShootconfig;
    }

    public void setHasShootconfig(Boolean hasShootconfig) {
        this.hasShootconfig = hasShootconfig;
    }

    @Override
    public String toString() {
        return "ProjectSearchDTO{" +
            "idNumber='" + idNumber + '\'' +
            ", contractNumber='" + contractNumber + '\'' +
            ", name='" + name + '\'' +
            ", clientName='" + clientName + '\'' +
            ", contractStatusId=" + contractStatusId +
            ", directorId=" + directorId +
            ", aeId=" + aeId +
            '}';
    }
}
