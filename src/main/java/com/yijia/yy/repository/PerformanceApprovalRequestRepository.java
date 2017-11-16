package com.yijia.yy.repository;

import com.yijia.yy.domain.PerformanceApprovalRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.Set;

public interface PerformanceApprovalRequestRepository extends JpaRepository<PerformanceApprovalRequest,Long>, QueryDslPredicateExecutor<PerformanceApprovalRequest> {
    Set<PerformanceApprovalRequest> findAllByProjectId(Long id);
    PerformanceApprovalRequest findBycorrelationId(Long id);
}
