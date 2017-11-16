package com.yijia.yy.service;


import com.yijia.yy.domain.Approval;
import com.yijia.yy.domain.ApprovalItem;
import com.yijia.yy.domain.ApprovalRequest;
import com.yijia.yy.domain.OvertimeWork;
import com.yijia.yy.domain.enumeration.ApprovalStatus;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.UUID;

@Service
public class UtilService {
    private final Logger log = LoggerFactory.getLogger(UtilService.class);

    @Inject
    private UserService userService;

    public void initApprovalItem(ApprovalItem approvalItem) {
        if (approvalItem == null) {
            return;
        }

        Long currentTimeMillis = System.currentTimeMillis();
        String uuid = UUID.randomUUID().toString();

        if (approvalItem.getCreateTime() == null) {
            approvalItem.setCreateTime(currentTimeMillis);
        }
        if (approvalItem.getOwner() == null) {
            approvalItem.setOwner(userService.currentLoginEmployee());
        }
        approvalItem.setCorrelationId(uuid);

        ApprovalRequest approvalRequest = approvalItem.getApprovalRequest();
        if (approvalRequest != null) {
            if (approvalRequest.getCreateTime() == null) {
                approvalRequest.setCreateTime(currentTimeMillis);
            }
            approvalRequest.setCorrelationId(uuid);
            approvalRequest.setLastModifiedTime(currentTimeMillis);
            if (approvalRequest.getStatus() == null) {
                approvalRequest.setStatus(ApprovalStatus.NOT_START);
            }

            Approval p = approvalRequest.getApprovalRoot();
            while (p != null) {
                if (p.getCreateTime() == null) {
                    p.setCreateTime(currentTimeMillis);
                }
                p.setLastModifiedTime(currentTimeMillis);
                p.setCorrelationId(uuid);
                if (p.getStatus() == null) {
                    p.setStatus(ApprovalStatus.NOT_START);
                }
                p = p.getNextApproval();
            }
            if (BooleanUtils.toBoolean(approvalItem.getAutoStart())) {
                p = approvalRequest.getApprovalRoot();
                if (p != null && (p.getStatus() == null || p.getStatus() == ApprovalStatus.NOT_START)) {
                    p.setStatus(ApprovalStatus.IN_PROGRESS);
                }
                if (approvalRequest.getStatus() == null || approvalRequest.getStatus() == ApprovalStatus.NOT_START) {
                    approvalRequest.setStatus(ApprovalStatus.IN_PROGRESS);
                }
            } else {
                approvalItem.setAutoStart(false);
            }
        }
    }
}
