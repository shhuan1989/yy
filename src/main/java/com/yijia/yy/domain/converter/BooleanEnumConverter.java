package com.yijia.yy.domain.converter;

import com.yijia.yy.domain.enumeration.BooleanEnum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class BooleanEnumConverter implements AttributeConverter<BooleanEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(BooleanEnum attribute) {
        if (attribute == null) {
            return BooleanEnum.UNKNOWN.ordinal();
        }
        return attribute.ordinal();
    }

    @Override
    public BooleanEnum convertToEntityAttribute(Integer ord) {
        if (ord == null || ord < 0 || ord >= BooleanEnum.values().length) {
            return BooleanEnum.UNKNOWN;
        }
        return BooleanEnum.values()[ord];
    }
}

