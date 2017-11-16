package com.yijia.yy.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yijia.yy.domain.converter.GenderConverter;
import com.yijia.yy.domain.enumeration.Gender;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Actor.
 */
@Entity
@Table(name = "actor")
public class Actor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Convert(converter = GenderConverter.class)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "birth_date")
    private Long birthDate;

    @Column(name = "height")
    private Float height;

    @Column(name = "bust")
    private Float bust;

    @Column(name = "waist")
    private Float waist;

    @Column(name = "hip")
    private Float hip;

    @Column(name = "tel")
    private String tel;

    @Column(name = "input_operator")
    private String inputOperator;

    @Column(name = "input_time")
    private Long inputTime;

    @Column(name = "last_modifier")
    private String lastModifier;

    @Column(name = "last_modified_time")
    private Long lastModifiedTime;

    @Column(name = "age")
    private Integer age;

    @Column
    private String info;

    @OneToMany
    @JsonIgnore
    @JoinTable(name = "actor_photos", joinColumns = @JoinColumn(name = "actor_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "photo_id", referencedColumnName = "id"))
    private Set<PictureInfo> photos = new HashSet<>();

    @ManyToOne(cascade = CascadeType.REFRESH)
    private Dictionary source;

    @ManyToOne(cascade = CascadeType.REFRESH)
    private Dictionary country;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Actor name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public Actor gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Long getBirthDate() {
        return birthDate;
    }

    public Actor birthDate(Long birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public void setBirthDate(Long birthDate) {
        this.birthDate = birthDate;
    }

    public Float getHeight() {
        return height;
    }

    public Actor height(Float height) {
        this.height = height;
        return this;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public Float getBust() {
        return bust;
    }

    public Actor bust(Float bust) {
        this.bust = bust;
        return this;
    }

    public void setBust(Float bust) {
        this.bust = bust;
    }

    public Float getWaist() {
        return waist;
    }

    public Actor waist(Float waist) {
        this.waist = waist;
        return this;
    }

    public void setWaist(Float waist) {
        this.waist = waist;
    }

    public Float getHip() {
        return hip;
    }

    public Actor hip(Float hip) {
        this.hip = hip;
        return this;
    }

    public void setHip(Float hip) {
        this.hip = hip;
    }

    public String getTel() {
        return tel;
    }

    public Actor tel(String tel) {
        this.tel = tel;
        return this;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getInputOperator() {
        return inputOperator;
    }

    public Actor inputOperator(String inputOperator) {
        this.inputOperator = inputOperator;
        return this;
    }

    public void setInputOperator(String inputOperator) {
        this.inputOperator = inputOperator;
    }

    public Long getInputTime() {
        return inputTime;
    }

    public Actor inputTime(Long inputTime) {
        this.inputTime = inputTime;
        return this;
    }

    public void setInputTime(Long inputTime) {
        this.inputTime = inputTime;
    }

    public String getLastModifier() {
        return lastModifier;
    }

    public Actor lastModifier(String lastModifier) {
        this.lastModifier = lastModifier;
        return this;
    }

    public void setLastModifier(String lastModifier) {
        this.lastModifier = lastModifier;
    }

    public Long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public Actor lastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
        return this;
    }

    public void setLastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public Integer getAge() {
        return age;
    }

    public Actor age(Integer age) {
        this.age = age;
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Set<PictureInfo> getPhotos() {
        return photos;
    }

    public Actor photos(Set<PictureInfo> pictureInfos) {
        this.photos = pictureInfos;
        return this;
    }

    public Actor addPhotos(PictureInfo pictureInfo) {
        photos.add(pictureInfo);
        return this;
    }

    public Actor removePhotos(PictureInfo pictureInfo) {
        photos.remove(pictureInfo);
        return this;
    }

    public void setPhotos(Set<PictureInfo> pictureInfos) {
        this.photos = pictureInfos;
    }

    public Dictionary getSource() {
        return source;
    }

    public Actor source(Dictionary dictionary) {
        this.source = dictionary;
        return this;
    }

    public void setSource(Dictionary dictionary) {
        this.source = dictionary;
    }

    public Dictionary getCountry() {
        return country;
    }

    public Actor country(Dictionary dictionary) {
        this.country = dictionary;
        return this;
    }

    public void setCountry(Dictionary dictionary) {
        this.country = dictionary;
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
        Actor actor = (Actor) o;
        if(actor.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, actor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Actor{" +
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
