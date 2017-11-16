package com.yijia.yy.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yijia.yy.domain.Employee;

/**
 * A DTO for the Comment entity.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CommentDTO implements Serializable {

    private Long id;

    private String text;

    private EmployeeDTO creator;

    private Long createTime;

    private Long taskId;

    private PictureInfoDTO pictureInfo;

    private FileInfoDTO fileInfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    public EmployeeDTO getCreator() {
        return creator;
    }

    public void setCreator(EmployeeDTO creator) {
        this.creator = creator;
    }
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public PictureInfoDTO getPictureInfo() {
        return pictureInfo;
    }

    public void setPictureInfo(PictureInfoDTO pictureInfo) {
        this.pictureInfo = pictureInfo;
    }

    public FileInfoDTO getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfoDTO fileInfo) {
        this.fileInfo = fileInfo;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CommentDTO commentDTO = (CommentDTO) o;

        if ( ! Objects.equals(id, commentDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CommentDTO{" +
            "id=" + id +
            ", text='" + text + "'" +
            ", creator='" + creator + "'" +
            ", createTime='" + createTime + "'" +
            '}';
    }
}
