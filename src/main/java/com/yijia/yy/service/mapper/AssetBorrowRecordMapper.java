package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.*;
import com.yijia.yy.service.dto.AssetBorrowRecordDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity AssetBorrowRecord and its DTO AssetBorrowRecordDTO.
 */
@Mapper(componentModel = "spring", uses = {AssetMapper.class, EmployeeMapper.class, ApprovalRequestMapper.class})
@DecoratedWith(AssetBorrowRecordMapperDecorator.class)
public interface AssetBorrowRecordMapper {

    @Mapping(target = "approvalStatus", ignore = true)
    @Mapping(target = "returnStatus", ignore = true)
    AssetBorrowRecordDTO assetBorrowRecordToAssetBorrowRecordDTO(AssetBorrowRecord assetBorrowRecord);

    List<AssetBorrowRecordDTO> assetBorrowRecordsToAssetBorrowRecordDTOs(List<AssetBorrowRecord> assetBorrowRecords);

    @Mapping(target = "returnStatus", ignore = true)
    AssetBorrowRecord assetBorrowRecordDTOToAssetBorrowRecord(AssetBorrowRecordDTO assetBorrowRecordDTO);

    List<AssetBorrowRecord> assetBorrowRecordDTOsToAssetBorrowRecords(List<AssetBorrowRecordDTO> assetBorrowRecordDTOs);

    default Employee employeeFromId(Long id) {
        if (id == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(id);
        return employee;
    }

    default Asset assetFromId(Long id) {
        if (id == null) {
            return null;
        }
        Asset asset = new Asset();
        asset.setId(id);
        return asset;
    }
}
