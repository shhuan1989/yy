package com.yijia.yy.domain.converter;

import com.yijia.yy.domain.enumeration.ProjectStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ProjectStatusConverter implements AttributeConverter<ProjectStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ProjectStatus projectStatus) {
        if (projectStatus == null) {
            return ProjectStatus.INCOMPLETE.ordinal();
        }
        return projectStatus.ordinal();
    }

    @Override
    public ProjectStatus convertToEntityAttribute(Integer ord) {
        if (ord == null || ord < 0 || ord >= ProjectStatus.values().length) {
            return ProjectStatus.INCOMPLETE;
        }
        return ProjectStatus.values()[ord];
    }
}
