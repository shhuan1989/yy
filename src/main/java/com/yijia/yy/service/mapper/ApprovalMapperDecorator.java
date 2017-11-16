package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.Approval;
import com.yijia.yy.domain.converter.ApprovalStatusConverter;
import com.yijia.yy.service.dto.ApprovalDTO;
import com.yijia.yy.service.dto.DictionaryDTO;
import com.yijia.yy.service.util.DomainObjectUtils;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class ApprovalMapperDecorator implements ApprovalMapper {

    @Inject
    @Qualifier("delegate")
    private ApprovalMapper delegate;

    @Override
    public ApprovalDTO approvalToApprovalDTO(Approval approval) {
        if (approval == null) {
            return null;
        }

        ApprovalDTO dto = delegate.approvalToApprovalDTO(approval);

        ApprovalDTO d = dto;
        Approval p = approval;
        while (d != null) {
            d.setStatus(
                new DictionaryDTO()
                    .withId(Long.valueOf(p.getStatus().ordinal()))
                    .withName(p.getStatus().toString())
            );
            d = d.getNextApproval();
            p = p.getNextApproval();
        }

        return dto;
    }

    @Override
    public List<ApprovalDTO> approvalsToApprovalDTOs(List<Approval> approvals) {
        if (approvals == null) {
            return null;
        }

        return approvals.stream()
            .map(a -> approvalToApprovalDTO(a))
            .collect(Collectors.toList());
    }

    @Override
    public Approval approvalDTOToApproval(ApprovalDTO approvalDTO) {
        if (approvalDTO == null) {
            return null;
        }

        Approval approval = delegate.approvalDTOToApproval(approvalDTO);

        Approval p = approval;
        ApprovalDTO d = approvalDTO;
        while (p != null) {
            if (DomainObjectUtils.dictionaryDtoIsNotNull(approvalDTO.getStatus())) {
                p.setStatus(
                    new ApprovalStatusConverter().convertToEntityAttribute(
                        Math.toIntExact(d.getStatus().getId())
                    )
                );
            }
            p = p.getNextApproval();
            d = d.getNextApproval();
        }

        return approval;
    }

    @Override
    public List<Approval> approvalDTOsToApprovals(List<ApprovalDTO> approvalDTOs) {
        if (approvalDTOs == null) {
            return null;
        }

        return approvalDTOs.stream()
            .map(a -> approvalDTOToApproval(a))
            .collect(Collectors.toList());
    }
}
