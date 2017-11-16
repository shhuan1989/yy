package com.yijia.yy.service;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.dto.AssetDTO;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Service Interface for managing Asset.
 */
public interface AssetService {

    /**
     * Save a asset.
     *
     * @param assetDTO the entity to save
     * @return the persisted entity
     */
    AssetDTO save(AssetDTO assetDTO);

    /**
     *  Get all the assets.
     *
     *  @param sort the order
     *  @return the list of entities
     */
    List<AssetDTO> findAll(Sort sort);


    /**
     *  Get all the assets.
     *
     *  @param predicate the predicate
     *  @param sort the order
     *  @return the list of entities
     */
    List<AssetDTO> findAll(Predicate predicate, Sort sort);

    /**
     *  Get the "id" asset.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    AssetDTO findOne(Long id);

    /**
     *  Delete the "id" asset.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
