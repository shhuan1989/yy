package com.yijia.yy.repository;

import com.yijia.yy.domain.ProjectRate;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * Spring Data JPA repository for the ProjectRate entity.
 */
@SuppressWarnings("unused")
public interface ProjectRateRepository extends JpaRepository<ProjectRate,Long>, QueryDslPredicateExecutor<ProjectRate> {

    Long countByFinishedAndProjectId(boolean finished, Long projectId);
    List<ProjectRate> findAllByProjectId(Long projectId);

}
