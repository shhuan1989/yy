package com.yijia.yy.repository;

import com.yijia.yy.domain.Dept;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Dept entity.
 */
@SuppressWarnings("unused")
public interface DeptRepository extends JpaRepository<Dept,Long> {

    Dept findTopByIdIsNotNull();

}
