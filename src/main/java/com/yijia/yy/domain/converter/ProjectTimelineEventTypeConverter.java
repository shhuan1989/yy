package com.yijia.yy.domain.converter;

import com.yijia.yy.domain.enumeration.ProjectTimelineEventType;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * converter for converting ProjectTimelineEventType
 */
@Converter
public class ProjectTimelineEventTypeConverter implements AttributeConverter<ProjectTimelineEventType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ProjectTimelineEventType projectStatus) {
        if (projectStatus == null) {
            return ProjectTimelineEventType.UNKNOWN.ordinal();
        }
        return projectStatus.ordinal();
    }

    @Override
    public ProjectTimelineEventType convertToEntityAttribute(Integer ord) {
        if (ord == null || ord < 0 || ord >= ProjectTimelineEventType.values().length) {
            return ProjectTimelineEventType.UNKNOWN;
        }
        return ProjectTimelineEventType.values()[ord];
    }
}
