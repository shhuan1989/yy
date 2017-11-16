package com.yijia.yy.domain.converter;

import com.yijia.yy.domain.enumeration.JobPositionStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class JobPositionStatusConverter implements AttributeConverter<JobPositionStatus, Integer>{
    @Override
    public Integer convertToDatabaseColumn(JobPositionStatus jobPositionStatus) {
        if (jobPositionStatus == null) {
            return JobPositionStatus.UNKNOWN.ordinal();
        }
        return jobPositionStatus.ordinal();
    }

    @Override
    public JobPositionStatus convertToEntityAttribute(Integer ord) {
        if (ord == null || ord < 0 || ord >= JobPositionStatus.values().length) {
            return JobPositionStatus.UNKNOWN;
        }

        return JobPositionStatus.values()[ord];
    }
}
