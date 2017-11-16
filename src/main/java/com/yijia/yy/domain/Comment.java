package com.yijia.yy.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;

/**
 * A Comment.
 */
@Entity
@Table(name = "comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "text", length = 4000)
    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    private Employee creator;

    @Column(name = "create_time")
    private Long createTime;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(unique = true)
    private PictureInfo pictureInfo;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(unique = true)
    private FileInfo fileInfo;

    @ManyToOne(cascade = CascadeType.REFRESH)
    private Task task;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public Comment text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Employee getCreator() {
        return creator;
    }

    public Comment creator(Employee creator) {
        this.creator = creator;
        return this;
    }

    public void setCreator(Employee creator) {
        this.creator = creator;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public Comment createTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public PictureInfo getPictureInfo() {
        return pictureInfo;
    }

    public Comment pictureInfo(PictureInfo pictureInfo) {
        this.pictureInfo = pictureInfo;
        return this;
    }

    public void setPictureInfo(PictureInfo pictureInfo) {
        this.pictureInfo = pictureInfo;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public Comment fileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
        return this;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Comment comment = (Comment) o;
        if(comment.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Comment{" +
            "id=" + id +
            ", text='" + text + "'" +
            ", creator='" + creator + "'" +
            ", createTime='" + createTime + "'" +
            '}';
    }
}
