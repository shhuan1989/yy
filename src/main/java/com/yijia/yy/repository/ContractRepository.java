package com.yijia.yy.repository;

import com.yijia.yy.domain.Contract;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * Spring Data JPA repository for the Contract entity.
 */
@SuppressWarnings("unused")
public interface ContractRepository extends JpaRepository<Contract,Long>, QueryDslPredicateExecutor<Contract> {
    Contract findTopByIdNumber(String name);
}
