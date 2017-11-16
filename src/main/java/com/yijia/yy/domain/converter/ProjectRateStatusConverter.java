package com.yijia.yy.domain.converter;

import com.yijia.yy.domain.enumeration.ProjectRateStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ProjectRateStatusConverter implements AttributeConverter<ProjectRateStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ProjectRateStatus projectRateStatus) {
        if (projectRateStatus == null) {
            return ProjectRateStatus.NOT_START.ordinal();
        }
        return projectRateStatus.ordinal();
    }

    @Override
    public ProjectRateStatus convertToEntityAttribute(Integer ord) {
        if (ord == null || ord < 0 || ord >= ProjectRateStatus.values().length) {
            return ProjectRateStatus.NOT_START;
        }
        return ProjectRateStatus.values()[ord];
    }
}
