package com.yijia.yy.domain.converter;

import com.yijia.yy.domain.enumeration.MeetingStatus;

import javax.persistence.AttributeConverter;

public class MeetingStatusConverter implements AttributeConverter<MeetingStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(MeetingStatus status) {
        if (status == null) {
            return MeetingStatus.NOT_START.ordinal();
        }

        return status.ordinal();
    }

    @Override
    public MeetingStatus convertToEntityAttribute(Integer ord) {
        if (ord == null || ord < 0 || ord >= MeetingStatus.values().length) {
            return MeetingStatus.NOT_START;
        }
        return MeetingStatus.values()[ord];
    }
}
