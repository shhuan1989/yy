package com.yijia.yy.domain.converter;


import com.yijia.yy.domain.enumeration.ApprovalStatus;

import javax.persistence.AttributeConverter;

public class ApprovalStatusConverter implements AttributeConverter<ApprovalStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ApprovalStatus approvalStatus) {
        if (approvalStatus == null) {
            return ApprovalStatus.IN_PROGRESS.ordinal();
        }
        return approvalStatus.ordinal();
    }

    @Override
    public ApprovalStatus convertToEntityAttribute(Integer ord) {
        if (ord == null || ord < 0 || ord >= ApprovalStatus.values().length) {
            return ApprovalStatus.IN_PROGRESS;
        }
        return ApprovalStatus.values()[ord];
    }
}
