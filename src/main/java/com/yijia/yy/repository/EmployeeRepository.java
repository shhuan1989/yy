package com.yijia.yy.repository;

import com.yijia.yy.domain.City;
import com.yijia.yy.domain.Employee;

import com.yijia.yy.domain.User;
import com.yijia.yy.domain.enumeration.Education;
import com.yijia.yy.domain.enumeration.Gender;
import com.yijia.yy.service.dto.EmployeeSearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Employee entity.
 */
@SuppressWarnings("unused")
public interface EmployeeRepository extends JpaRepository<Employee,Long>, QueryDslPredicateExecutor<Employee> {

    Optional<Employee> findOneByUser(User user);

}
