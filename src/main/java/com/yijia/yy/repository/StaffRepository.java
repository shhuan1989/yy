package com.yijia.yy.repository;

import com.yijia.yy.domain.Staff;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * Spring Data JPA repository for the Staff entity.
 */
@SuppressWarnings("unused")
public interface StaffRepository extends JpaRepository<Staff,Long>,QueryDslPredicateExecutor<Staff> {

}
