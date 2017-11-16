package com.yijia.yy.repository;

import com.yijia.yy.domain.Comment;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * Spring Data JPA repository for the Comment entity.
 */
@SuppressWarnings("unused")
public interface CommentRepository extends JpaRepository<Comment,Long>, QueryDslPredicateExecutor<Comment> {

}
