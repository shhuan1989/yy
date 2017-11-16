package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.ApprovalRequest;
import com.yijia.yy.domain.converter.ApprovalStatusConverter;
import com.yijia.yy.domain.converter.ContractPaymentStatusConverter;
import com.yijia.yy.domain.enumeration.ApprovalStatus;
import com.yijia.yy.domain.enumeration.ContractPaymentStatus;
import com.yijia.yy.service.dto.ApprovalRequestDTO;
import com.yijia.yy.service.dto.DictionaryDTO;
import com.yijia.yy.service.util.DomainObjectUtils;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class ApprovalRequestMapperDecorator implements ApprovalRequestMapper{

    @Inject
    @Qualifier("delegate")
    private ApprovalRequestMapper delegate;

    @Override
    public ApprovalRequestDTO approvalRequestToApprovalRequestDTO(ApprovalRequest approvalRequest) {
        if (approvalRequest == null) {
            return null;
        }

        ApprovalRequestDTO dto = delegate.approvalRequestToApprovalRequestDTO(approvalRequest);
        dto.setStatus(
            new DictionaryDTO()
                .withId(Long.valueOf(approvalRequest.getStatus().ordinal()))
                .withName(approvalRequest.getStatus().toString())
        );
        return dto;
    }

    @Override
    public List<ApprovalRequestDTO> approvalRequestsToApprovalRequestDTOs(List<ApprovalRequest> approvalRequests) {
        if (approvalRequests == null) {
            return null;
        }

        return approvalRequests.stream()
            .map(a -> approvalRequestToApprovalRequestDTO(a))
            .collect(Collectors.toList());
    }

    @Override
    public ApprovalRequest approvalRequestDTOToApprovalRequest(ApprovalRequestDTO approvalRequestDTO) {
        if (approvalRequestDTO == null) {
            return null;
        }

        ApprovalRequest approvalRequest = delegate.approvalRequestDTOToApprovalRequest(approvalRequestDTO);
        DomainObjectUtils.setEnumFromDictionaryDTO(approvalRequestDTO.getStatus(), approvalRequest, "status",
            ApprovalStatus.class, ApprovalStatusConverter.class);
        return approvalRequest;
    }

    @Override
    public List<ApprovalRequest> approvalRequestDTOsToApprovalRequests(List<ApprovalRequestDTO> approvalRequestDTOs) {
        if (approvalRequestDTOs == null) {
            return null;
        }

        return approvalRequestDTOs.stream()
            .map(a -> approvalRequestDTOToApprovalRequest(a))
            .collect(Collectors.toList());
    }
}
