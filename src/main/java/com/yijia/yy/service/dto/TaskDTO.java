package com.yijia.yy.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yijia.yy.domain.PictureInfo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the Task entity.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TaskDTO implements Serializable {

    private Long id;

    private String name;

    private String info;

    private Long createTime;

    private Long eta;

    private Set<EmployeeDTO> members = new HashSet<>();

    private Set<FileInfoDTO> attachments = new HashSet<>();

    private Set<PictureInfo> pictureInfos = new HashSet<>();

    private DictionaryDTO status;

    private EmployeeDTO creator;

    private EmployeeDTO owner;

    private Long projectId;

    private String projectName;

    private Integer order;

    private Long lastUpdateTime;

    private EmployeeDTO lastModifier;

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
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
    public Long getEta() {
        return eta;
    }

    public void setEta(Long eta) {
        this.eta = eta;
    }

    public Set<EmployeeDTO> getMembers() {
        return members;
    }

    public void setMembers(Set<EmployeeDTO> employees) {
        this.members = employees;
    }

    public Set<FileInfoDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<FileInfoDTO> fileInfos) {
        this.attachments = fileInfos;
    }

    public Set<PictureInfo> getPictureInfos() {
        return pictureInfos;
    }

    public void setPictureInfos(Set<PictureInfo> pictureInfos) {
        this.pictureInfos = pictureInfos;
    }

    public DictionaryDTO getStatus() {
        return status;
    }

    public void setStatus(DictionaryDTO status) {
        this.status = status;
    }

    public EmployeeDTO getCreator() {
        return creator;
    }

    public void setCreator(EmployeeDTO creator) {
        this.creator = creator;
    }

    public EmployeeDTO getOwner() {
        return owner;
    }

    public void setOwner(EmployeeDTO owner) {
        this.owner = owner;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TaskDTO taskDTO = (TaskDTO) o;

        if ( ! Objects.equals(id, taskDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TaskDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", info='" + info + "'" +
            ", createTime='" + createTime + "'" +
            ", eta='" + eta + "'" +
            '}';
    }
}
