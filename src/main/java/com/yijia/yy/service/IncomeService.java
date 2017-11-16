package com.yijia.yy.service;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.dto.IncomeDTO;
import org.springframework.data.domain.Sort;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Income.
 */
public interface IncomeService {

    /**
     * Save a income.
     *
     * @param incomeDTO the entity to save
     * @return the persisted entity
     */
    IncomeDTO save(IncomeDTO incomeDTO);

    /**
     *  Get all the incomes.
     *
     *  @param sort the order
     *  @return the list of entities
     */
    List<IncomeDTO> findAll(Sort sort);

    /**
     *  Get all the incomes.
     *
     *  @param predicate the predicate
     *  @param sort the order
     *  @return the list of entities
     */
    List<IncomeDTO> findAll(Predicate predicate, Sort sort);

    /**
     *  Get the "id" income.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    IncomeDTO findOne(Long id);

    /**
     *  Delete the "id" income.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
