package com.yijia.yy.repository;

import com.yijia.yy.domain.ShootConfig;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * Spring Data JPA repository for the ShootConfig entity.
 */
@SuppressWarnings("unused")
public interface ShootConfigRepository extends JpaRepository<ShootConfig,Long>, QueryDslPredicateExecutor<ShootConfig> {

    public ShootConfig findOneByCorrelationId(String correlationId);
}
