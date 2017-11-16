package com.yijia.yy.service;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.dto.AssetBorrowRecordDTO;
import org.springframework.data.domain.Sort;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing AssetBorrowRecord.
 */
public interface AssetBorrowRecordService {

    /**
     * Save a assetBorrowRecord.
     *
     * @param assetBorrowRecordDTO the entity to save
     * @return the persisted entity
     */
    AssetBorrowRecordDTO save(AssetBorrowRecordDTO assetBorrowRecordDTO);

    /**
     *  Get all the assetBorrowRecords.
     *
     *  @param sort the order
     *  @return the list of entities
     */
    List<AssetBorrowRecordDTO> findAll(Sort sort);

    /**
     *  Get all the assetBorrowRecords.
     *
     *  @param predicate the predicate
     *  @param sort the order
     *  @return the list of entities
     */
    List<AssetBorrowRecordDTO> findAll(Predicate predicate, Sort sort);

    /**
     *  Get the "id" assetBorrowRecord.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    AssetBorrowRecordDTO findOne(Long id);

    /**
     *  Delete the "id" assetBorrowRecord.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     *  return the "id" assetBorrowRecord.
     *
     *  @param id the id of the entity
     */
    void returnItem(Long id);
}
