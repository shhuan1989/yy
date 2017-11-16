package com.yijia.yy.service.dto;

import com.yijia.yy.domain.PictureInfo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the NoticeChat entity.
 */
public class NoticeChatDTO implements Serializable {

    private Long id;

    private String text;

    private Long createTime;

    private EmployeeDTO creator;

    private Long noticeId;

    private FileInfoDTO fileInfo;

    private PictureInfoDTO pictureInfo;

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
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public EmployeeDTO getCreator() {
        return creator;
    }

    public void setCreator(EmployeeDTO creator) {
        this.creator = creator;
    }

    public Long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
    }

    public FileInfoDTO getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfoDTO fileInfo) {
        this.fileInfo = fileInfo;
    }

    public PictureInfoDTO getPictureInfo() {
        return pictureInfo;
    }

    public void setPictureInfo(PictureInfoDTO pictureInfo) {
        this.pictureInfo = pictureInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NoticeChatDTO noticeChatDTO = (NoticeChatDTO) o;

        if ( ! Objects.equals(id, noticeChatDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "NoticeChatDTO{" +
            "id=" + id +
            ", text='" + text + "'" +
            ", createTime='" + createTime + "'" +
            '}';
    }
}
