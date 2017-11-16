package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.*;
import com.yijia.yy.service.dto.ApprovalRequestDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity ApprovalRequest and its DTO ApprovalRequestDTO.
 */
@Mapper(componentModel = "spring", uses = {ApprovalMapper.class})
@DecoratedWith(ApprovalRequestMapperDecorator.class)
public interface ApprovalRequestMapper {

    @Mapping(source = "applicant.id", target = "applicantId")
    @Mapping(target = "status", ignore = true)
    ApprovalRequestDTO approvalRequestToApprovalRequestDTO(ApprovalRequest approvalRequest);

    List<ApprovalRequestDTO> approvalRequestsToApprovalRequestDTOs(List<ApprovalRequest> approvalRequests);

    @Mapping(source = "applicantId", target = "applicant")
    @Mapping(target = "status", ignore = true)
    ApprovalRequest approvalRequestDTOToApprovalRequest(ApprovalRequestDTO approvalRequestDTO);

    List<ApprovalRequest> approvalRequestDTOsToApprovalRequests(List<ApprovalRequestDTO> approvalRequestDTOs);

    default Employee employeeFromId(Long id) {
        if (id == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(id);
        return employee;
    }
}
