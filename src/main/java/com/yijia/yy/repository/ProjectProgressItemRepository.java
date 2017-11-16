package com.yijia.yy.repository;

import com.yijia.yy.domain.ProjectProgressItem;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ProjectProgressItem entity.
 */
@SuppressWarnings("unused")
public interface ProjectProgressItemRepository extends JpaRepository<ProjectProgressItem,Long> {

}
