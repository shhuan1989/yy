package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.*;
import com.yijia.yy.service.dto.ApprovalDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Approval and its DTO ApprovalDTO.
 */
@Mapper(componentModel = "spring", uses = {})
@DecoratedWith(ApprovalMapperDecorator.class)
public interface ApprovalMapper {

    @Mapping(source = "approver.id", target = "approverId")
    @Mapping(target = "status", ignore = true)
    ApprovalDTO approvalToApprovalDTO(Approval approval);

    List<ApprovalDTO> approvalsToApprovalDTOs(List<Approval> approvals);

    @Mapping(source = "approverId", target = "approver.id")
    @Mapping(target = "status", ignore = true)
    Approval approvalDTOToApproval(ApprovalDTO approvalDTO);

    List<Approval> approvalDTOsToApprovals(List<ApprovalDTO> approvalDTOs);

    default Approval approvalFromId(Long id) {
        if (id == null) {
            return null;
        }
        Approval approval = new Approval();
        approval.setId(id);
        return approval;
    }

    default ApprovalRequest approvalRequestFromId(Long id) {
        if (id == null) {
            return null;
        }

        ApprovalRequest request = new ApprovalRequest();
        request.setId(id);
        return request;
    }
}
