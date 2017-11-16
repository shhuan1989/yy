package com.yijia.yy.domain.converter;

import com.yijia.yy.domain.enumeration.ContractPaymentStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ContractPaymentStatusConverter implements AttributeConverter<ContractPaymentStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ContractPaymentStatus contractPaymentStatus) {
        if (contractPaymentStatus == null) {
            return ContractPaymentStatus.IN_PROGRESS.ordinal();
        }
        return contractPaymentStatus.ordinal();
    }

    @Override
    public ContractPaymentStatus convertToEntityAttribute(Integer ord) {
        if (ord == null || ord < 0 || ord >= ContractPaymentStatus.values().length) {
            return ContractPaymentStatus.IN_PROGRESS;
        }
        return ContractPaymentStatus.values()[ord];
    }
}
