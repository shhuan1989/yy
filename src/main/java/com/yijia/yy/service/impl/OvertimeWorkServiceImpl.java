package com.yijia.yy.service.impl;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.domain.Approval;
import com.yijia.yy.domain.enumeration.ApprovalStatus;
import com.yijia.yy.service.OvertimeWorkService;
import com.yijia.yy.domain.OvertimeWork;
import com.yijia.yy.repository.OvertimeWorkRepository;
import com.yijia.yy.service.UserService;
import com.yijia.yy.service.UtilService;
import com.yijia.yy.service.dto.OvertimeWorkDTO;
import com.yijia.yy.service.mapper.OvertimeWorkMapper;
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
 * Service Implementation for managing OvertimeWork.
 */
@Service
@Transactional
public class OvertimeWorkServiceImpl implements OvertimeWorkService{

    private final Logger log = LoggerFactory.getLogger(OvertimeWorkServiceImpl.class);

    @Inject
    private OvertimeWorkRepository overtimeWorkRepository;

    @Inject
    private OvertimeWorkMapper overtimeWorkMapper;

    @Inject
    private UtilService utilService;

    /**
     * Save a overtimeWork.
     *
     * @param overtimeWorkDTO the entity to save
     * @return the persisted entity
     */
    public OvertimeWorkDTO save(OvertimeWorkDTO overtimeWorkDTO) {
        log.debug("Request to save OvertimeWork : {}", overtimeWorkDTO);
        OvertimeWork overtimeWork = overtimeWorkMapper.overtimeWorkDTOToOvertimeWork(overtimeWorkDTO);

        utilService.initApprovalItem(overtimeWork);
        overtimeWork = overtimeWorkRepository.save(overtimeWork);
        OvertimeWorkDTO result = overtimeWorkMapper.overtimeWorkToOvertimeWorkDTO(overtimeWork);
        return result;
    }

    /**
     *  Get all the overtimeWorks.
     *
     *  @param sort the order
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<OvertimeWorkDTO> findAll(Sort sort) {
        log.debug("Request to get all OvertimeWorks");
        List<OvertimeWorkDTO> result = overtimeWorkRepository.findAll(sort).stream()
            .map(overtimeWorkMapper::overtimeWorkToOvertimeWorkDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get all the overtimeWorks.
     *
     *  @param predicate the predicate
     *  @param sort the order
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<OvertimeWorkDTO> findAll(Predicate predicate, Sort sort) {
        log.debug("Request to get all OvertimeWorks");
        List<OvertimeWorkDTO> result = StreamSupport.stream(overtimeWorkRepository.findAll(predicate, sort).spliterator(), false)
            .map(overtimeWorkMapper::overtimeWorkToOvertimeWorkDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one overtimeWork by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public OvertimeWorkDTO findOne(Long id) {
        log.debug("Request to get OvertimeWork : {}", id);
        OvertimeWork overtimeWork = overtimeWorkRepository.findOne(id);
        OvertimeWorkDTO overtimeWorkDTO = overtimeWorkMapper.overtimeWorkToOvertimeWorkDTO(overtimeWork);
        return overtimeWorkDTO;
    }

    /**
     *  Delete the  overtimeWork by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete OvertimeWork : {}", id);
        overtimeWorkRepository.delete(id);
    }
}
