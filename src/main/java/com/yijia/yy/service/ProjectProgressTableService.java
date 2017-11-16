package com.yijia.yy.service;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.dto.ProjectProgressTableDTO;
import org.springframework.data.domain.Sort;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing ProjectProgressTable.
 */
public interface ProjectProgressTableService {

    /**
     * Save a projectProgressTable.
     *
     * @param projectProgressTableDTO the entity to save
     * @return the persisted entity
     */
    ProjectProgressTableDTO save(ProjectProgressTableDTO projectProgressTableDTO);

    /**
     *  Get all the projectProgressTables.
     *
     *  @param  sort the order
     *  @return the list of entities
     */
    List<ProjectProgressTableDTO> findAll(Sort sort);

    /**
     *  Get all the projectProgressTables.
     *
     *  @predicate the predicate
     *  @param  sort the order
     *  @return the list of entities
     */
    List<ProjectProgressTableDTO> findAll(Predicate predicate, Sort sort);

    /**
     *  Get the "id" projectProgressTable.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ProjectProgressTableDTO findOne(Long id);

    /**
     *  Delete the "id" projectProgressTable.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
