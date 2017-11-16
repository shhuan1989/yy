package com.yijia.yy.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * A DTO for the Dictionary entity.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DictionaryDTO implements Serializable {

    private Long id;

    private String name;

    private String creator;

    private Long createTime;

    private String comment;

    private Boolean isSystem;

    private Long lastModifiedTime;

    private String lastModifier;

    private Long parentId;

    private List<DictionaryDTO> children = new ArrayList<>();

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
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    public Boolean getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(Boolean isSystem) {
        this.isSystem = isSystem;
    }
    public Long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }
    public String getLastModifier() {
        return lastModifier;
    }

    public void setLastModifier(String lastModifier) {
        this.lastModifier = lastModifier;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long dictionaryId) {
        this.parentId = dictionaryId;
    }

    public List<DictionaryDTO> getChildren() {
        return children;
    }

    public void setChildren(List<DictionaryDTO> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DictionaryDTO that = (DictionaryDTO) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return parentId != null ? parentId.equals(that.parentId) : that.parentId == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DictionaryDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", creator='" + creator + "'" +
            ", createTime='" + createTime + "'" +
            ", comment='" + comment + "'" +
            ", isSystem='" + isSystem + "'" +
            ", lastModifiedTime='" + lastModifiedTime + "'" +
            ", lastModifier='" + lastModifier + "'" +
            '}';
    }

    public DictionaryDTO withId(Long id) {
        this.id = id;
        return this;
    }

    public DictionaryDTO withName(String name) {
        this.name = name;
        return this;
    }
}
