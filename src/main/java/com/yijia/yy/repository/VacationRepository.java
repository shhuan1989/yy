package com.yijia.yy.repository;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.domain.Vacation;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * Spring Data JPA repository for the Vacation entity.
 */
@SuppressWarnings("unused")
public interface VacationRepository extends JpaRepository<Vacation,Long>, QueryDslPredicateExecutor<Vacation> {
}
