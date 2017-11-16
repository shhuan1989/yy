package com.yijia.yy.repository;

import com.yijia.yy.domain.OvertimeWork;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * Spring Data JPA repository for the OvertimeWork entity.
 */
@SuppressWarnings("unused")
public interface OvertimeWorkRepository extends JpaRepository<OvertimeWork,Long>, QueryDslPredicateExecutor<OvertimeWork> {

}
