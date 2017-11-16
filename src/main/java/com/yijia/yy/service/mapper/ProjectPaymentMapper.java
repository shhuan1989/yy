package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.*;
import com.yijia.yy.service.dto.ProjectPaymentDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ProjectPayment and its DTO ProjectPaymentDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProjectPaymentMapper {

    @Mapping(source = "project.id", target = "projectId")
    ProjectPaymentDTO projectPaymentToProjectPaymentDTO(ProjectPayment projectPayment);

    List<ProjectPaymentDTO> projectPaymentsToProjectPaymentDTOs(List<ProjectPayment> projectPayments);

    @Mapping(source = "projectId", target = "project")
    ProjectPayment projectPaymentDTOToProjectPayment(ProjectPaymentDTO projectPaymentDTO);

    List<ProjectPayment> projectPaymentDTOsToProjectPayments(List<ProjectPaymentDTO> projectPaymentDTOs);

    default Project projectFromId(Long id) {
        if (id == null) {
            return null;
        }
        Project project = new Project();
        project.setId(id);
        return project;
    }
}
