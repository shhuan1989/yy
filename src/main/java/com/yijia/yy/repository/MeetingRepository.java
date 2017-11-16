package com.yijia.yy.repository;

import com.yijia.yy.domain.Meeting;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * Spring Data JPA repository for the Meeting entity.
 */
@SuppressWarnings("unused")
public interface MeetingRepository extends JpaRepository<Meeting,Long>, QueryDslPredicateExecutor<Meeting> {

}
