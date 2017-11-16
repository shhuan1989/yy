package com.yijia.yy.service.impl;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.domain.Asset;
import com.yijia.yy.domain.enumeration.AssetReturnStatus;
import com.yijia.yy.repository.AssetRepository;
import com.yijia.yy.service.AssetBorrowRecordService;
import com.yijia.yy.domain.AssetBorrowRecord;
import com.yijia.yy.repository.AssetBorrowRecordRepository;
import com.yijia.yy.service.UserService;
import com.yijia.yy.service.UtilService;
import com.yijia.yy.service.dto.AssetBorrowRecordDTO;
import com.yijia.yy.service.mapper.AssetBorrowRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing AssetBorrowRecord.
 */
@Service
@Transactional
public class AssetBorrowRecordServiceImpl implements AssetBorrowRecordService{

    private final Logger log = LoggerFactory.getLogger(AssetBorrowRecordServiceImpl.class);

    @Inject
    private AssetBorrowRecordRepository assetBorrowRecordRepository;

    @Inject
    private AssetBorrowRecordMapper assetBorrowRecordMapper;

    @Inject
    private UtilService utilService;

    @Inject
    private UserService userService;

    @Inject
    private AssetRepository assetRepository;

    /**
     * Save a assetBorrowRecord.
     *
     * @param assetBorrowRecordDTO the entity to save
     * @return the persisted entity
     */
    public AssetBorrowRecordDTO save(AssetBorrowRecordDTO assetBorrowRecordDTO) {
        log.debug("Request to save AssetBorrowRecord : {}", assetBorrowRecordDTO);
        AssetBorrowRecord assetBorrowRecord = assetBorrowRecordMapper.assetBorrowRecordDTOToAssetBorrowRecord(assetBorrowRecordDTO);
        utilService.initApprovalItem(assetBorrowRecord);
        if (assetBorrowRecord.getReturnStatus() == AssetReturnStatus.RETURNED) {
            assetBorrowRecord.setReturner(userService.currentLoginEmployee());
            assetBorrowRecord.setActualEndTime(System.currentTimeMillis());
        }

        if (assetBorrowRecord.getReturnStatus() == AssetReturnStatus.RETURNED) {
            Asset asset = assetRepository.findOne(assetBorrowRecord.getAsset().getId());
            asset.setAmount(asset.getAmount() + assetBorrowRecord.getAmount());
            assetRepository.save(asset);
        }

        assetBorrowRecord = assetBorrowRecordRepository.save(assetBorrowRecord);
        AssetBorrowRecordDTO result = assetBorrowRecordMapper.assetBorrowRecordToAssetBorrowRecordDTO(assetBorrowRecord);
        return result;
    }

    /**
     *  Get all the assetBorrowRecords.
     *
     *  @param sort the order
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<AssetBorrowRecordDTO> findAll(Sort sort) {
        log.debug("Request to get all AssetBorrowRecords");
        List<AssetBorrowRecordDTO> result = assetBorrowRecordRepository.findAll(sort).stream()
            .map(assetBorrowRecordMapper::assetBorrowRecordToAssetBorrowRecordDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get all the assetBorrowRecords.
     *
     *  @param predicate the predicate
     *  @param sort the order
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<AssetBorrowRecordDTO> findAll(Predicate predicate, Sort sort) {
        log.debug("Request to get all AssetBorrowRecords");
        List<AssetBorrowRecordDTO> result = StreamSupport.stream(assetBorrowRecordRepository.findAll(predicate, sort).spliterator(), false)
            .map(assetBorrowRecordMapper::assetBorrowRecordToAssetBorrowRecordDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one assetBorrowRecord by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public AssetBorrowRecordDTO findOne(Long id) {
        log.debug("Request to get AssetBorrowRecord : {}", id);
        AssetBorrowRecord assetBorrowRecord = assetBorrowRecordRepository.findOne(id);
        AssetBorrowRecordDTO assetBorrowRecordDTO = assetBorrowRecordMapper.assetBorrowRecordToAssetBorrowRecordDTO(assetBorrowRecord);
        return assetBorrowRecordDTO;
    }

    /**
     *  Delete the  assetBorrowRecord by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete AssetBorrowRecord : {}", id);
        assetBorrowRecordRepository.delete(id);
    }

    @Override
    public void returnItem(Long id) {
        log.debug("Request to return AssetBorrowRecord : {}", id);
        if (id == null) {
            throw new IllegalArgumentException("ID 不能为空");
        }
        AssetBorrowRecord record = assetBorrowRecordRepository.findOne(id);
        if (record == null) {
            throw new IllegalArgumentException("没找到知道的记录");
        }
        record.setReturnStatus(AssetReturnStatus.RETURNED);
        record.setReturner(userService.currentLoginEmployee());
        record.setActualEndTime(System.currentTimeMillis());
        assetBorrowRecordRepository.save(record);
    }
}
