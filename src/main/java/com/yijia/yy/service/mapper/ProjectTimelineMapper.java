package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.*;
import com.yijia.yy.service.dto.ProjectTimelineDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ProjectTimeline and its DTO ProjectTimelineDTO.
 */
@Mapper(componentModel = "spring", uses = {EmployeeMapper.class})
@DecoratedWith(ProjectTimelineMapperDecorator.class)
public interface ProjectTimelineMapper {

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(target = "typeId", ignore = true)
    ProjectTimelineDTO projectTimelineToProjectTimelineDTO(ProjectTimeline projectTimeline);

    List<ProjectTimelineDTO> projectTimelinesToProjectTimelineDTOs(List<ProjectTimeline> projectTimelines);

    @Mapping(source = "projectId", target = "project")
    @Mapping(target = "type", ignore = true)
    ProjectTimeline projectTimelineDTOToProjectTimeline(ProjectTimelineDTO projectTimelineDTO);

    List<ProjectTimeline> projectTimelineDTOsToProjectTimelines(List<ProjectTimelineDTO> projectTimelineDTOs);

    default Employee employeeFromId(Long id) {
        if (id == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(id);
        return employee;
    }

    default Project projectFromId(Long id) {
        if (id == null) {
            return null;
        }
        Project project = new Project();
        project.setId(id);
        return project;
    }
}
