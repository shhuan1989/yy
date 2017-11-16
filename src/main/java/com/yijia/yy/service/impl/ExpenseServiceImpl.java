package com.yijia.yy.service.impl;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.domain.Expense;
import com.yijia.yy.repository.ExpenseRepository;
import com.yijia.yy.service.ExpenseService;
import com.yijia.yy.service.UtilService;
import com.yijia.yy.service.dto.ExpenseDTO;
import com.yijia.yy.service.mapper.ExpenseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing Expense.
 */
@Service
@Transactional
public class ExpenseServiceImpl implements ExpenseService{

    private final Logger log = LoggerFactory.getLogger(ExpenseServiceImpl.class);

    @Inject
    private ExpenseRepository expenseRepository;

    @Inject
    private ExpenseMapper expenseMapper;

    @Inject
    private UtilService utilService;

    /**
     * Save a expense.
     *
     * @param expenseDTO the entity to save
     * @return the persisted entity
     */
    public ExpenseDTO save(ExpenseDTO expenseDTO) {
        log.debug("Request to save Expense : {}", expenseDTO);
        Expense expense = expenseMapper.expenseDTOToExpense(expenseDTO);

        utilService.initApprovalItem(expense);

        final Expense e = expense;
        if (expense.getItems() != null) {
            expense.getItems().forEach(i -> i.setExpense(e));
        }

        if (expense.getProject() != null && expense.getProject().getId() == null) {
            expense.setProject(null);
        }

        expense = expenseRepository.save(expense);
        ExpenseDTO result = expenseMapper.expenseToExpenseDTO(expense);
        return result;
    }

    /**
     *  Get all the expenses.
     *
     *  @param sort the order
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ExpenseDTO> findAll(Sort sort) {
        log.debug("Request to get all Expenses");
        List<ExpenseDTO> result = expenseRepository.findAll(sort).stream()
            .map(expenseMapper::expenseToExpenseDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get all the expenses.
     *
     *  @param predicate the predicate
     *  @param sort the order
     *  @return the list of entities
     */
    public List<ExpenseDTO> findAll(Predicate predicate, Sort sort) {
        if (predicate == null) {
            return findAll(sort);
        }

        return StreamSupport.stream(expenseRepository.findAll(predicate, sort).spliterator(), false)
            .map(expenseMapper::expenseToExpenseDTO)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one expense by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ExpenseDTO findOne(Long id) {
        log.debug("Request to get Expense : {}", id);
        Expense expense = expenseRepository.findOne(id);
        ExpenseDTO expenseDTO = expenseMapper.expenseToExpenseDTO(expense);
        return expenseDTO;
    }

    /**
     *  Delete the  expense by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Expense : {}", id);
        expenseRepository.delete(id);
    }
}
