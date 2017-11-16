package com.yijia.yy.repository;

import com.yijia.yy.domain.Approval;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Approval entity.
 */
@SuppressWarnings("unused")
public interface ApprovalRepository extends JpaRepository<Approval,Long> {

}
