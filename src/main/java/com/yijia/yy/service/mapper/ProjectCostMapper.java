package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.*;
import com.yijia.yy.service.dto.ProjectCostDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ProjectCost and its DTO ProjectCostDTO.
 */
@Mapper(componentModel = "spring", uses = {EmployeeMapper.class, DictionaryMapper.class})
public interface ProjectCostMapper {

    @Mapping(source = "project.id", target = "projectId")
    ProjectCostDTO projectCostToProjectCostDTO(ProjectCost projectCost);

    List<ProjectCostDTO> projectCostsToProjectCostDTOs(List<ProjectCost> projectCosts);

    @Mapping(source = "projectId", target = "project")
    ProjectCost projectCostDTOToProjectCost(ProjectCostDTO projectCostDTO);

    List<ProjectCost> projectCostDTOsToProjectCosts(List<ProjectCostDTO> projectCostDTOs);


    default Project projectFromId(Long id) {
        if (id == null) {
            return null;
        }
        Project project = new Project();
        project.setId(id);
        return project;
    }
}
