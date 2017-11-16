package com.yijia.yy.service.impl;

import com.yijia.yy.domain.Approval;
import com.yijia.yy.domain.ApprovalRequest;
import com.yijia.yy.domain.Asset;
import com.yijia.yy.domain.AssetBorrowRecord;
import com.yijia.yy.domain.enumeration.ApprovalStatus;
import com.yijia.yy.exception.OutOfRepoException;
import com.yijia.yy.repository.*;
import com.yijia.yy.service.ApprovalService;
import com.yijia.yy.service.dto.ApprovalDTO;
import com.yijia.yy.service.event.ApprovalEvent;
import com.yijia.yy.service.mapper.ApprovalMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.bus.Event;
import reactor.bus.EventBus;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Approval.
 */
@Service
@Transactional
public class ApprovalServiceImpl implements ApprovalService{

    private final Logger log = LoggerFactory.getLogger(ApprovalServiceImpl.class);

    @Inject
    private ApprovalRepository approvalRepository;

    @Inject
    private ApprovalMapper approvalMapper;

    @Inject
    private ProjectRepository projectRepository;

    @Inject
    private ApprovalRequestRepository approvalRequestRepository;

    @Inject
    private AssetBorrowRecordRepository assetBorrowRecordRepository;

    @Inject
    private AssetRepository assetRepository;

    @Inject
    private EventBus eventBus;

    /**
     * Save a approval.
     *
     * @param approvalDTO the entity to save
     * @return the persisted entity
     */
    public ApprovalDTO save(ApprovalDTO approvalDTO) {
        log.debug("Request to save Approval : {}", approvalDTO);
        Approval approval = approvalMapper.approvalDTOToApproval(approvalDTO);
        approval = approvalRepository.save(approval);
        ApprovalDTO result = approvalMapper.approvalToApprovalDTO(approval);
        return result;
    }

    /**
     *  Get all the approvals.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ApprovalDTO> findAll() {
        log.debug("Request to get all Approvals");
        List<ApprovalDTO> result = approvalRepository.findAll().stream()
            .map(approvalMapper::approvalToApprovalDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one approval by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ApprovalDTO findOne(Long id) {
        log.debug("Request to get Approval : {}", id);
        Approval approval = approvalRepository.findOne(id);
        ApprovalDTO approvalDTO = approvalMapper.approvalToApprovalDTO(approval);
        return approvalDTO;
    }

    /**
     *  Delete the  approval by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Approval : {}", id);
        approvalRepository.delete(id);
    }

    @Override
    public ApprovalDTO approval(Long id) throws OutOfRepoException {
        Approval approval = approvalRepository.findOne(id);
        if (approval == null) {
            return null;
        }

        if (approval.getNextApproval() != null) {
            approval.getNextApproval().setStatus(ApprovalStatus.IN_PROGRESS);
            updateApprovalStatus(approval.getCorrelationId(), ApprovalStatus.IN_PROGRESS, null);
        } else {
            AssetBorrowRecord assetBorrowRecord = assetBorrowRecordRepository.findOneByCorrelationId(approval.getCorrelationId());
            if (assetBorrowRecord != null) {
                Asset asset = assetRepository.findOne(assetBorrowRecord.getAsset().getId());
                if (asset.getAmount() < assetBorrowRecord.getAmount()) {
                    throw new OutOfRepoException();
                }
            }

            updateApprovalStatus(approval.getCorrelationId(), ApprovalStatus.APPROVED, null);
            eventBus.notify("approval-item", Event.wrap(new ApprovalEvent().correlationId(approval.getCorrelationId())));
        }

        approval.setStatus(ApprovalStatus.APPROVED);
        return approvalMapper.approvalToApprovalDTO(approvalRepository.save(approval));
    }

    @Override
    public ApprovalDTO reject(Long id, String reason) {
        Approval approval = approvalRepository.findOne(id);
        if (approval == null) {
            return null;
        }

        approval.setStatus(ApprovalStatus.REJECTED);
        approval.setResult(reason);
        updateApprovalStatus(approval.getCorrelationId(), ApprovalStatus.REJECTED, reason);
        return approvalMapper.approvalToApprovalDTO(approvalRepository.save(approval));
    }

    private void updateApprovalStatus(String correlationId, ApprovalStatus status, String reason) {
        if (correlationId == null) {
            return;
        }

        ApprovalRequest approvalRequest = approvalRequestRepository.findBycorrelationId(correlationId);
        approvalRequest.setStatus(status);
        approvalRequest.setResult(reason);
        approvalRequestRepository.save(approvalRequest);
    }
}
