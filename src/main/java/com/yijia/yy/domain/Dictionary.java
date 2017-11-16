package com.yijia.yy.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A Dictionary.
 */
@Entity
@Table(name = "dictionary")
public class Dictionary implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String NAME_COUNTRY = "国家";
    public static final String NAME_STAFF_TYPE = "工作人员类型";
    public static final String NAME_CONTRACT_STATUS = "合同状态";
    public static final String NAME_PROJECT_COMPLETE_STATUS = "完结状态";
    public static final String NAME_PROJECT_COST_CATEGORY = "支出项目";
    public static final String NAME_ROOMS = "会议室";
    public static final String NAME_VACATION_TYPES = "假期类型";
    public static final String NAME_PAY_TYPES = "支付方式";
    public static final String NAME_PROJECT_PROGRESS = "项目进度";
    public static final String NAME_CLIENT_STATUS = "客户状态";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "creator")
    private String creator;

    @Column(name = "create_time")
    private Long createTime;

    @Column(name = "comment")
    private String comment;

    @Column(name = "is_system")
    private Boolean isSystem;

    @Column(name = "last_modified_time")
    private Long lastModifiedTime;

    @Column(name = "last_modifier")
    private String lastModifier;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Dictionary> children = new ArrayList<>();

    @ManyToOne
    private Dictionary parent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Dictionary name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator() {
        return creator;
    }

    public Dictionary creator(String creator) {
        this.creator = creator;
        return this;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public Dictionary createTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getComment() {
        return comment;
    }

    public Dictionary comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean isIsSystem() {
        return isSystem;
    }

    public Dictionary isSystem(Boolean isSystem) {
        this.isSystem = isSystem;
        return this;
    }

    public void setIsSystem(Boolean isSystem) {
        this.isSystem = isSystem;
    }

    public Long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public Dictionary lastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
        return this;
    }

    public void setLastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getLastModifier() {
        return lastModifier;
    }

    public Dictionary lastModifier(String lastModifier) {
        this.lastModifier = lastModifier;
        return this;
    }

    public void setLastModifier(String lastModifier) {
        this.lastModifier = lastModifier;
    }

    public List<Dictionary> getChildren() {
        return children;
    }

    public Dictionary children(List<Dictionary> dictionaries) {
        this.children = dictionaries;
        return this;
    }

    public Dictionary addChildren(Dictionary dictionary) {
        children.add(dictionary);
        dictionary.setParent(this);
        return this;
    }

    public Dictionary removeChildren(Dictionary dictionary) {
        children.remove(dictionary);
        dictionary.setParent(null);
        return this;
    }

    public void setChildren(List<Dictionary> dictionaries) {
        this.children = dictionaries;
    }

    public Dictionary getParent() {
        return parent;
    }

    public Dictionary parent(Dictionary dictionary) {
        this.parent = dictionary;
        return this;
    }

    public void setParent(Dictionary dictionary) {
        this.parent = dictionary;
    }

    public Dictionary id(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dictionary that = (Dictionary) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Dictionary{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", creator='" + creator + "'" +
            ", createTime='" + createTime + "'" +
            ", comment='" + comment + "'" +
            ", isSystem='" + isSystem + "'" +
            ", lastModifiedTime='" + lastModifiedTime + "'" +
            ", lastModifier='" + lastModifier + "'" +
            '}';
    }
}
