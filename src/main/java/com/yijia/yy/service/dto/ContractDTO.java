package com.yijia.yy.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the Contract entity.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ContractDTO implements Serializable {

    private Long id;

    private String name;

    private String idNumber;

    private Long signTime;

    private Long createTime;

    private Long creatorId;

    private Long lastModifierId;

    private Long lastModifiedTime;

    private Set<FileInfoDTO> attachments;

    private Double moneyAmount;

    private String taxRate;

    private Double infoCost;

    private String tax;

    private ProjectDTO project;

    private List<ContractInstallmentDTO> installments;

    private DictionaryDTO paymentStatus;

    private Long nextInstallmentETA;

    private Double nextInstallmentAmount;

    private Double invoicedAmount;

    private Double notInvoicedAmount;

    private DictionaryDTO level;

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

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public Long getSignTime() {
        return signTime;
    }

    public void setSignTime(Long signTime) {
        this.signTime = signTime;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getLastModifierId() {
        return lastModifierId;
    }

    public void setLastModifierId(Long lastModifierId) {
        this.lastModifierId = lastModifierId;
    }

    public Long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public Set<FileInfoDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<FileInfoDTO> attachments) {
        this.attachments = attachments;
    }

    public Double getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(Double moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public Double getInfoCost() {
        return infoCost;
    }

    public void setInfoCost(Double infoCost) {
        this.infoCost = infoCost;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public ProjectDTO getProject() {
        return project;
    }

    public void setProject(ProjectDTO project) {
        this.project = project;
    }

    public List<ContractInstallmentDTO> getInstallments() {
        return installments;
    }


    public void setInstallments(List<ContractInstallmentDTO> installments) {
        this.installments = installments;
    }

    public Long getNextInstallmentETA() {
        return nextInstallmentETA;
    }

    public void setNextInstallmentETA(Long nextInstallmentETA) {
        this.nextInstallmentETA = nextInstallmentETA;
    }

    public DictionaryDTO getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(DictionaryDTO paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Double getNextInstallmentAmount() {
        return nextInstallmentAmount;
    }

    public void setNextInstallmentAmount(Double nextInstallmentAmount) {
        this.nextInstallmentAmount = nextInstallmentAmount;
    }

    public Double getInvoicedAmount() {
        return invoicedAmount;
    }

    public void setInvoicedAmount(Double invoicedAmount) {
        this.invoicedAmount = invoicedAmount;
    }

    public Double getNotInvoicedAmount() {
        return notInvoicedAmount;
    }

    public void setNotInvoicedAmount(Double notInvoicedAmount) {
        this.notInvoicedAmount = notInvoicedAmount;
    }

    public DictionaryDTO getLevel() {
        return level;
    }

    public void setLevel(DictionaryDTO level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ContractDTO contractDTO = (ContractDTO) o;

        if ( ! Objects.equals(id, contractDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ContractDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
