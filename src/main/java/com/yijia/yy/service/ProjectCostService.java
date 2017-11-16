package com.yijia.yy.service;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.dto.ProjectCostDTO;
import org.springframework.data.domain.Sort;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing ProjectCost.
 */
public interface ProjectCostService {

    /**
     * Save a projectCost.
     *
     * @param projectCostDTO the entity to save
     * @return the persisted entity
     */
    ProjectCostDTO save(ProjectCostDTO projectCostDTO);

    /**
     *  Get all the projectCosts.
     *
     *  @return the list of entities
     */
    List<ProjectCostDTO> findAll(Sort sort);

    List<ProjectCostDTO> findAll(Predicate predicate, Sort sort);

    /**
     *  Get the "id" projectCost.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ProjectCostDTO findOne(Long id);

    /**
     *  Delete the "id" projectCost.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
