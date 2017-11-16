package com.yijia.yy.domain;


import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

@Entity
public class  PerformanceApprovalRequest extends ApprovalRequest {

    private static final long serialVersionUID = -4956642555748574936L;

    @OneToMany(cascade = CascadeType.ALL)
    private List<PerformanceBonus> bonuses;

    @ManyToOne
    @JsonManagedReference
    private Project project;

    @Column(name = "issued")
    private Boolean issued;

    @Column(name = "issue_time")
    private Long issueTime;

    @ManyToOne
    private Employee issuer;

    @Column
    private Float workType;

    @Column
    private Float productionDifficulty;

    public List<PerformanceBonus> getBonuses() {
        return bonuses;
    }

    public void setBonuses(List<PerformanceBonus> bonuses) {
        this.bonuses = bonuses;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Boolean getIssued() {
        return issued;
    }

    public void setIssued(Boolean issued) {
        this.issued = issued;
    }

    public Long getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(Long issueTime) {
        this.issueTime = issueTime;
    }

    public Employee getIssuer() {
        return issuer;
    }

    public void setIssuer(Employee issuer) {
        this.issuer = issuer;
    }

    public Float getWorkType() {
        return workType;
    }

    public void setWorkType(Float workType) {
        this.workType = workType;
    }

    public Float getProductionDifficulty() {
        return productionDifficulty;
    }

    public void setProductionDifficulty(Float productionDifficulty) {
        this.productionDifficulty = productionDifficulty;
    }
}
