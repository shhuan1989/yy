package com.yijia.yy.service.impl;

import com.yijia.yy.service.ProjectPaymentService;
import com.yijia.yy.domain.ProjectPayment;
import com.yijia.yy.repository.ProjectPaymentRepository;
import com.yijia.yy.service.dto.ProjectPaymentDTO;
import com.yijia.yy.service.mapper.ProjectPaymentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ProjectPayment.
 */
@Service
@Transactional
public class ProjectPaymentServiceImpl implements ProjectPaymentService{

    private final Logger log = LoggerFactory.getLogger(ProjectPaymentServiceImpl.class);
    
    @Inject
    private ProjectPaymentRepository projectPaymentRepository;

    @Inject
    private ProjectPaymentMapper projectPaymentMapper;

    /**
     * Save a projectPayment.
     *
     * @param projectPaymentDTO the entity to save
     * @return the persisted entity
     */
    public ProjectPaymentDTO save(ProjectPaymentDTO projectPaymentDTO) {
        log.debug("Request to save ProjectPayment : {}", projectPaymentDTO);
        ProjectPayment projectPayment = projectPaymentMapper.projectPaymentDTOToProjectPayment(projectPaymentDTO);
        projectPayment = projectPaymentRepository.save(projectPayment);
        ProjectPaymentDTO result = projectPaymentMapper.projectPaymentToProjectPaymentDTO(projectPayment);
        return result;
    }

    /**
     *  Get all the projectPayments.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<ProjectPaymentDTO> findAll() {
        log.debug("Request to get all ProjectPayments");
        List<ProjectPaymentDTO> result = projectPaymentRepository.findAll().stream()
            .map(projectPaymentMapper::projectPaymentToProjectPaymentDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one projectPayment by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ProjectPaymentDTO findOne(Long id) {
        log.debug("Request to get ProjectPayment : {}", id);
        ProjectPayment projectPayment = projectPaymentRepository.findOne(id);
        ProjectPaymentDTO projectPaymentDTO = projectPaymentMapper.projectPaymentToProjectPaymentDTO(projectPayment);
        return projectPaymentDTO;
    }

    /**
     *  Delete the  projectPayment by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProjectPayment : {}", id);
        projectPaymentRepository.delete(id);
    }
}
