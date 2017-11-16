package com.yijia.yy.service.impl;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.UserService;
import com.yijia.yy.service.UtilService;
import com.yijia.yy.service.VacationService;
import com.yijia.yy.domain.Vacation;
import com.yijia.yy.repository.VacationRepository;
import com.yijia.yy.service.dto.VacationDTO;
import com.yijia.yy.service.mapper.VacationMapper;
import org.apache.commons.lang3.BooleanUtils;
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
 * Service Implementation for managing Vacation.
 */
@Service
@Transactional
public class VacationServiceImpl implements VacationService{

    private final Logger log = LoggerFactory.getLogger(VacationServiceImpl.class);

    @Inject
    private VacationRepository vacationRepository;

    @Inject
    private VacationMapper vacationMapper;

    @Inject
    private UtilService utilService;

    /**
     * Save a vacation.
     *
     * @param vacationDTO the entity to save
     * @return the persisted entity
     */
    public VacationDTO save(VacationDTO vacationDTO) {
        log.debug("Request to save Vacation : {}", vacationDTO);
        Vacation vacation = vacationMapper.vacationDTOToVacation(vacationDTO);
        utilService.initApprovalItem(vacation);
        vacation = vacationRepository.save(vacation);
        VacationDTO result = vacationMapper.vacationToVacationDTO(vacation);
        return result;
    }

    /**
     *  Get all the vacations.
     *
     *  @param sort the order
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<VacationDTO> findAll(Sort sort) {
        log.debug("Request to get all Vacations");
        List<VacationDTO> result = vacationRepository.findAll(sort).stream()
            .map(vacationMapper::vacationToVacationDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }


    /**
     *  Get all the vacations.
     *
     *  @param predicate the predicate
     *  @param sort the order
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<VacationDTO> findAll(Predicate predicate, Sort sort) {
        log.debug("Request to get all Vacations");
        List<VacationDTO> result = StreamSupport.stream(vacationRepository.findAll(predicate, sort).spliterator(), false)
            .map(vacationMapper::vacationToVacationDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one vacation by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public VacationDTO findOne(Long id) {
        log.debug("Request to get Vacation : {}", id);
        Vacation vacation = vacationRepository.findOne(id);
        VacationDTO vacationDTO = vacationMapper.vacationToVacationDTO(vacation);
        return vacationDTO;
    }

    /**
     *  Delete the  vacation by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Vacation : {}", id);
        vacationRepository.delete(id);
    }
}
