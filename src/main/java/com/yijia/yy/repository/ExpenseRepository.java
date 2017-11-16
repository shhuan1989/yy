package com.yijia.yy.repository;

import com.yijia.yy.domain.Expense;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * Spring Data JPA repository for the Expense entity.
 */
@SuppressWarnings("unused")
public interface ExpenseRepository extends JpaRepository<Expense,Long>, QueryDslPredicateExecutor<Expense> {

}
