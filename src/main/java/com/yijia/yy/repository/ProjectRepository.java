package com.yijia.yy.repository;

import com.yijia.yy.domain.Project;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Project entity.
 */
@SuppressWarnings("unused")
public interface ProjectRepository extends JpaRepository<Project,Long>, QueryDslPredicateExecutor<Project> {

    @Query("select distinct project from Project project left join fetch project.aes left join fetch project.members")
    List<Project> findAllWithEagerRelationships();

    @Query("select project from Project project left join fetch project.aes left join fetch project.members where project.id =:id")
    Project findOneWithEagerRelationships(@Param("id") Long id);


    Project findTopByIsHuaWeiOrderByIdDesc(Boolean isHuaWei);

    List<Project> findByIsHuaWeiOrderByIdDesc(Boolean isHuaWei);

}
