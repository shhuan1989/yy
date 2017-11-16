package com.yijia.yy.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yijia.yy.domain.converter.GenderConverter;
import com.yijia.yy.domain.enumeration.Gender;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ActorSearchDTO {
    private Integer ageFrom;
    private Integer ageTo;
    private Long countryId;
    private Integer genderId;

    public Integer getAgeFrom() {
        return ageFrom;
    }

    public void setAgeFrom(Integer ageFrom) {
        this.ageFrom = ageFrom;
    }

    public Integer getAgeTo() {
        return ageTo;
    }

    public void setAgeTo(Integer ageTo) {
        this.ageTo = ageTo;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public Integer getGenderId() {
        return genderId;
    }

    public void setGenderId(Integer genderId) {
        this.genderId = genderId;
    }

    public Gender getGender() {
        if (this.genderId == null) {
            return null;
        }
        return new GenderConverter().convertToEntityAttribute(this.genderId);
    }
}
