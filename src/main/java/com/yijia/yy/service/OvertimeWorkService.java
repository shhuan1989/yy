package com.yijia.yy.service;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.dto.OvertimeWorkDTO;
import org.springframework.data.domain.Sort;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing OvertimeWork.
 */
public interface OvertimeWorkService {

    /**
     * Save a overtimeWork.
     *
     * @param overtimeWorkDTO the entity to save
     * @return the persisted entity
     */
    OvertimeWorkDTO save(OvertimeWorkDTO overtimeWorkDTO);

    /**
     *  Get all the overtimeWorks.
     *
     *  @param sort the order
     *  @return the list of entities
     */
    List<OvertimeWorkDTO> findAll(Sort sort);

    /**
     *  Get all the overtimeWorks.
     *
     *  @param predicate the predicate
     *  @param sort the order
     *  @return the list of entities
     */
    List<OvertimeWorkDTO> findAll(Predicate predicate, Sort sort);

    /**
     *  Get the "id" overtimeWork.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    OvertimeWorkDTO findOne(Long id);

    /**
     *  Delete the "id" overtimeWork.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
