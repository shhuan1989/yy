package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.Employee;
import com.yijia.yy.domain.Project;
import com.yijia.yy.service.dto.ProjectDTO;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity Project and its DTO ProjectDTO.
 */
@Mapper(componentModel = "spring", uses = {EmployeeMapper.class, ClientMapper.class, ContractMapper.class, DictionaryMapper.class, PerformanceApprovalRequestMapper.class})
@DecoratedWith(ProjectMapperDecorator.class)
public interface ProjectMapper {

    @Mapping(target = "isNewClient", ignore = true)
    @Mapping(target = "creatorName", source = "creator.name")
    @Mapping(target = "creatorId", source = "creator.id")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "hasContract", ignore = true)
    @Mapping(target = "rateStatus", ignore = true)
    @Mapping(target = "participantsCount", ignore = true)
    @Mapping(target = "bonusStatus", ignore = true)
    @Mapping(target = "bonusIssued", ignore = true)
    @Mapping(target = "cost", ignore = true)
    @Mapping(target = "bonusPool", ignore = true)
    ProjectDTO projectToProjectDTO(Project project);

    List<ProjectDTO> projectsToProjectDTOs(List<Project> projects);

    @Mapping(target = "isNewClient", ignore = true)
    @Mapping(target = "creator.id", source = "creatorId")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "hasContract", ignore = true)
    @Mapping(target = "rateStatus", ignore = true)
    @Mapping(target = "allMembers", ignore = true)
    @Mapping(target = "groundLevelMembers", ignore = true)
    @Mapping(target = "bonusStatus", ignore = true)
    @Mapping(target = "shootConfigs", ignore = true)
    Project projectDTOToProject(ProjectDTO projectDTO);

    List<Project> projectDTOsToProjects(List<ProjectDTO> projectDTOs);

    default Employee EmployeeFromId(Long id) {
        if (id == null) {
            return null;
        }
        return new Employee().id(id);
    }
}
