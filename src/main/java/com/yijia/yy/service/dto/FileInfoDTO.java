package com.yijia.yy.service.dto;

import java.io.Serializable;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A DTO for the FileInfo entity.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class FileInfoDTO implements Serializable {

    private Long id;

    private String originName;

    private String name;

    private Long createTime;

    private String creator;

    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FileInfoDTO fileInfoDTO = (FileInfoDTO) o;

        if ( ! Objects.equals(id, fileInfoDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FileInfoDTO{" +
            "id=" + id +
            ", originName='" + originName + "'" +
            ", name='" + name + "'" +
            ", createTime='" + createTime + "'" +
            ", creator='" + creator + "'" +
            '}';
    }
}
