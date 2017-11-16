package com.yijia.yy.service;

import com.yijia.yy.service.dto.VacationDTO;
import org.springframework.data.domain.Sort;

import java.util.LinkedList;
import java.util.List;
import com.querydsl.core.types.Predicate;

/**
 * Service Interface for managing Vacation.
 */
public interface VacationService {

    /**
     * Save a vacation.
     *
     * @param vacationDTO the entity to save
     * @return the persisted entity
     */
    VacationDTO save(VacationDTO vacationDTO);

    /**
     *  Get all the vacations.
     *
     *  @param sort the order
     *  @return the list of entities
     */
    List<VacationDTO> findAll(Sort sort);

    List<VacationDTO> findAll(Predicate predicate, Sort sort);

    /**
     *  Get the "id" vacation.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    VacationDTO findOne(Long id);

    /**
     *  Delete the "id" vacation.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
