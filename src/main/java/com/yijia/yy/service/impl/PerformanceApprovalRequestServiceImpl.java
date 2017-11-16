package com.yijia.yy.service.impl;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.domain.Approval;
import com.yijia.yy.domain.Employee;
import com.yijia.yy.domain.PerformanceApprovalRequest;
import com.yijia.yy.domain.Project;
import com.yijia.yy.domain.enumeration.ApprovalStatus;
import com.yijia.yy.repository.PerformanceApprovalRequestRepository;
import com.yijia.yy.repository.ProjectRepository;
import com.yijia.yy.service.PerformanceApprovalRequestService;
import com.yijia.yy.service.UserService;
import com.yijia.yy.service.dto.PerformanceApprovalRequestDTO;
import com.yijia.yy.service.mapper.PerformanceApprovalRequestMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class PerformanceApprovalRequestServiceImpl implements PerformanceApprovalRequestService{

    @Inject
    private PerformanceApprovalRequestRepository approvalRequestRepository;

    @Inject
    private PerformanceApprovalRequestMapper mapper;

    @Inject
    private UserService userService;

    @Inject
    private ProjectRepository projectRepository;

    @Override
    public PerformanceApprovalRequestDTO save(PerformanceApprovalRequestDTO approvalRequestDTO) {
        if (approvalRequestDTO == null) {
            return null;
        }

        PerformanceApprovalRequest approvalRequest = mapper.performanceApprovalRequestDTOToPerformanceApprovalRequest(approvalRequestDTO);

        Long currentTime = System.currentTimeMillis();
        String uuid = UUID.randomUUID().toString();
        if (approvalRequest.getId() == null) {
            Employee currentUser = userService.currentLoginEmployee();
            approvalRequest.setCreateTime(currentTime);
            approvalRequest.setApplicant(currentUser);
            approvalRequest.setLastModifiedTime(currentTime);
            approvalRequest.setStatus(ApprovalStatus.NOT_START);
            approvalRequest.setActive(true);
            approvalRequest.setCorrelationId(uuid);

            approvalRequest.getBonuses().forEach(b -> {
                b.setCreateTime(currentTime);
                b.setLastModifiedTime(currentTime);
                b.setCreator(currentUser);
            });

            Approval p = approvalRequest.getApprovalRoot();
            while (p != null) {
                p.setLastModifiedTime(currentTime);
                p.setCreateTime(currentTime);
                p.setStatus(ApprovalStatus.NOT_START);
                p.setCorrelationId(uuid);
                p = p.getNextApproval();
            }
        } else {
            approvalRequest.setLastModifiedTime(currentTime);
        }


        Set<PerformanceApprovalRequest> existsRequests = approvalRequestRepository.findAllByProjectId(approvalRequest.getProject().getId());
        if (existsRequests != null) {
            existsRequests.forEach(p -> {
                p.setStatus(ApprovalStatus.CANCELLED);
                p.setActive(false);
                approvalRequestRepository.save(p);
            });
        }

        if (approvalRequestDTO.getBonuses() != null) {
            Project project = projectRepository.findOne(approvalRequestDTO.getProjectId());
            project.setTotalBonus(
                approvalRequestDTO.getBonuses().stream()
                    .mapToDouble(b -> b.getAmount())
                    .sum()
            );
            projectRepository.save(project);
        }

        return mapper.performanceApprovalRequestToPerformanceApprovalRequestDTO(approvalRequestRepository.save(approvalRequest));
    }

    @Override
    public List<PerformanceApprovalRequestDTO> findAll(Sort sort) {
        return approvalRequestRepository.findAll(sort).stream()
            .map(p -> mapper.performanceApprovalRequestToPerformanceApprovalRequestDTO(p))
            .collect(Collectors.toList());
    }

    @Override
    public List<PerformanceApprovalRequestDTO> findAll(Predicate predicate, Sort sort) {
        return StreamSupport.stream(approvalRequestRepository.findAll(predicate, sort).spliterator(), false)
            .map(p -> mapper.performanceApprovalRequestToPerformanceApprovalRequestDTO(p))
            .collect(Collectors.toList());
    }

    @Override
    public PerformanceApprovalRequestDTO findOne(Long id) {
        return mapper.performanceApprovalRequestToPerformanceApprovalRequestDTO(approvalRequestRepository.findOne(id));
    }

    @Override
    public void delete(Long id) {
        approvalRequestRepository.delete(id);
    }

    @Override
    public PerformanceApprovalRequestDTO start(Long id) {
        PerformanceApprovalRequest approvalRequest = approvalRequestRepository.findOne(id);
        if (approvalRequest == null) {
            return null;
        }

        approvalRequest.setStatus(ApprovalStatus.IN_PROGRESS);
        approvalRequest.getApprovalRoot().setStatus(ApprovalStatus.IN_PROGRESS);
        return mapper.performanceApprovalRequestToPerformanceApprovalRequestDTO(approvalRequestRepository.save(approvalRequest));
    }

    @Override
    public void issue(Long id) {
        PerformanceApprovalRequest approvalRequest = approvalRequestRepository.findOne(id);
        if (approvalRequest != null) {
            approvalRequest.setIssued(true);
            approvalRequest.setIssueTime(System.currentTimeMillis());
            approvalRequest.setIssuer(userService.currentLoginEmployee());
            approvalRequestRepository.save(approvalRequest);
        }
    }
}
