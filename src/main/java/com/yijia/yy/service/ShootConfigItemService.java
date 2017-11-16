package com.yijia.yy.service;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.dto.ShootConfigItemDTO;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Service Interface for managing ShootConfigItem.
 */
public interface ShootConfigItemService {

    /**
     * Save a shootConfig.
     *
     * @param shootConfigItemDTO the entity to save
     * @return the persisted entity
     */
    ShootConfigItemDTO save(ShootConfigItemDTO shootConfigItemDTO);

    /**
     *  Get all the shootConfigs.
     *
     *  @param sort the order
     *  @return the list of entities
     */
    List<ShootConfigItemDTO> findAll(Sort sort);


    /**
     *  Get all the shootConfigs.
     *
     *  @param predicate the predicate
     *  @param sort the order
     *  @return the list of entities
     */
    List<ShootConfigItemDTO> findAll(Predicate predicate, Sort sort);

    /**
     *  Get the "id" shootConfig.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ShootConfigItemDTO findOne(Long id);

    /**
     *  Delete the "id" shootConfig.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
