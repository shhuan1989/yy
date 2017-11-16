package com.yijia.yy.domain.converter;

import com.yijia.yy.domain.enumeration.Education;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class EducationConverter implements AttributeConverter<Education, Integer>{
    @Override
    public Integer convertToDatabaseColumn(Education education) {
        if (education == null) {
            return Education.UNKNOWN.ordinal();
        }
        return education.ordinal();
    }

    @Override
    public Education convertToEntityAttribute(Integer ord) {
        if (ord == null || ord < 0 || ord >= Education.values().length) {
            return Education.UNKNOWN;
        }
        return Education.values()[ord];
    }
}
