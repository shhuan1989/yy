package com.yijia.yy.domain.converter;


import com.yijia.yy.domain.enumeration.Gender;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class GenderConverter implements AttributeConverter<Gender, Integer>{
    @Override
    public Integer convertToDatabaseColumn(Gender gender) {
        if (gender == null) {
            return Gender.UNKNOWN.ordinal();
        }
        return gender.ordinal();
    }

    @Override
    public Gender convertToEntityAttribute(Integer ord) {
        if (ord == null || ord < 0 || ord >= Gender.values().length) {
            return Gender.UNKNOWN;
        }
        return Gender.values()[ord];
    }
}
