package com.yijia.yy.repository;

import com.yijia.yy.domain.Client;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * Spring Data JPA repository for the Client entity.
 */
@SuppressWarnings("unused")
public interface ClientRepository extends JpaRepository<Client,Long>, QueryDslPredicateExecutor<Client> {

}
