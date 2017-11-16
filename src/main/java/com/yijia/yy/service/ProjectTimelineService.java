package com.yijia.yy.service;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.dto.ProjectTimelineDTO;
import org.springframework.data.domain.Sort;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing ProjectTimeline.
 */
public interface ProjectTimelineService {

    /**
     * Save a projectTimeline.
     *
     * @param projectTimelineDTO the entity to save
     * @return the persisted entity
     */
    ProjectTimelineDTO save(ProjectTimelineDTO projectTimelineDTO);

    /**
     *  Get all the projectTimelines.
     *
     *  @return the list of entities
     */
    List<ProjectTimelineDTO> findAll(Sort sort);

    List<ProjectTimelineDTO> findAll(Predicate predicate, Sort sort);

    /**
     *  Get the "id" projectTimeline.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ProjectTimelineDTO findOne(Long id);

    /**
     *  Delete the "id" projectTimeline.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
