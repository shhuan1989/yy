package com.yijia.yy.repository;

import com.yijia.yy.domain.Income;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * Spring Data JPA repository for the Income entity.
 */
@SuppressWarnings("unused")
public interface IncomeRepository extends JpaRepository<Income,Long>, QueryDslPredicateExecutor<Income> {

}
