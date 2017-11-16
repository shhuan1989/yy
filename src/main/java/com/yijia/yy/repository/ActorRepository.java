package com.yijia.yy.repository;

import com.yijia.yy.domain.Actor;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * Spring Data JPA repository for the Actor entity.
 */
@SuppressWarnings("unused")
public interface ActorRepository extends JpaRepository<Actor,Long>, QueryDslPredicateExecutor<Actor> {

}
