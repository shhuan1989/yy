package com.yijia.yy.service;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.dto.ShootConfigDTO;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Service Interface for managing ShootConfig.
 */
public interface ShootConfigService {

    /**
     * Save a shootConfigs.
     *
     * @param shootConfigDTO the entity to save
     * @return the persisted entity
     */
    ShootConfigDTO save(ShootConfigDTO shootConfigDTO);

    /**
     *  Get all the shootConfigs.
     *
     *  @param sort the order
     *  @return the list of entities
     */
    List<ShootConfigDTO> findAll(Sort sort);

    /**
     *  Get all the shootConfigs.
     *
     *  @param predicate the predicate
     *  @param sort the order
     *  @return the list of entities
     */
    List<ShootConfigDTO> findAll(Predicate predicate, Sort sort);

    /**
     *  Get the "id" shootConfigs.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ShootConfigDTO findOne(Long id);

    /**
     *  Delete the "id" shootConfigs.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
