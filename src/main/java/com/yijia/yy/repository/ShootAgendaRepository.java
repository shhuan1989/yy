package com.yijia.yy.repository;

import com.yijia.yy.domain.ShootAgenda;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * Spring Data JPA repository for the ShootAgenda entity.
 */
@SuppressWarnings("unused")
public interface ShootAgendaRepository extends JpaRepository<ShootAgenda,Long>, QueryDslPredicateExecutor<ShootAgenda> {

}
