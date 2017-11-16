package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.*;
import com.yijia.yy.service.dto.ShootAgendaDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ShootAgenda and its DTO ShootAgendaDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ShootAgendaMapper {

    @Mapping(source = "creator.id", target = "creatorId")
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "project.name", target = "projectName")
    @Mapping(source = "creator.name", target = "creatorName")
    ShootAgendaDTO shootAgendaToShootAgendaDTO(ShootAgenda shootAgenda);

    List<ShootAgendaDTO> shootAgendaToShootAgendaDTOs(List<ShootAgenda> shootAgenda);

    @Mapping(source = "creatorId", target = "creator")
    @Mapping(source = "projectId", target = "project")
    @Mapping(source = "projectName", target = "project.name")
    ShootAgenda shootAgendaDTOToShootAgenda(ShootAgendaDTO shootAgendaDTO);

    List<ShootAgenda> shootAgendaDTOsToShootAgenda(List<ShootAgendaDTO> shootAgendaDTOs);

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
