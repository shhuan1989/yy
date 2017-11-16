package com.yijia.yy.service;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.dto.ExpenseDTO;
import org.springframework.data.domain.Sort;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Expense.
 */
public interface ExpenseService {

    /**
     * Save a expense.
     *
     * @param expenseDTO the entity to save
     * @return the persisted entity
     */
    ExpenseDTO save(ExpenseDTO expenseDTO);

    /**
     *  Get all the expenses.
     *
     *  @param sort the order
     *  @return the list of entities
     */
    List<ExpenseDTO> findAll(Sort sort);

    /**
     *  Get all the expenses.
     *
     *  @param predicate the predicate
     *  @param sort the order
     *  @return the list of entities
     */
    List<ExpenseDTO> findAll(Predicate predicate, Sort sort);

    /**
     *  Get the "id" expense.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ExpenseDTO findOne(Long id);

    /**
     *  Delete the "id" expense.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
