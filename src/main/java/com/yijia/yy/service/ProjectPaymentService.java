package com.yijia.yy.service;

import com.yijia.yy.service.dto.ProjectPaymentDTO;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing ProjectPayment.
 */
public interface ProjectPaymentService {

    /**
     * Save a projectPayment.
     *
     * @param projectPaymentDTO the entity to save
     * @return the persisted entity
     */
    ProjectPaymentDTO save(ProjectPaymentDTO projectPaymentDTO);

    /**
     *  Get all the projectPayments.
     *  
     *  @return the list of entities
     */
    List<ProjectPaymentDTO> findAll();

    /**
     *  Get the "id" projectPayment.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ProjectPaymentDTO findOne(Long id);

    /**
     *  Delete the "id" projectPayment.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
