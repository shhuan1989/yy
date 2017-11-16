package com.yijia.yy.repository;

import com.yijia.yy.domain.ContractInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * Spring Data JPA repository for the ContractInvoice entity.
 */
@SuppressWarnings("unused")
public interface ContractInvoiceRepository extends JpaRepository<ContractInvoice,Long>, QueryDslPredicateExecutor<ContractInvoice> {

}
