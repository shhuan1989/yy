package com.yijia.yy.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A NoticeChat.
 */
@Entity
@Table(name = "notice_chat")
public class NoticeChat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "text", length = 4000)
    private String text;

    @Column(name = "create_time")
    private Long createTime;

    @ManyToOne(fetch = FetchType.EAGER)
    private Employee creator;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(unique = true)
    private PictureInfo pictureInfo;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(unique = true)
    private FileInfo fileInfo;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JsonBackReference
    private Notice notice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public NoticeChat text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public NoticeChat createTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Employee getCreator() {
        return creator;
    }

    public NoticeChat creator(Employee employee) {
        this.creator = employee;
        return this;
    }

    public void setCreator(Employee employee) {
        this.creator = employee;
    }

    public PictureInfo getPictureInfo() {
        return pictureInfo;
    }

    public NoticeChat pictureInfo(PictureInfo pictureInfo) {
        this.pictureInfo = pictureInfo;
        return this;
    }

    public void setPictureInfo(PictureInfo pictureInfo) {
        this.pictureInfo = pictureInfo;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public NoticeChat fileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
        return this;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    public Notice getNotice() {
        return notice;
    }

    public NoticeChat notice(Notice notice) {
        this.notice = notice;
        return this;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NoticeChat noticeChat = (NoticeChat) o;
        if(noticeChat.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, noticeChat.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "NoticeChat{" +
            "id=" + id +
            ", text='" + text + "'" +
            ", createTime='" + createTime + "'" +
            '}';
    }
}
