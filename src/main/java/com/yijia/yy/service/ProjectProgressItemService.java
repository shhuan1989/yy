package com.yijia.yy.service;

import com.yijia.yy.service.dto.ProjectProgressItemDTO;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing ProjectProgressItem.
 */
public interface ProjectProgressItemService {

    /**
     * Save a projectProgressItem.
     *
     * @param projectProgressItemDTO the entity to save
     * @return the persisted entity
     */
    ProjectProgressItemDTO save(ProjectProgressItemDTO projectProgressItemDTO);

    /**
     *  Get all the projectProgressItems.
     *  
     *  @return the list of entities
     */
    List<ProjectProgressItemDTO> findAll();

    /**
     *  Get the "id" projectProgressItem.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ProjectProgressItemDTO findOne(Long id);

    /**
     *  Delete the "id" projectProgressItem.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
