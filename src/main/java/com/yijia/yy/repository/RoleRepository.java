package com.yijia.yy.repository;

import com.yijia.yy.domain.Role;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * Spring Data JPA repository for the Role entity.
 */
@SuppressWarnings("unused")
public interface RoleRepository extends JpaRepository<Role,Long>, QueryDslPredicateExecutor<Role> {

}
