package com.yijia.yy.service.dto;

/**
 * dto used to search notices
 */
public class NoticeSearchDTO {
    private Boolean ownedOnly;

    private Long startTime;

    private Long expireTime;

    private Boolean validate;

    private String subject;

    private Long projectId;

    public Boolean getOwnedOnly() {
        return ownedOnly;
    }

    public void setOwnedOnly(Boolean ownedOnly) {
        this.ownedOnly = ownedOnly;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public Boolean getValidate() {
        return validate;
    }

    public void setValidate(Boolean validate) {
        this.validate = validate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
