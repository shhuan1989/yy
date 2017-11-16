package com.yijia.yy.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the DirectorNeedsComment entity.
 */
public class DirectorNeedsCommentDTO implements Serializable {

    private Long id;

    private String content;

    private Long createTime;

    private Long directorNeedsId;

    private EmployeeDTO creator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getDirectorNeedsId() {
        return directorNeedsId;
    }

    public void setDirectorNeedsId(Long directorNeedsId) {
        this.directorNeedsId = directorNeedsId;
    }

    public EmployeeDTO getCreator() {
        return creator;
    }

    public void setCreator(EmployeeDTO creator) {
        this.creator = creator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DirectorNeedsCommentDTO directorNeedsCommentDTO = (DirectorNeedsCommentDTO) o;

        if ( ! Objects.equals(id, directorNeedsCommentDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DirectorNeedsCommentDTO{" +
            "id=" + id +
            ", content='" + content + "'" +
            ", createTime='" + createTime + "'" +
            '}';
    }
}
