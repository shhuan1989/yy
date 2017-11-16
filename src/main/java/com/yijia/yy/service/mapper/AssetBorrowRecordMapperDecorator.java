package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.AssetBorrowRecord;
import com.yijia.yy.domain.converter.AssetReturnStatusConverter;
import com.yijia.yy.domain.enumeration.AssetReturnStatus;
import com.yijia.yy.service.dto.AssetBorrowRecordDTO;
import com.yijia.yy.service.dto.DictionaryDTO;
import com.yijia.yy.service.util.DomainObjectUtils;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class AssetBorrowRecordMapperDecorator implements AssetBorrowRecordMapper{

    @Inject
    @Qualifier("delegate")
    private AssetBorrowRecordMapper delegate;

    @Override
    public AssetBorrowRecordDTO assetBorrowRecordToAssetBorrowRecordDTO(AssetBorrowRecord assetBorrowRecord) {
        if (assetBorrowRecord == null) {
            return null;
        }

        AssetBorrowRecordDTO dto = delegate.assetBorrowRecordToAssetBorrowRecordDTO(assetBorrowRecord);

        if (assetBorrowRecord.getApprovalRequest() != null) {
            dto.setApprovalStatus(
                new DictionaryDTO()
                    .withId(Long.valueOf(assetBorrowRecord.getApprovalRequest().getStatus().ordinal()))
                    .withName(assetBorrowRecord.getApprovalRequest().getStatus().toString())
            );
        }

        if (assetBorrowRecord.getReturnStatus() != null) {
            dto.setReturnStatus(
                new DictionaryDTO()
                .withId(Long.valueOf(assetBorrowRecord.getReturnStatus().ordinal()))
                .withName(assetBorrowRecord.getReturnStatus().toString())
            );
        }

        return dto;
    }

    @Override
    public List<AssetBorrowRecordDTO> assetBorrowRecordsToAssetBorrowRecordDTOs(List<AssetBorrowRecord> assetBorrowRecords) {
        if (assetBorrowRecords == null) {
            return null;
        }

        return assetBorrowRecords.stream()
            .map(a -> assetBorrowRecordToAssetBorrowRecordDTO(a))
            .collect(Collectors.toList());
    }

    @Override
    public AssetBorrowRecord assetBorrowRecordDTOToAssetBorrowRecord(AssetBorrowRecordDTO assetBorrowRecordDTO) {
        if (assetBorrowRecordDTO == null) {
            return  null;
        }
        AssetBorrowRecord assetBorrowRecord = delegate.assetBorrowRecordDTOToAssetBorrowRecord(assetBorrowRecordDTO);
        if (DomainObjectUtils.dictionaryDtoIsNotNull(assetBorrowRecordDTO.getReturnStatus())) {
            DomainObjectUtils.setEnumFromDictionaryDTO(assetBorrowRecordDTO.getReturnStatus(), assetBorrowRecord,
                "returnStatus", AssetReturnStatus.class, AssetReturnStatusConverter.class);
        }

        return assetBorrowRecord;
    }

    @Override
    public List<AssetBorrowRecord> assetBorrowRecordDTOsToAssetBorrowRecords(List<AssetBorrowRecordDTO> assetBorrowRecordDTOs) {
        if (assetBorrowRecordDTOs == null) {
            return null;
        }

        return assetBorrowRecordDTOs.stream()
            .map(a -> assetBorrowRecordDTOToAssetBorrowRecord(a))
            .collect(Collectors.toList());
    }
}
