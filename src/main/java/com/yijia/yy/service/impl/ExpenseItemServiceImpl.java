package com.yijia.yy.service.impl;

import com.yijia.yy.service.ExpenseItemService;
import com.yijia.yy.domain.ExpenseItem;
import com.yijia.yy.repository.ExpenseItemRepository;
import com.yijia.yy.service.dto.ExpenseItemDTO;
import com.yijia.yy.service.mapper.ExpenseItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ExpenseItem.
 */
@Service
@Transactional
public class ExpenseItemServiceImpl implements ExpenseItemService{

    private final Logger log = LoggerFactory.getLogger(ExpenseItemServiceImpl.class);
    
    @Inject
    private ExpenseItemRepository expenseItemRepository;

    @Inject
    private ExpenseItemMapper expenseItemMapper;

    /**
     * Save a expenseItem.
     *
     * @param expenseItemDTO the entity to save
     * @return the persisted entity
     */
    public ExpenseItemDTO save(ExpenseItemDTO expenseItemDTO) {
        log.debug("Request to save ExpenseItem : {}", expenseItemDTO);
        ExpenseItem expenseItem = expenseItemMapper.expenseItemDTOToExpenseItem(expenseItemDTO);
        expenseItem = expenseItemRepository.save(expenseItem);
        ExpenseItemDTO result = expenseItemMapper.expenseItemToExpenseItemDTO(expenseItem);
        return result;
    }

    /**
     *  Get all the expenseItems.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<ExpenseItemDTO> findAll() {
        log.debug("Request to get all ExpenseItems");
        List<ExpenseItemDTO> result = expenseItemRepository.findAll().stream()
            .map(expenseItemMapper::expenseItemToExpenseItemDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one expenseItem by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ExpenseItemDTO findOne(Long id) {
        log.debug("Request to get ExpenseItem : {}", id);
        ExpenseItem expenseItem = expenseItemRepository.findOne(id);
        ExpenseItemDTO expenseItemDTO = expenseItemMapper.expenseItemToExpenseItemDTO(expenseItem);
        return expenseItemDTO;
    }

    /**
     *  Delete the  expenseItem by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ExpenseItem : {}", id);
        expenseItemRepository.delete(id);
    }
}
