package com.yijia.yy.service;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.dto.ProjectRateDTO;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Service Interface for managing ProjectRate.
 */
public interface ProjectRateService {

    /**
     * Save a projectRate.
     *
     * @param projectRateDTO the entity to save
     * @return the persisted entity
     */
    ProjectRateDTO save(ProjectRateDTO projectRateDTO);

    /**
     *  Get all the projectRates.
     *
     *  @param sort
     *  @return the list of entities
     */
    List<ProjectRateDTO> findAll(Sort sort);


    /**
     *  Get all the projectRates by predicate.
     *
     *  @param predicate
     *  @param sort
     *  @return the list of entities
     */
    List<ProjectRateDTO> findAll(Predicate predicate, Sort sort);

    /**
     *  Get the "id" projectRate.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ProjectRateDTO findOne(Long id);

    /**
     *  Delete the "id" projectRate.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Get the rate privileges of all employees
     *
     * @param employeeId the id of employee
     * @return the privileges table
     */
    Map<String,Boolean> getAllProjectRatePrivilegesOfEmployee(Long employeeId) throws IOException;

    /**
     *  Close rate of project
     * @param id the project id
     */
    void closeProjectRates(Long id);
}
