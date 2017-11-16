package com.yijia.yy.domain.converter;

import com.yijia.yy.domain.enumeration.HasEnum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class HasEnumConverter implements AttributeConverter<HasEnum, Integer> {
    @Override
    public Integer convertToDatabaseColumn(HasEnum attribute) {
        if (attribute == null) {
            return HasEnum.UNKNOWN.ordinal();
        }
        return attribute.ordinal();
    }

    @Override
    public HasEnum convertToEntityAttribute(Integer ord) {
        if (ord == null || ord < 0 || ord >= HasEnum.values().length) {
            return HasEnum.UNKNOWN;
        }
        return HasEnum.values()[ord];
    }
}
