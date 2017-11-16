package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.*;
import com.yijia.yy.service.dto.ProjectRateDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ProjectRate and its DTO ProjectRateDTO.
 */
@Mapper(componentModel = "spring", uses = {EmployeeMapper.class})
public interface ProjectRateMapper {

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "project.name", target = "projectName")
    @Mapping(source = "owner.id", target = "ownerId")
    ProjectRateDTO projectRateToProjectRateDTO(ProjectRate projectRate);

    List<ProjectRateDTO> projectRatesToProjectRateDTOs(List<ProjectRate> projectRates);

    @Mapping(source = "projectId", target = "project")
    @Mapping(source = "ownerId", target = "owner")
    ProjectRate projectRateDTOToProjectRate(ProjectRateDTO projectRateDTO);

    List<ProjectRate> projectRateDTOsToProjectRates(List<ProjectRateDTO> projectRateDTOs);

    default Project projectFromId(Long id) {
        if (id == null) {
            return null;
        }
        Project project = new Project();
        project.setId(id);
        return project;
    }

    default Employee employeeFromId(Long id) {
        if (id == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(id);
        return employee;
    }
}
