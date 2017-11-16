package com.yijia.yy.service.impl;

import com.yijia.yy.domain.Approval;
import com.yijia.yy.domain.enumeration.ApprovalStatus;
import com.yijia.yy.service.ApprovalRequestService;
import com.yijia.yy.domain.ApprovalRequest;
import com.yijia.yy.repository.ApprovalRequestRepository;
import com.yijia.yy.service.UserService;
import com.yijia.yy.service.dto.ApprovalRequestDTO;
import com.yijia.yy.service.mapper.ApprovalRequestMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ApprovalRequest.
 */
@Service
@Transactional
public class ApprovalRequestServiceImpl implements ApprovalRequestService{

    private final Logger log = LoggerFactory.getLogger(ApprovalRequestServiceImpl.class);

    @Inject
    private ApprovalRequestRepository approvalRequestRepository;

    @Inject
    private ApprovalRequestMapper approvalRequestMapper;

    @Inject
    private UserService userService;

    /**
     * Save a approvalRequest.
     *
     * @param approvalRequestDTO the entity to save
     * @return the persisted entity
     */
    public synchronized ApprovalRequestDTO save(ApprovalRequestDTO approvalRequestDTO) {
        log.debug("Request to save ApprovalRequest : {}", approvalRequestDTO);
        ApprovalRequest approvalRequest = approvalRequestMapper.approvalRequestDTOToApprovalRequest(approvalRequestDTO);

        Long currentTimeMillis = System.currentTimeMillis();
        String uuid = UUID.randomUUID().toString();
        if (approvalRequest.getId() == null) {
            approvalRequest.setStatus(ApprovalStatus.NOT_START);
            approvalRequest.setActive(true);
            approvalRequest.setApplicant(userService.currentLoginEmployee());
            approvalRequest.setCorrelationId(uuid);
            approvalRequest.setCreateTime(currentTimeMillis);
            approvalRequest.setLastModifiedTime(currentTimeMillis);

            Approval p = approvalRequest.getApprovalRoot();
            while (p != null) {
                p.setStatus(ApprovalStatus.NOT_START);
                p.setCorrelationId(uuid);
                p.setCreateTime(currentTimeMillis);
                p.setLastModifiedTime(currentTimeMillis);
                p = p.getNextApproval();
            }
        } else {
            approvalRequest.setLastModifiedTime(currentTimeMillis);
            Approval p = approvalRequest.getApprovalRoot();
            while (p != null) {
                p.setLastModifiedTime(currentTimeMillis);
                p = p.getNextApproval();
            }
        }

        approvalRequest = approvalRequestRepository.save(approvalRequest);
        ApprovalRequestDTO result = approvalRequestMapper.approvalRequestToApprovalRequestDTO(approvalRequest);
        return result;
    }

    /**
     *  Get all the approvalRequests.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ApprovalRequestDTO> findAll() {
        log.debug("Request to get all ApprovalRequests");
        List<ApprovalRequestDTO> result = approvalRequestRepository.findAll().stream()
            .map(approvalRequestMapper::approvalRequestToApprovalRequestDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one approvalRequest by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ApprovalRequestDTO findOne(Long id) {
        log.debug("Request to get ApprovalRequest : {}", id);
        ApprovalRequest approvalRequest = approvalRequestRepository.findOne(id);
        ApprovalRequestDTO approvalRequestDTO = approvalRequestMapper.approvalRequestToApprovalRequestDTO(approvalRequest);
        return approvalRequestDTO;
    }

    /**
     *  Delete the  approvalRequest by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ApprovalRequest : {}", id);
        approvalRequestRepository.delete(id);
    }


    @Override
    public ApprovalRequestDTO start(Long id) {
        ApprovalRequest approvalRequest = approvalRequestRepository.findOne(id);
        if (approvalRequest == null) {
            return null;
        }

        approvalRequest.setStatus(ApprovalStatus.IN_PROGRESS);
        approvalRequest.getApprovalRoot().setStatus(ApprovalStatus.IN_PROGRESS);
        return approvalRequestMapper.approvalRequestToApprovalRequestDTO(approvalRequestRepository.save(approvalRequest));
    }
}
