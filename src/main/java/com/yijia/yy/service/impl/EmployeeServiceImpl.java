package com.yijia.yy.service.impl;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.domain.Employee;
import com.yijia.yy.domain.JobTitle;
import com.yijia.yy.domain.QEmployee;
import com.yijia.yy.repository.EmployeeRepository;
import com.yijia.yy.service.EmployeeService;
import com.yijia.yy.service.UserService;
import com.yijia.yy.service.dto.EmployeeDTO;
import com.yijia.yy.service.mapper.EmployeeMapper;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing Employee.
 */
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService{

    private final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Inject
    private EmployeeRepository employeeRepository;

    @Inject
    private EmployeeMapper employeeMapper;

    @Inject
    private UserService userService;

    /**
     * Save a employee.
     *
     * @param employeeDTO the entity to save
     * @return the persisted entity
     */
    public EmployeeDTO save(EmployeeDTO employeeDTO) {
        log.debug("Request to save Employee : {}", employeeDTO);
        Employee employee = employeeMapper.employeeDTOToEmployee(employeeDTO);
        employee.setLastUpdateTime(System.currentTimeMillis());

        if (employee.getId() != null) {
            Employee oldEmployee = employeeRepository.findOne(employee.getId());
            if (oldEmployee != null) {
                BeanUtils.copyProperties(employee, oldEmployee, "deleted");
                employee = oldEmployee;
            }
        }
        if (employee.getPhoto() == null || employee.getPhoto().getId() <= 0) {
            employee.setPhoto(null);
        }
        employee.setDeleted(BooleanUtils.toBoolean(employee.getDeleted()));
        employee = employeeRepository.save(employee);
        EmployeeDTO result = employeeMapper.employeeToEmployeeDTO(employee);
        return result;
    }

    /**
     *  Get all the employees.
     *
     *  @param sort the order
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<EmployeeDTO> findAll(Sort sort) {
        log.debug("Request to get all Employees");
        Predicate predicate = QEmployee.employee.deleted.eq(false);
        return findAll(predicate, sort);
    }

    @Override
    public List<EmployeeDTO> findAll(Predicate predicate, Sort sort) {
        log.debug("Request to get all Employees by dsl");
        if (predicate == null) {
            return findAll(sort);
        }
        return StreamSupport.stream(
            employeeRepository.findAll(QEmployee.employee.deleted.eq(false).and(predicate), sort).spliterator(),
            false
        ).map(employee -> employeeMapper.employeeToEmployeeDTO(employee))
            .collect(Collectors.toList());
    }

    /**
     *  Get one employee by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public EmployeeDTO findOne(Long id) {
        log.debug("Request to get Employee : {}", id);
        Employee employee = employeeRepository.findOne(id);
        EmployeeDTO employeeDTO = employeeMapper.employeeToEmployeeDTO(employee);
        return employeeDTO;
    }

    /**
     *  Get all subordinates
     *
     *  @param id the id of the employee
     *  @return the subordinates list
     */
    @Transactional(readOnly = true)
    public List<Employee> subordinates(Long id) {
        log.debug("find all subordinates of employee with id: {}", id);

        Employee employee = employeeRepository.findOne(id);
        if (employee == null || employee.getJobTitles() == null || employee.getJobTitles().isEmpty()) {
            return new ArrayList<>();
        }

        List<JobTitle> sjs = new ArrayList<>();
        for (JobTitle j : employee.getJobTitles()) {
            Queue<JobTitle> q = new ArrayDeque<>();
            q.add(j);
            while (!q.isEmpty()) {
                JobTitle tj = q.poll();
                sjs.addAll(tj.getSubordinates());
                q.addAll(tj.getSubordinates());
            }
        }

        List<Employee> employees = employeeRepository.findAll();

        return employees.stream()
            .filter(e ->
                e.getJobTitles() != null &&
                    e.getJobTitles().stream().filter(j -> sjs.stream().filter(sj -> sj.getId().equals(j.getId()))
                    .findAny().isPresent()
                ).findAny().isPresent()
            )
            .filter(e -> e.sameDept(employee))
            .collect(Collectors.toList());

    }

    /**
     *  Delete the  employee by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Employee : {}", id);
        Employee employee = employeeRepository.findOne(id);
        if (employee != null) {
            employee.setDeleted(true);
            employee = employeeRepository.save(employee);
            if (employee.getUser() != null && employee.getUser().getId() != null) {
                userService.deactivateUser(employee.getUser().getId());
            }
        }
    }
}
