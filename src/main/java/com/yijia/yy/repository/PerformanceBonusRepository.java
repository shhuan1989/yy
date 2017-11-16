package com.yijia.yy.repository;

import com.yijia.yy.domain.PerformanceBonus;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * Spring Data JPA repository for the PerformanceBonus entity.
 */
@SuppressWarnings("unused")
public interface PerformanceBonusRepository extends JpaRepository<PerformanceBonus,Long>, QueryDslPredicateExecutor<PerformanceBonus> {

    List<PerformanceBonus> findAllByProjectId(Long projectId);

}
