package com.yijia.yy.service.impl;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.IncomeService;
import com.yijia.yy.domain.Income;
import com.yijia.yy.repository.IncomeRepository;
import com.yijia.yy.service.UtilService;
import com.yijia.yy.service.dto.IncomeDTO;
import com.yijia.yy.service.mapper.IncomeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing Income.
 */
@Service
@Transactional
public class IncomeServiceImpl implements IncomeService{

    private final Logger log = LoggerFactory.getLogger(IncomeServiceImpl.class);

    @Inject
    private IncomeRepository incomeRepository;

    @Inject
    private IncomeMapper incomeMapper;

    @Inject
    private UtilService utilService;

    /**
     * Save a income.
     *
     * @param incomeDTO the entity to save
     * @return the persisted entity
     */
    public IncomeDTO save(IncomeDTO incomeDTO) {
        log.debug("Request to save Income : {}", incomeDTO);
        Income income = incomeMapper.incomeDTOToIncome(incomeDTO);
        utilService.initApprovalItem(income);

        income = incomeRepository.save(income);
        IncomeDTO result = incomeMapper.incomeToIncomeDTO(income);
        return result;
    }

    /**
     *  Get all the incomes.
     *
     *  @param sort the order
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<IncomeDTO> findAll(Sort sort) {
        log.debug("Request to get all Incomes");
        List<IncomeDTO> result = incomeRepository.findAll(sort).stream()
            .map(incomeMapper::incomeToIncomeDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get all the incomes.
     *
     *  @param predicate the predicate
     *  @param sort the order
     *  @return the list of entities
     */
    public List<IncomeDTO> findAll(Predicate predicate, Sort sort) {
        if (predicate == null) {
            return findAll(sort);
        }

        return StreamSupport.stream(incomeRepository.findAll(predicate, sort).spliterator(), false)
            .map(incomeMapper::incomeToIncomeDTO)
            .collect(Collectors.toList());
    }

    /**
     *  Get one income by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public IncomeDTO findOne(Long id) {
        log.debug("Request to get Income : {}", id);
        Income income = incomeRepository.findOne(id);
        IncomeDTO incomeDTO = incomeMapper.incomeToIncomeDTO(income);
        return incomeDTO;
    }

    /**
     *  Delete the  income by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Income : {}", id);
        incomeRepository.delete(id);
    }
}
