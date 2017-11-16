package com.yijia.yy.domain.converter;


import com.yijia.yy.domain.enumeration.AssetReturnStatus;

import javax.persistence.AttributeConverter;

public class AssetReturnStatusConverter implements AttributeConverter<AssetReturnStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(AssetReturnStatus assetReturnStatus) {
        if (assetReturnStatus == null) {
            return AssetReturnStatus.NO.ordinal();
        }
        return assetReturnStatus.ordinal();
    }

    @Override
    public AssetReturnStatus convertToEntityAttribute(Integer ord) {
        if (ord == null || ord < 0 || ord >= AssetReturnStatus.values().length) {
            return AssetReturnStatus.NO;
        }
        return AssetReturnStatus.values()[ord];
    }
}
