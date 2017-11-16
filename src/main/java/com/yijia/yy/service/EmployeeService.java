package com.yijia.yy.service;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.domain.Employee;
import com.yijia.yy.service.dto.EmployeeDTO;
import com.yijia.yy.service.dto.EmployeeSearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Employee.
 */
public interface EmployeeService {

    /**
     * Save a employee.
     *
     * @param employeeDTO the entity to save
     * @return the persisted entity
     */
    EmployeeDTO save(EmployeeDTO employeeDTO);

    /**
     *  Get all the employees.
     *
     *  @param sort the order
     *  @return the list of entities
     */
    List<EmployeeDTO> findAll(Sort sort);


    /**
     *  Get all the employees.
     *
     *  @param predicate querydsl predicate
     *  @param sort the order
     *  @return the list of entities
     */
    List<EmployeeDTO> findAll(Predicate predicate, Sort sort);

    /**
     *  Get the "id" employee.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    EmployeeDTO findOne(Long id);

    /**
     *  Delete the "id" employee.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     *  Get all subordinates
     *
     *  @param id the id of the employee
     *  @return the subordinates list
     */
    List<Employee> subordinates(Long id);
}
