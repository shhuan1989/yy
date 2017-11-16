package com.yijia.yy.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Role.
 */
@Entity
@Table(name = "role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "create_time")
    private Long createTime;

    @Column(name = "last_modified_time")
    private Long lastModifiedTime;

    @Lob
    private String auths;

    @Column(name = "name")
    private String name;

    @Column(length = 500)
    private String info;

    @ManyToOne
    private Employee creator;

    @ManyToOne
    private Employee lastModifier;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public Role createTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public Role lastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
        return this;
    }

    public void setLastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getAuths() {
        return auths;
    }

    public Role auths(String auths) {
        this.auths = auths;
        return this;
    }

    public void setAuths(String auths) {
        this.auths = auths;
    }

    public String getName() {
        return name;
    }

    public Role name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Employee getCreator() {
        return creator;
    }

    public Role creator(Employee employee) {
        this.creator = employee;
        return this;
    }

    public void setCreator(Employee employee) {
        this.creator = employee;
    }

    public Employee getLastModifier() {
        return lastModifier;
    }

    public Role lastModifier(Employee employee) {
        this.lastModifier = employee;
        return this;
    }

    public void setLastModifier(Employee employee) {
        this.lastModifier = employee;
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
        Role role = (Role) o;
        if(role.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Role{" +
            "id=" + id +
            ", createTime='" + createTime + "'" +
            ", lastModifiedTime='" + lastModifiedTime + "'" +
            ", auths='" + auths + "'" +
            ", name='" + name + "'" +
            '}';
    }
}
