package com.yijia.yy.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;


/**
 * A PictureInfo.
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@DiscriminatorValue( value=FileInfo.TYPE_IMAGE )
public class PictureInfo extends FileInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private PictureInfo thumbnail;

    public PictureInfo getThumbnail() {
        return thumbnail;
    }

    public PictureInfo thumbnail(PictureInfo pictureInfo) {
        this.thumbnail = pictureInfo;
        return this;
    }

    public void setThumbnail(PictureInfo pictureInfo) {
        this.thumbnail = pictureInfo;
    }

    public PictureInfo originName(String originName) {
        super.originName(originName);
        return this;
    }

    public PictureInfo name(String name) {
        super.name(name);
        return this;
    }

    public PictureInfo createTime(Long createTime) {
        super.createTime(createTime);
        return this;
    }

    public PictureInfo creator(String creator) {
        super.creator(creator);
        return this;
    }

    public String getType() {
        return FileInfo.TYPE_IMAGE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PictureInfo pictureInfo = (PictureInfo) o;
        if(pictureInfo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pictureInfo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PictureInfo{" +
            "thumbnail=" + thumbnail +
            '}';
    }
}
