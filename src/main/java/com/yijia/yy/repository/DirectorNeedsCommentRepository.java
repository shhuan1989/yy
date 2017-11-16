package com.yijia.yy.repository;

import com.yijia.yy.domain.DirectorNeedsComment;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the DirectorNeedsComment entity.
 */
@SuppressWarnings("unused")
public interface DirectorNeedsCommentRepository extends JpaRepository<DirectorNeedsComment,Long> {

}
