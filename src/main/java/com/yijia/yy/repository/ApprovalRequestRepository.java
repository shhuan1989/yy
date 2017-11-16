package com.yijia.yy.repository;

import com.yijia.yy.domain.ApprovalRequest;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ApprovalRequest entity.
 */
@SuppressWarnings("unused")
public interface ApprovalRequestRepository extends JpaRepository<ApprovalRequest,Long> {

    ApprovalRequest findBycorrelationId(String correlationId);
}
