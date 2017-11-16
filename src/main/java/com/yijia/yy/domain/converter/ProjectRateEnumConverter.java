package com.yijia.yy.domain.converter;

import com.yijia.yy.domain.enumeration.ProjectRateEnum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ProjectRateEnumConverter implements AttributeConverter<ProjectRateEnum, Integer>{
    @Override
    public Integer convertToDatabaseColumn(ProjectRateEnum ProjectRateEnum) {
        if (ProjectRateEnum == null) {
            return ProjectRateEnum.UNKNOWN.ordinal();
        }
        return ProjectRateEnum.ordinal();
    }

    @Override
    public ProjectRateEnum convertToEntityAttribute(Integer ord) {
        if (ord == null || ord < 0 || ord >= ProjectRateEnum.values().length) {
            return ProjectRateEnum.UNKNOWN;
        }
        return ProjectRateEnum.values()[ord];
    }
}
