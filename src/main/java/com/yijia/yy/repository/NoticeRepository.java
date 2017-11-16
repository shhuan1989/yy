package com.yijia.yy.repository;

import com.yijia.yy.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Notice entity.
 */
@SuppressWarnings("unused")
public interface NoticeRepository extends JpaRepository<Notice, Long>, QueryDslPredicateExecutor<Notice> {

    @Query("select distinct notice from Notice notice left join fetch notice.depts left join fetch notice.projects " +
        "left join fetch notice.employees")
    List<Notice> findAllWithEagerRelationships();

    @Query("select notice from Notice notice left join fetch notice.depts left join fetch notice.projects left join " +
        "fetch notice.employees where notice.id =:id")
    Notice findOneWithEagerRelationships(@Param("id") Long id);

}
