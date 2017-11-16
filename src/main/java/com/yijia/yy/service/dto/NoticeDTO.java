package com.yijia.yy.service.dto;

import java.io.Serializable;
import java.util.*;


/**
 * A DTO for the Notice entity.
 */
public class NoticeDTO implements Serializable {

    private Long id;

    private String subject;

    private Long expireTime;

    private String content;

    private Long createTime;

    private Long projectId;

    private String projectName;

    private Long creatorId;

    private Long startTime;

    private Set<DeptDTO> depts = new HashSet<>();

    private Set<ProjectDTO> projects = new HashSet<>();

    private Set<EmployeeDTO> employees = new HashSet<>();

    List<NoticeChatDTO> comments = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
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

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long employeeId) {
        this.creatorId = employeeId;
    }

    public Set<DeptDTO> getDepts() {
        return depts;
    }

    public void setDepts(Set<DeptDTO> depts) {
        this.depts = depts;
    }

    public Set<ProjectDTO> getProjects() {
        return projects;
    }

    public void setProjects(Set<ProjectDTO> projects) {
        this.projects = projects;
    }

    public Set<EmployeeDTO> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<EmployeeDTO> employees) {
        this.employees = employees;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public List<NoticeChatDTO> getComments() {
        return comments;
    }

    public void setComments(List<NoticeChatDTO> comments) {
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

        NoticeDTO noticeDTO = (NoticeDTO) o;

        if ( ! Objects.equals(id, noticeDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "NoticeDTO{" +
            "id=" + id +
            ", subject='" + subject + "'" +
            ", expireTime='" + expireTime + "'" +
            ", content='" + content + "'" +
            ", createTime='" + createTime + "'" +
            '}';
    }
}
