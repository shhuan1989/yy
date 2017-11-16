package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.*;
import com.yijia.yy.service.dto.PerformanceBonusDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity PerformanceBonus and its DTO PerformanceBonusDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PerformanceBonusMapper {

    @Mapping(source = "creator.id", target = "creatorId")
    @Mapping(source = "lastModifier.id", target = "lastModifierId")
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "owner.id", target = "ownerId")
    PerformanceBonusDTO performanceBonusToPerformanceBonusDTO(PerformanceBonus performanceBonus);

    List<PerformanceBonusDTO> performanceBonusesToPerformanceBonusDTOs(List<PerformanceBonus> performanceBonuses);

    @Mapping(source = "creatorId", target = "creator")
    @Mapping(source = "lastModifierId", target = "lastModifier")
    @Mapping(source = "projectId", target = "project")
    @Mapping(source = "ownerId", target = "owner")
    PerformanceBonus performanceBonusDTOToPerformanceBonus(PerformanceBonusDTO performanceBonusDTO);

    List<PerformanceBonus> performanceBonusDTOsToPerformanceBonuses(List<PerformanceBonusDTO> performanceBonusDTOs);

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
