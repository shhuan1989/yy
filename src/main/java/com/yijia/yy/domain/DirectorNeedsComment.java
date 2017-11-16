package com.yijia.yy.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DirectorNeedsComment.
 */
@Entity
@Table(name = "director_needs_comment")
public class DirectorNeedsComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "create_time")
    private Long createTime;

    @ManyToOne
    @JsonBackReference
    private DirectorNeeds directorNeeds;

    @ManyToOne
    private Employee creator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public DirectorNeedsComment content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public DirectorNeedsComment createTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Employee getCreator() {
        return creator;
    }

    public DirectorNeedsComment creator(Employee creator) {
        this.creator = creator;
        return this;
    }

    public void setCreator(Employee creator) {
        this.creator = creator;
    }

    public DirectorNeeds getDirectorNeeds() {
        return directorNeeds;
    }

    public void setDirectorNeeds(DirectorNeeds directorNeeds) {
        this.directorNeeds = directorNeeds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DirectorNeedsComment directorNeedsComment = (DirectorNeedsComment) o;
        if(directorNeedsComment.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, directorNeedsComment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DirectorNeedsComment{" +
            "id=" + id +
            ", content='" + content + "'" +
            ", createTime='" + createTime + "'" +
            '}';
    }
}
