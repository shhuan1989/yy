package com.yijia.yy.repository;

import com.yijia.yy.domain.Asset;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * Spring Data JPA repository for the Asset entity.
 */
@SuppressWarnings("unused")
public interface AssetRepository extends JpaRepository<Asset,Long>, QueryDslPredicateExecutor<Asset> {

}
