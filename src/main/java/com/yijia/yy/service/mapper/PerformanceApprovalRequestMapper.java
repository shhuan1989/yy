package com.yijia.yy.service.mapper;


import com.yijia.yy.domain.PerformanceApprovalRequest;
import com.yijia.yy.service.dto.PerformanceApprovalRequestDTO;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring", uses = {PerformanceBonusMapper.class, ApprovalMapper.class})
@DecoratedWith(PerformanceApprovalRequestMapperDecorator.class)
public interface PerformanceApprovalRequestMapper {

    @Mapping(source = "applicant.id", target = "applicantId")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "projectId", source = "project.id")
    @Mapping(target = "issuerId", source = "issuer.id")
    PerformanceApprovalRequestDTO performanceApprovalRequestToPerformanceApprovalRequestDTO(PerformanceApprovalRequest approvalRequest);

    Set<PerformanceApprovalRequestDTO> performanceApprovalRequestsToPerformanceApprovalRequestDTOs(Set<PerformanceApprovalRequest> approvalRequests);

    @Mapping(source = "applicantId", target = "applicant")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "project.id", source = "projectId")
    @Mapping(target = "issuer.id", source = "issuerId")
    PerformanceApprovalRequest performanceApprovalRequestDTOToPerformanceApprovalRequest(PerformanceApprovalRequestDTO approvalRequestDTO);

    Set<PerformanceApprovalRequest> performanceApprovalRequestDTOsToPerformanceApprovalRequests(Set<PerformanceApprovalRequestDTO> approvalRequestDTOs);
}
