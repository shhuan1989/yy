package com.yijia.yy.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * A Notice.
 */
@Entity
@Table(name = "notice")
public class Notice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "subject")
    private String subject;

    @Column(name = "expire_time")
    private Long expireTime;

    @Column(name = "content")
    @Lob
    private byte[] content;

    @Column(name = "create_time")
    private Long createTime;

    @Column(name = "start_time")
    private Long startTime;

    @ManyToOne
    private Project project;

    @ManyToOne
    private Employee creator;

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<NoticeChat> comments = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "notice_depts",
               joinColumns = @JoinColumn(name="notices_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="depts_id", referencedColumnName="ID"))
    private Set<Dept> depts = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "notice_projects",
               joinColumns = @JoinColumn(name="notices_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="projects_id", referencedColumnName="ID"))
    private Set<Project> projects = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "notice_employees",
               joinColumns = @JoinColumn(name="notices_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="employees_id", referencedColumnName="ID"))
    private Set<Employee> employees = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public Notice subject(String subject) {
        this.subject = subject;
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public Notice expireTime(Long expireTime) {
        this.expireTime = expireTime;
        return this;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public byte[] getContent() {
        return content;
    }

    public Notice content(byte[] content) {
        this.content = content;
        return this;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public Notice createTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Project getProject() {
        return project;
    }

    public Notice project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Employee getCreator() {
        return creator;
    }

    public Notice creator(Employee employee) {
        this.creator = employee;
        return this;
    }

    public void setCreator(Employee employee) {
        this.creator = employee;
    }

    public Set<Dept> getDepts() {
        return depts;
    }

    public Notice depts(Set<Dept> depts) {
        this.depts = depts;
        return this;
    }

    public Notice addDepts(Dept dept) {
        depts.add(dept);
        return this;
    }

    public Notice removeDepts(Dept dept) {
        depts.remove(dept);
        return this;
    }

    public void setDepts(Set<Dept> depts) {
        this.depts = depts;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public Notice projects(Set<Project> projects) {
        this.projects = projects;
        return this;
    }

    public Notice addProjects(Project project) {
        projects.add(project);
        return this;
    }

    public Notice removeProjects(Project project) {
        projects.remove(project);
        return this;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public Notice employees(Set<Employee> employees) {
        this.employees = employees;
        return this;
    }

    public Notice addEmployees(Employee employee) {
        employees.add(employee);
        return this;
    }

    public Notice removeEmployees(Employee employee) {
        employees.remove(employee);
        return this;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public List<NoticeChat> getComments() {
        return comments;
    }

    public void setComments(List<NoticeChat> comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Notice notice = (Notice) o;
        if(notice.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, notice.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Notice{" +
            "id=" + id +
            ", subject='" + subject + "'" +
            ", expireTime='" + expireTime + "'" +
            ", content='" + content + "'" +
            ", createTime='" + createTime + "'" +
            '}';
    }
}
