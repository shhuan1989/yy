package com.yijia.yy.domain;


import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.Objects;

/**
 * A FileInfo.
 */
@Entity
@Table(name = "file_info")
@Inheritance( strategy = InheritanceType.SINGLE_TABLE )
@DiscriminatorColumn( name = "type" )
@DiscriminatorValue( value=FileInfo.TYPE_FILE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class FileInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String TYPE_FILE="FILE";
    public static final String TYPE_IMAGE="IMAGE";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "origin_name")
    private String originName;

    @Column(name = "name")
    private String name;

    @Column(name = "create_time")
    private Long createTime;

    @Column(name = "creator")
    private String creator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginName() {
        return originName;
    }

    public FileInfo originName(String originName) {
        this.originName = originName;
        return this;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getName() {
        return name;
    }

    public FileInfo name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public FileInfo createTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public FileInfo creator(String creator) {
        this.creator = creator;
        return this;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @XmlElement(name="type")
    public String getType() {
        return FileInfo.TYPE_FILE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FileInfo fileInfo = (FileInfo) o;
        if(fileInfo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, fileInfo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FileInfo{" +
            "id=" + id +
            ", originName='" + originName + "'" +
            ", name='" + name + "'" +
            ", createTime='" + createTime + "'" +
            ", creator='" + creator + "'" +
            '}';
    }

    public FileInfo id(Long id) {
        this.id = id;
        return this;
    }
}
