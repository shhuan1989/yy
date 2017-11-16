package com.yijia.yy.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yijia.yy.domain.converter.TaskStatusConverter;
import com.yijia.yy.domain.enumeration.TaskStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Task.
 */
@Entity
@Table(name = "task")
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "info", length = 4000)
    private String info;

    @Column(name = "create_time")
    private Long createTime;

    @Column(name = "eta")
    private Long eta;

    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "task_members",
               joinColumns = @JoinColumn(name="tasks_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="members_id", referencedColumnName="ID"))
    private Set<Employee> members = new HashSet<>();

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "task_attachments",
               joinColumns = @JoinColumn(name="tasks_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="attachments_id", referencedColumnName="ID"))
    private Set<FileInfo> attachments = new HashSet<>();

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "task_picture_infos",
               joinColumns = @JoinColumn(name="tasks_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="picture_infos_id", referencedColumnName="ID"))
    private Set<PictureInfo> pictureInfos = new HashSet<>();


    @Column
    @Convert(converter = TaskStatusConverter.class)
    private TaskStatus status;

    @OneToMany(mappedBy = "task", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<Comment> comments = new HashSet<>();

    @ManyToOne(cascade = CascadeType.REFRESH)
    private Employee creator;

    @ManyToOne(cascade = CascadeType.REFRESH)
    private Employee owner;

    @ManyToOne(cascade = CascadeType.REFRESH)
    private Project project;

    @ManyToOne(cascade = CascadeType.REFRESH)
    private Employee lastModifier;

    @Column(name = "sort_order")
    private Integer order;

    @Column
    private Long lastUpdateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Task name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public Task info(String info) {
        this.info = info;
        return this;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public Task createTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getEta() {
        return eta;
    }

    public Task eta(Long eta) {
        this.eta = eta;
        return this;
    }

    public void setEta(Long eta) {
        this.eta = eta;
    }

    public Set<Employee> getMembers() {
        return members;
    }

    public Task members(Set<Employee> employees) {
        this.members = employees;
        return this;
    }

    public Task addMembers(Employee employee) {
        members.add(employee);
        return this;
    }

    public Task removeMembers(Employee employee) {
        members.remove(employee);
        return this;
    }

    public void setMembers(Set<Employee> employees) {
        this.members = employees;
    }

    public Set<FileInfo> getAttachments() {
        return attachments;
    }

    public Task attachments(Set<FileInfo> fileInfos) {
        this.attachments = fileInfos;
        return this;
    }

    public Task addAttachments(FileInfo fileInfo) {
        attachments.add(fileInfo);
        return this;
    }

    public Task removeAttachments(FileInfo fileInfo) {
        attachments.remove(fileInfo);
        return this;
    }

    public void setAttachments(Set<FileInfo> fileInfos) {
        this.attachments = fileInfos;
    }

    public Set<PictureInfo> getPictureInfos() {
        return pictureInfos;
    }

    public Task pictureInfos(Set<PictureInfo> pictureInfos) {
        this.pictureInfos = pictureInfos;
        return this;
    }

    public Task addPictureInfos(PictureInfo pictureInfo) {
        pictureInfos.add(pictureInfo);
        return this;
    }

    public Task removePictureInfos(PictureInfo pictureInfo) {
        pictureInfos.remove(pictureInfo);
        return this;
    }

    public void setPictureInfos(Set<PictureInfo> pictureInfos) {
        this.pictureInfos = pictureInfos;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Task status(TaskStatus dictionary) {
        this.status = dictionary;
        return this;
    }

    public void setStatus(TaskStatus dictionary) {
        this.status = dictionary;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public Task comments(Set<Comment> comments) {
        this.comments = comments;
        return this;
    }

    public Task addComments(Comment comment) {
        comments.add(comment);
        comment.setTask(this);
        return this;
    }

    public Task removeComments(Comment comment) {
        comments.remove(comment);
        comment.setTask(null);
        return this;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Employee getCreator() {
        return creator;
    }

    public Task creator(Employee employee) {
        this.creator = employee;
        return this;
    }

    public void setCreator(Employee employee) {
        this.creator = employee;
    }

    public Employee getOwner() {
        return owner;
    }

    public Task owner(Employee employee) {
        this.owner = employee;
        return this;
    }

    public void setOwner(Employee employee) {
        this.owner = employee;
    }

    public Project getProject() {
        return project;
    }

    public Task project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
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

    public Employee getLastModifier() {
        return lastModifier;
    }

    public void setLastModifier(Employee lastModifier) {
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
        Task task = (Task) o;
        if(task.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Task{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", info='" + info + "'" +
            ", createTime='" + createTime + "'" +
            ", eta='" + eta + "'" +
            '}';
    }

    public Task id(Long id) {
        this.id = id;
        return this;
    }
}
