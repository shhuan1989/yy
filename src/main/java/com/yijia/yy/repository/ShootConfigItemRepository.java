package com.yijia.yy.repository;

import com.yijia.yy.domain.ShootConfigItem;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * Spring Data JPA repository for the ShootConfigItem entity.
 */
@SuppressWarnings("unused")
public interface ShootConfigItemRepository extends JpaRepository<ShootConfigItem,Long>, QueryDslPredicateExecutor<ShootConfigItem> {

}
