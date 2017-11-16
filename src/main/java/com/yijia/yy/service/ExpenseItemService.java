package com.yijia.yy.service;

import com.yijia.yy.service.dto.ExpenseItemDTO;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing ExpenseItem.
 */
public interface ExpenseItemService {

    /**
     * Save a expenseItem.
     *
     * @param expenseItemDTO the entity to save
     * @return the persisted entity
     */
    ExpenseItemDTO save(ExpenseItemDTO expenseItemDTO);

    /**
     *  Get all the expenseItems.
     *  
     *  @return the list of entities
     */
    List<ExpenseItemDTO> findAll();

    /**
     *  Get the "id" expenseItem.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ExpenseItemDTO findOne(Long id);

    /**
     *  Delete the "id" expenseItem.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
