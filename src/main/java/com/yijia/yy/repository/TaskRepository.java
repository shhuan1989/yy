package com.yijia.yy.repository;

import com.yijia.yy.domain.Task;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Task entity.
 */
@SuppressWarnings("unused")
public interface TaskRepository extends JpaRepository<Task,Long>, QueryDslPredicateExecutor<Task> {

    @Query("select distinct task from Task task left join fetch task.owner left join fetch task.members left join fetch task.attachments left join fetch task.pictureInfos")
    List<Task> findAllWithEagerRelationships();

    @Query("select task from Task task left join fetch task.owner left join fetch task.members left join fetch task.attachments left join fetch task.pictureInfos where task.id =:id")
    Task findOneWithEagerRelationships(@Param("id") Long id);

}
