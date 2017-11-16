package com.yijia.yy.repository;

import com.yijia.yy.domain.ProjectTimeline;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * Spring Data JPA repository for the ProjectTimeline entity.
 */
@SuppressWarnings("unused")
public interface ProjectTimelineRepository extends JpaRepository<ProjectTimeline,Long>, QueryDslPredicateExecutor<ProjectTimeline> {

}
