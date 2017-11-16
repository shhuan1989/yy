package com.yijia.yy.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the ProjectCost entity.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProjectCostDTO implements Serializable {

    private Long id;

    private Long createTime;

    private Double amount;

    private String info;

    private EmployeeDTO creator;

    private Long projectId;

    private DictionaryDTO category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public EmployeeDTO getCreator() {
        return creator;
    }

    public void setCreator(EmployeeDTO creator) {
        this.creator = creator;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public DictionaryDTO getCategory() {
        return category;
    }

    public void setCategory(DictionaryDTO category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProjectCostDTO projectCostDTO = (ProjectCostDTO) o;

        if ( ! Objects.equals(id, projectCostDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProjectCostDTO{" +
            "id=" + id +
            ", createTime='" + createTime + "'" +
            ", amount='" + amount + "'" +
            ", info='" + info + "'" +
            '}';
    }
}
