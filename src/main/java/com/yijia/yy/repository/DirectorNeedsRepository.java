package com.yijia.yy.repository;

import com.yijia.yy.domain.DirectorNeeds;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * Spring Data JPA repository for the DirectorNeeds entity.
 */
@SuppressWarnings("unused")
public interface DirectorNeedsRepository extends JpaRepository<DirectorNeeds,Long>, QueryDslPredicateExecutor<DirectorNeeds> {

}
