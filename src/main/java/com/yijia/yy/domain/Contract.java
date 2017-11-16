package com.yijia.yy.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.yijia.yy.domain.converter.ContractLevelConverter;
import com.yijia.yy.domain.converter.ContractPaymentStatusConverter;
import com.yijia.yy.domain.enumeration.ContractLevel;
import com.yijia.yy.domain.enumeration.ContractPaymentStatus;

import javax.jdo.annotations.Unique;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

/**
 * A Contract.
 */
@Entity
@Table(name = "contract")
public class Contract implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(unique = true)
    @NotNull
    @Unique
    private String idNumber;

    @Column
    private Long signTime;

    @Column
    private Long createTime;

    @ManyToOne
    private Employee creator;

    @ManyToOne
    private Employee lastModifier;

    @Column
    private Long lastModifiedTime;

    @Column
    @NotNull
    private Double moneyAmount;

    @Column
    private String taxRate;

    @Column
    private String tax;

    @Column
    private Double infoCost;

    @Column
    @Convert(converter = ContractPaymentStatusConverter.class)
    private ContractPaymentStatus paymentStatus;

    @Column
    private Long nextInstallmentETA;

    @Column
    private Double invoicedAmount;

    @Column
    @Convert(converter = ContractLevelConverter.class)
    private ContractLevel level;

    @OneToMany
    @JsonIgnore
    private Set<FileInfo> attachments = new HashSet<>();

    @OneToOne(mappedBy = "contract", cascade = CascadeType.REFRESH)
    @JsonBackReference
    private Project project;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "contract")
    @JsonManagedReference
    private List<ContractInstallment> installments = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Contract name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<FileInfo> getAttachments() {
        return attachments;
    }

    public Contract attachments(Set<FileInfo> fileInfos) {
        this.attachments = fileInfos;
        return this;
    }

    public Contract addAttachments(FileInfo fileInfo) {
        attachments.add(fileInfo);
        return this;
    }

    public Contract removeAttachments(FileInfo fileInfo) {
        attachments.remove(fileInfo);
        return this;
    }

    public void setAttachments(Set<FileInfo> fileInfos) {
        this.attachments = fileInfos;
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

    public Employee getCreator() {
        return creator;
    }

    public void setCreator(Employee creator) {
        this.creator = creator;
    }

    public Employee getLastModifier() {
        return lastModifier;
    }

    public void setLastModifier(Employee lastModifier) {
        this.lastModifier = lastModifier;
    }

    public Long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
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

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public Double getInfoCost() {
        return infoCost;
    }

    public void setInfoCost(Double infoCost) {
        this.infoCost = infoCost;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<ContractInstallment> getInstallments() {
        return installments;
    }

    public void setInstallments(List<ContractInstallment> installments) {
        this.installments = installments;
    }

    public ContractPaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(ContractPaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Long getNextInstallmentETA() {
        return nextInstallmentETA;
    }

    public void setNextInstallmentETA(Long nextInstallmentETA) {
        this.nextInstallmentETA = nextInstallmentETA;
    }

    public Double getInvoicedAmount() {
        return invoicedAmount;
    }

    public void setInvoicedAmount(Double invoicedAmount) {
        this.invoicedAmount = invoicedAmount;
    }

    public ContractLevel getLevel() {
        if (level == null) {
            return ContractLevel.LEVEL_1;
        }
        return level;
    }

    public void setLevel(ContractLevel level) {
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
        Contract contract = (Contract) o;
        if(contract.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, contract.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Contract{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
