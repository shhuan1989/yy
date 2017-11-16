package com.yijia.yy.repository;

import com.yijia.yy.domain.ProjectCost;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * Spring Data JPA repository for the ProjectCost entity.
 */
@SuppressWarnings("unused")
public interface ProjectCostRepository extends JpaRepository<ProjectCost,Long>, QueryDslPredicateExecutor<ProjectCost> {

}
