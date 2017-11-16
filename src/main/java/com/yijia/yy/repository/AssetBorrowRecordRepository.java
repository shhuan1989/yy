package com.yijia.yy.repository;

import com.yijia.yy.domain.AssetBorrowRecord;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * Spring Data JPA repository for the AssetBorrowRecord entity.
 */
@SuppressWarnings("unused")
public interface AssetBorrowRecordRepository extends JpaRepository<AssetBorrowRecord,Long>, QueryDslPredicateExecutor<AssetBorrowRecord> {

    AssetBorrowRecord findOneByCorrelationId(String correlationId);
}
