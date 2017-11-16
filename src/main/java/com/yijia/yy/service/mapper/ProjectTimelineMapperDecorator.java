package com.yijia.yy.service.mapper;


import com.yijia.yy.domain.ProjectTimeline;
import com.yijia.yy.domain.converter.ProjectTimelineEventTypeConverter;
import com.yijia.yy.service.dto.ProjectTimelineDTO;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectTimelineMapperDecorator implements ProjectTimelineMapper {

    @Inject
    @Qualifier("delegate")
    private ProjectTimelineMapper delegate;

    @Override
    public ProjectTimelineDTO projectTimelineToProjectTimelineDTO(ProjectTimeline projectTimeline) {
        if (projectTimeline == null) {
            return null;
        }
        ProjectTimelineDTO dto = delegate.projectTimelineToProjectTimelineDTO(projectTimeline);
        dto.setTypeId(projectTimeline.getType().ordinal());

        return dto;
    }

    @Override
    public List<ProjectTimelineDTO> projectTimelinesToProjectTimelineDTOs(List<ProjectTimeline> projectTimelines) {
        if (projectTimelines == null) {
            return null;
        }

        return projectTimelines.stream()
            .map(p -> projectTimelineToProjectTimelineDTO(p))
            .collect(Collectors.toList());
    }

    @Override
    public ProjectTimeline projectTimelineDTOToProjectTimeline(ProjectTimelineDTO projectTimelineDTO) {
        if (projectTimelineDTO == null) {
            return null;
        }

        ProjectTimeline timeline = delegate.projectTimelineDTOToProjectTimeline(projectTimelineDTO);
        timeline.setType( new ProjectTimelineEventTypeConverter().convertToEntityAttribute(projectTimelineDTO.getTypeId()));

        return timeline;
    }

    @Override
    public List<ProjectTimeline> projectTimelineDTOsToProjectTimelines(List<ProjectTimelineDTO> projectTimelineDTOs) {
        if (projectTimelineDTOs == null) {
            return null;
        }

        return projectTimelineDTOs.stream()
            .map(p -> projectTimelineDTOToProjectTimeline(p))
            .collect(Collectors.toList());
    }
}
