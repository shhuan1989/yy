package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.PerformanceApprovalRequest;
import com.yijia.yy.domain.converter.ApprovalStatusConverter;
import com.yijia.yy.service.dto.DictionaryDTO;
import com.yijia.yy.service.dto.PerformanceApprovalRequestDTO;
import com.yijia.yy.service.util.DomainObjectUtils;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.inject.Inject;
import java.util.Set;
import java.util.stream.Collectors;

public class PerformanceApprovalRequestMapperDecorator implements PerformanceApprovalRequestMapper{
    @Inject
    @Qualifier("delegate")
    private PerformanceApprovalRequestMapper delegate;


    @Override
    public PerformanceApprovalRequestDTO performanceApprovalRequestToPerformanceApprovalRequestDTO(PerformanceApprovalRequest approvalRequest) {
        if (approvalRequest == null) {
            return null;
        }
        PerformanceApprovalRequestDTO dto = delegate.performanceApprovalRequestToPerformanceApprovalRequestDTO(approvalRequest);
        dto.setStatus(
            new DictionaryDTO()
                .withId(Long.valueOf(approvalRequest.getStatus().ordinal()))
                .withName(approvalRequest.getStatus().toString())
        );
        return dto;
    }

    @Override
    public Set<PerformanceApprovalRequestDTO> performanceApprovalRequestsToPerformanceApprovalRequestDTOs(Set<PerformanceApprovalRequest> approvalRequests) {
        if (approvalRequests == null) {
            return null;
        }

        return approvalRequests.stream()
            .map(p -> performanceApprovalRequestToPerformanceApprovalRequestDTO(p))
            .collect(Collectors.toSet());
    }

    @Override
    public PerformanceApprovalRequest performanceApprovalRequestDTOToPerformanceApprovalRequest(PerformanceApprovalRequestDTO approvalRequestDTO) {
        if (approvalRequestDTO == null) {
            return null;
        }

        PerformanceApprovalRequest approvalRequest = delegate.performanceApprovalRequestDTOToPerformanceApprovalRequest(approvalRequestDTO);

        if (DomainObjectUtils.dictionaryDtoIsNotNull(approvalRequestDTO.getStatus())) {
            approvalRequest.setStatus(
                new ApprovalStatusConverter().convertToEntityAttribute(
                    Math.toIntExact(approvalRequestDTO.getStatus().getId())
                )
            );
        }

        if (approvalRequestDTO.getIssuerId() == null) {
            approvalRequest.setIssuer(null);
        }
        return approvalRequest;
    }

    @Override
    public Set<PerformanceApprovalRequest> performanceApprovalRequestDTOsToPerformanceApprovalRequests(Set<PerformanceApprovalRequestDTO> approvalRequestDTOs) {
        if (approvalRequestDTOs == null) {
            return null;
        }

        return approvalRequestDTOs.stream()
            .map(p -> performanceApprovalRequestDTOToPerformanceApprovalRequest(p))
            .collect(Collectors.toSet());
    }
}
