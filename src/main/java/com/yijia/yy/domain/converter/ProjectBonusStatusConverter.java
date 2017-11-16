package com.yijia.yy.domain.converter;

import com.yijia.yy.domain.enumeration.ProjectBonusStatus;

import javax.persistence.AttributeConverter;


public class ProjectBonusStatusConverter implements AttributeConverter<ProjectBonusStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ProjectBonusStatus projectBonusStatus) {
        if (projectBonusStatus == null) {
            return ProjectBonusStatus.NOT_STARTED.ordinal();
        }
        return projectBonusStatus.ordinal();
    }

    @Override
    public ProjectBonusStatus convertToEntityAttribute(Integer ord) {
        if (ord == null || ord < 0 || ord >= ProjectBonusStatus.values().length) {
            return ProjectBonusStatus.NOT_STARTED;
        }
        return ProjectBonusStatus.values()[ord];
    }
}
