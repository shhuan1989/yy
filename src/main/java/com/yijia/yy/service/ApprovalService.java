package com.yijia.yy.service;

import com.yijia.yy.exception.OutOfRepoException;
import com.yijia.yy.service.dto.ApprovalDTO;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Approval.
 */
public interface ApprovalService {

    /**
     * Save a approval.
     *
     * @param approvalDTO the entity to save
     * @return the persisted entity
     */
    ApprovalDTO save(ApprovalDTO approvalDTO);

    /**
     *  Get all the approvals.
     *
     *  @return the list of entities
     */
    List<ApprovalDTO> findAll();

    /**
     *  Get the "id" approval.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ApprovalDTO findOne(Long id);

    /**
     *  Delete the "id" approval.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    ApprovalDTO approval(Long id) throws OutOfRepoException;

    ApprovalDTO reject(Long id, String reason);
}
