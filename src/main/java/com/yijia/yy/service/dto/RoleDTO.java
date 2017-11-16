package com.yijia.yy.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Role entity.
 */
public class RoleDTO implements Serializable {

    private Long id;

    private Long createTime;

    private Long lastModifiedTime;

    private String auths;

    private String name;

    private String info;

    private Long creatorId;

    private Long lastModifierId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
    public Long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }
    public String getAuths() {
        return auths;
    }

    public void setAuths(String auths) {
        this.auths = auths;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long employeeId) {
        this.creatorId = employeeId;
    }

    public Long getLastModifierId() {
        return lastModifierId;
    }

    public void setLastModifierId(Long employeeId) {
        this.lastModifierId = employeeId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RoleDTO roleDTO = (RoleDTO) o;

        if ( ! Objects.equals(id, roleDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RoleDTO{" +
            "id=" + id +
            ", createTime='" + createTime + "'" +
            ", lastModifiedTime='" + lastModifiedTime + "'" +
            ", auths='" + auths + "'" +
            ", name='" + name + "'" +
            '}';
    }
}
