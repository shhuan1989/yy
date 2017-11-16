package com.yijia.yy.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the DirectorNeeds entity.
 */
public class DirectorNeedsDTO implements Serializable {

    private Long id;

    private Long projectId;

    private String projectIdNumber;

    private String projectName;

    private Long createTime;

    private Long creatorId;

    private String creatorName;

    private List<DirectorNeedsItemDTO> items;

    private List<DirectorNeedsCommentDTO> comments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
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

    public List<DirectorNeedsCommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<DirectorNeedsCommentDTO> comments) {
        this.comments = comments;
    }

    public List<DirectorNeedsItemDTO> getItems() {
        return items;
    }

    public void setItems(List<DirectorNeedsItemDTO> items) {
        this.items = items;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getProjectIdNumber() {
        return projectIdNumber;
    }

    public void setProjectIdNumber(String projectIdNumber) {
        this.projectIdNumber = projectIdNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DirectorNeedsDTO directorNeedsDTO = (DirectorNeedsDTO) o;

        if ( ! Objects.equals(id, directorNeedsDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DirectorNeedsDTO{" +
            "id=" + id +
            '}';
    }
}
