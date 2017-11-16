package com.yijia.yy.domain.converter;

import com.yijia.yy.domain.enumeration.TaskStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class TaskStatusConverter implements AttributeConverter<TaskStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(TaskStatus TaskStatus) {
        if (TaskStatus == null) {
            return TaskStatus.UNKNOWN.ordinal();
        }
        return TaskStatus.value();
    }

    @Override
    public TaskStatus convertToEntityAttribute(Integer ord) {
        if (ord == null || ord < -2 || ord > 1) {
            return TaskStatus.UNKNOWN;
        }
        switch (ord) {
            case -2:
                return TaskStatus.UNKNOWN;
            case -1:
                return TaskStatus.NEW;
            case 0:
                return TaskStatus.INPROGRESS;
            case 1:
                return TaskStatus.DONE;
            default:
                return TaskStatus.UNKNOWN;
        }
    }
}
