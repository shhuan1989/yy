package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.*;
import com.yijia.yy.service.dto.ProjectProgressItemDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ProjectProgressItem and its DTO ProjectProgressItemDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProjectProgressItemMapper {

    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "projectProgressTable.id", target = "projectProgressTableId")
    @Mapping(source = "owner.name", target = "ownerName")
    ProjectProgressItemDTO projectProgressItemToProjectProgressItemDTO(ProjectProgressItem projectProgressItem);

    List<ProjectProgressItemDTO> projectProgressItemsToProjectProgressItemDTOs(List<ProjectProgressItem> projectProgressItems);

    @Mapping(source = "ownerId", target = "owner")
    @Mapping(source = "projectProgressTableId", target = "projectProgressTable")
    ProjectProgressItem projectProgressItemDTOToProjectProgressItem(ProjectProgressItemDTO projectProgressItemDTO);

    List<ProjectProgressItem> projectProgressItemDTOsToProjectProgressItems(List<ProjectProgressItemDTO> projectProgressItemDTOs);

    default Employee employeeFromId(Long id) {
        if (id == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(id);
        return employee;
    }

    default ProjectProgressTable projectProgressTableFromId(Long id) {
        if (id == null) {
            return null;
        }
        ProjectProgressTable projectProgressTable = new ProjectProgressTable();
        projectProgressTable.setId(id);
        return projectProgressTable;
    }
}
