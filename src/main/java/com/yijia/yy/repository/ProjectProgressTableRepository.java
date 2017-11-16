package com.yijia.yy.repository;

import com.yijia.yy.domain.ProjectProgressTable;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * Spring Data JPA repository for the ProjectProgressTable entity.
 */
@SuppressWarnings("unused")
public interface ProjectProgressTableRepository extends JpaRepository<ProjectProgressTable,Long>, QueryDslPredicateExecutor<ProjectProgressTable> {

}
