package com.yijia.yy.service.dto;

import com.yijia.yy.domain.PictureInfo;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A DTO for the Actor entity.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ActorDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private DictionaryDTO gender;

    private Long birthDate;

    private Float height;

    private Float bust;

    private Float waist;

    private Float hip;

    private String tel;

    private String inputOperator;

    private Long inputTime;

    private String lastModifier;

    private Long lastModifiedTime;

    private Set<PictureInfo> photos;

    private Integer age;


    private Long sourceId;


    private String sourceName;

    private Long countryId;

    private String info;

    private String countryName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public DictionaryDTO getGender() {
        return gender;
    }

    public void setGender(DictionaryDTO gender) {
        this.gender = gender;
    }
    public Long getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Long birthDate) {
        this.birthDate = birthDate;
    }
    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }
    public Float getBust() {
        return bust;
    }

    public void setBust(Float bust) {
        this.bust = bust;
    }
    public Float getWaist() {
        return waist;
    }

    public void setWaist(Float waist) {
        this.waist = waist;
    }
    public Float getHip() {
        return hip;
    }

    public void setHip(Float hip) {
        this.hip = hip;
    }
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
    public String getInputOperator() {
        return inputOperator;
    }

    public void setInputOperator(String inputOperator) {
        this.inputOperator = inputOperator;
    }
    public Long getInputTime() {
        return inputTime;
    }

    public void setInputTime(Long inputTime) {
        this.inputTime = inputTime;
    }
    public String getLastModifier() {
        return lastModifier;
    }

    public void setLastModifier(String lastModifier) {
        this.lastModifier = lastModifier;
    }
    public Long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long dictionaryId) {
        this.sourceId = dictionaryId;
    }


    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String dictionaryName) {
        this.sourceName = dictionaryName;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long dictionaryId) {
        this.countryId = dictionaryId;
    }


    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String dictionaryName) {
        this.countryName = dictionaryName;
    }

    public Set<PictureInfo> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<PictureInfo> photos) {
        this.photos = photos;
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

        ActorDTO actorDTO = (ActorDTO) o;

        if ( ! Objects.equals(id, actorDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ActorDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", gender='" + gender + "'" +
            ", birthDate='" + birthDate + "'" +
            ", height='" + height + "'" +
            ", bust='" + bust + "'" +
            ", waist='" + waist + "'" +
            ", hip='" + hip + "'" +
            ", tel='" + tel + "'" +
            ", inputOperator='" + inputOperator + "'" +
            ", inputTime='" + inputTime + "'" +
            ", lastModifier='" + lastModifier + "'" +
            ", lastModifiedTime='" + lastModifiedTime + "'" +
            ", age='" + age + "'" +
            '}';
    }
}
