package com.yijia.yy.service.impl;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.PerformanceBonusService;
import com.yijia.yy.domain.PerformanceBonus;
import com.yijia.yy.repository.PerformanceBonusRepository;
import com.yijia.yy.service.dto.PerformanceBonusDTO;
import com.yijia.yy.service.mapper.PerformanceBonusMapper;
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
 * Service Implementation for managing PerformanceBonus.
 */
@Service
@Transactional
public class PerformanceBonusServiceImpl implements PerformanceBonusService{

    private final Logger log = LoggerFactory.getLogger(PerformanceBonusServiceImpl.class);

    @Inject
    private PerformanceBonusRepository performanceBonusRepository;

    @Inject
    private PerformanceBonusMapper performanceBonusMapper;

    /**
     * Save a performanceBonus.
     *
     * @param performanceBonusDTO the entity to save
     * @return the persisted entity
     */
    public PerformanceBonusDTO save(PerformanceBonusDTO performanceBonusDTO) {
        log.debug("Request to save PerformanceBonus : {}", performanceBonusDTO);
        PerformanceBonus performanceBonus = performanceBonusMapper.performanceBonusDTOToPerformanceBonus(performanceBonusDTO);
        performanceBonus = performanceBonusRepository.save(performanceBonus);
        PerformanceBonusDTO result = performanceBonusMapper.performanceBonusToPerformanceBonusDTO(performanceBonus);
        return result;
    }

    /**
     *  Get all the performanceBonuses.
     *
     *  @param sort the order
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<PerformanceBonusDTO> findAll(Sort sort) {
        log.debug("Request to get all PerformanceBonuses");
        List<PerformanceBonusDTO> result = performanceBonusRepository.findAll(sort).stream()
            .map(performanceBonusMapper::performanceBonusToPerformanceBonusDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get all the performanceBonuses by predicate.
     *
     *  @param predicate the predicate
     *  @param sort the order
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<PerformanceBonusDTO> findAll(Predicate predicate, Sort sort) {
        log.debug("Request to get all PerformanceBonuses");
        List<PerformanceBonusDTO> result = StreamSupport.stream(performanceBonusRepository.findAll(predicate, sort).spliterator(), false)
            .map(performanceBonusMapper::performanceBonusToPerformanceBonusDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one performanceBonus by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public PerformanceBonusDTO findOne(Long id) {
        log.debug("Request to get PerformanceBonus : {}", id);
        PerformanceBonus performanceBonus = performanceBonusRepository.findOne(id);
        PerformanceBonusDTO performanceBonusDTO = performanceBonusMapper.performanceBonusToPerformanceBonusDTO(performanceBonus);
        return performanceBonusDTO;
    }

    /**
     *  Delete the  performanceBonus by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PerformanceBonus : {}", id);
        performanceBonusRepository.delete(id);
    }
}
