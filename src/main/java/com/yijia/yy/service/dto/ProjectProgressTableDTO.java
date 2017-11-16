package com.yijia.yy.service.dto;

import com.yijia.yy.domain.ProjectProgressItem;

import java.io.Serializable;
import java.util.*;


/**
 * A DTO for the ProjectProgressTable entity.
 */
public class ProjectProgressTableDTO implements Serializable {

    private Long id;

    private Long createTime;

    private String memo;

    private Long projectId;

    private Long creatorId;

    private List<ProjectProgressItemDTO> items = new ArrayList<>();

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
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long employeeId) {
        this.creatorId = employeeId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public List<ProjectProgressItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ProjectProgressItemDTO> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProjectProgressTableDTO projectProgressTableDTO = (ProjectProgressTableDTO) o;

        if ( ! Objects.equals(id, projectProgressTableDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProjectProgressTableDTO{" +
            "id=" + id +
            ", createTime='" + createTime + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
