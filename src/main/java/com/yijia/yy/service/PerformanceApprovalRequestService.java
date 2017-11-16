package com.yijia.yy.service;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.dto.ApprovalRequestDTO;
import com.yijia.yy.service.dto.PerformanceApprovalRequestDTO;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface PerformanceApprovalRequestService {

    /**
     * Save a approvalRequest.
     *
     * @param approvalRequestDTO the entity to save
     * @return the persisted entity
     */
    PerformanceApprovalRequestDTO save(PerformanceApprovalRequestDTO approvalRequestDTO);

    /**
     *  Get all the approvalRequests.
     *
     *  @return the list of entities
     */
    List<PerformanceApprovalRequestDTO> findAll(Sort sort);

    List<PerformanceApprovalRequestDTO> findAll(Predicate predicate, Sort sort);

    /**
     *  Get the "id" approvalRequest.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PerformanceApprovalRequestDTO findOne(Long id);

    /**
     *  Delete the "id" approvalRequest.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * start the approval process
     * @param id
     * @return
     */
    PerformanceApprovalRequestDTO start(Long id);

    void issue(Long id);
}
