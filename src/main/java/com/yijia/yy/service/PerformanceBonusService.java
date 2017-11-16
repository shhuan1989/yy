package com.yijia.yy.service;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.dto.PerformanceBonusDTO;
import org.springframework.data.domain.Sort;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing PerformanceBonus.
 */
public interface PerformanceBonusService {

    /**
     * Save a performanceBonus.
     *
     * @param performanceBonusDTO the entity to save
     * @return the persisted entity
     */
    PerformanceBonusDTO save(PerformanceBonusDTO performanceBonusDTO);

    /**
     *  Get all the performanceBonuses.
     *
     *  @param sort the order
     *  @return the list of entities
     */
    List<PerformanceBonusDTO> findAll(Sort sort);


    /**
     *  Get all the performanceBonuses.
     *
     *  @param predicate the predicate
     *  @param sort the order
     *  @return the list of entities
     */
    List<PerformanceBonusDTO> findAll(Predicate predicate, Sort sort);

    /**
     *  Get the "id" performanceBonus.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PerformanceBonusDTO findOne(Long id);

    /**
     *  Delete the "id" performanceBonus.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
