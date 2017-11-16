package com.yijia.yy.repository;

import com.yijia.yy.domain.Equipment;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * Spring Data JPA repository for the Equipment entity.
 */
@SuppressWarnings("unused")
public interface EquipmentRepository extends JpaRepository<Equipment,Long>, QueryDslPredicateExecutor<Equipment> {

}
