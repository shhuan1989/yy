package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.*;
import com.yijia.yy.service.dto.ProjectProgressTableDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ProjectProgressTable and its DTO ProjectProgressTableDTO.
 */
@Mapper(componentModel = "spring", uses = {ProjectProgressItemMapper.class})
public interface ProjectProgressTableMapper {

    @Mapping(source = "creator.id", target = "creatorId")
    @Mapping(source = "project.id", target = "projectId")
    ProjectProgressTableDTO projectProgressTableToProjectProgressTableDTO(ProjectProgressTable projectProgressTable);

    List<ProjectProgressTableDTO> projectProgressTablesToProjectProgressTableDTOs(List<ProjectProgressTable> projectProgressTables);

    @Mapping(source = "creatorId", target = "creator.id")
    @Mapping(source = "projectId", target = "project.id")
    ProjectProgressTable projectProgressTableDTOToProjectProgressTable(ProjectProgressTableDTO projectProgressTableDTO);

    List<ProjectProgressTable> projectProgressTableDTOsToProjectProgressTables(List<ProjectProgressTableDTO> projectProgressTableDTOs);

    default Employee employeeFromId(Long id) {
        if (id == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(id);
        return employee;
    }
}
