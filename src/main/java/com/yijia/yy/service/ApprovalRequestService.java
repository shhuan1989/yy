package com.yijia.yy.service;

import com.yijia.yy.service.dto.ApprovalRequestDTO;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing ApprovalRequest.
 */
public interface ApprovalRequestService {

    /**
     * Save a approvalRequest.
     *
     * @param approvalRequestDTO the entity to save
     * @return the persisted entity
     */
    ApprovalRequestDTO save(ApprovalRequestDTO approvalRequestDTO);

    /**
     * Get all the approvalRequests.
     *
     * @return the list of entities
     */
    List<ApprovalRequestDTO> findAll();

    /**
     * Get the "id" approvalRequest.
     *
     * @param id the id of the entity
     * @return the entity
     */
    ApprovalRequestDTO findOne(Long id);

    /**
     * Delete the "id" approvalRequest.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * start the approval process
     * @param id
     * @return
     */
    ApprovalRequestDTO start(Long id);
}
