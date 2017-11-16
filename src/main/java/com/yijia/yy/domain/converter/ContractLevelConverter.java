package com.yijia.yy.domain.converter;

import com.yijia.yy.domain.enumeration.ContractLevel;

import javax.persistence.AttributeConverter;

/**
 * converter for contract level
 */
public class ContractLevelConverter implements AttributeConverter<ContractLevel, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ContractLevel contractLevel) {
        if (contractLevel == null) {
            return ContractLevel.LEVEL_1.ordinal();
        }
        return contractLevel.ordinal();
    }

    @Override
    public ContractLevel convertToEntityAttribute(Integer ord) {
        if (ord == null || ord < 0 || ord >= ContractLevel.values().length) {
            return ContractLevel.LEVEL_1;
        }
        return ContractLevel.values()[ord];    }
}
