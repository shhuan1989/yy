package com.yijia.yy.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A ShootConfig.
 */
@Entity
public class ShootConfig extends ApprovalItem implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final Integer TYPE_CONFIG = 0;

    public static final Integer TYPE_BUDGET = 1;

    public static final Integer TYPE_COST = 2;

    public static final Integer TYPE_COST_FOR_AUDIT = 3;

    @OneToMany(mappedBy = "shootConfig", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ShootConfigItem> configItems = new ArrayList<>();

    @Column(name = "shoot_config_budget")
    private Double budget;

    @Column(name = "shoot_cost")
    private Double actualCost;

    @Column(name = "shoot_config_type")
    private Integer type; // 0 means config, 1 means budget, 2 means cost

    @Column(name = "shoot_config_cid", length = 300, unique = true)
    private String configCorrelationId; // same shoot config but different type share same configCorrelationId

    @Column
    private Long lastModifiedTime;

    @ManyToOne
    private Project project;

    public List<ShootConfigItem> getConfigItems() {
        return configItems;
    }

    public ShootConfig configItems(List<ShootConfigItem> shootConfigItems) {
        this.configItems = shootConfigItems;
        return this;
    }

    public ShootConfig addConfigItems(ShootConfigItem shootConfigItem) {
        configItems.add(shootConfigItem);
        shootConfigItem.setShootConfig(this);
        return this;
    }

    public ShootConfig removeConfigItems(ShootConfigItem shootConfigItem) {
        configItems.remove(shootConfigItem);
        shootConfigItem.setShootConfig(null);
        return this;
    }

    public void setConfigItems(List<ShootConfigItem> shootConfigItems) {
        this.configItems = shootConfigItems;
    }

    public Project getProject() {
        return project;
    }

    public ShootConfig project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getActualCost() {
        return actualCost;
    }

    public void setActualCost(Double actualCost) {
        this.actualCost = actualCost;
    }

    public String getConfigCorrelationId() {
        return configCorrelationId;
    }

    public void setConfigCorrelationId(String configCorrelationId) {
        this.configCorrelationId = configCorrelationId;
    }

    public Long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ShootConfig shootConfig = (ShootConfig) o;
        if(shootConfig.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), shootConfig.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ShootConfig{" +
            "id=" + getId() +
            '}';
    }
}
