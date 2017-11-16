package com.yijia.yy.service;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.exception.ProjectArchiveHasUnfinishedTaskException;
import com.yijia.yy.service.dto.ProjectDTO;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Set;

/**
 * Service Interface for managing Project.
 */
public interface ProjectService {

    /**
     * Save a project.
     *
     * @param projectDTO the entity to save
     * @return the persisted entity
     */
    ProjectDTO save(ProjectDTO projectDTO);

    /**
     *  Get all the projects.
     *
     *  @param sort the order
     *  @return the list of entities
     */
    List<ProjectDTO> findAll(Sort sort);


    /**
     *  Get all the projects by predicate.
     *
     *  @param predicate the predicate
     *  @param sort the order
     *  @return the list of entities
     */
    List<ProjectDTO> findAll(Predicate predicate, Sort sort);

    /**
     *  Get the "id" project.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ProjectDTO findOne(Long id);

    /**
     *  Delete the "id" project.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     *  Close the "id" project.
     *
     *  @param id the id of the entity
     */
    void close(Long id);

    /**
     *  Archive the "id" project.
     *
     *  @param id the id of the entity
     */
    void archive(Long id) throws ProjectArchiveHasUnfinishedTaskException;

    /**
     *  Restart the "id" project.
     *
     *  @param id the id of the entity
     */
    void restart(Long id);

    /**
     *  Pause the "id" project.
     *
     *  @param id the id of the entity
     */
    void pause(Long id);

    /**
     * get projects of employee "employeeId"
     *
     * @param employeeId the id of employee
     * @return the project list
     */
    List<ProjectDTO> projectsParticipatedByEmployee(Long employeeId);

    /**
     * get projects of employee "employeeId"
     *
     * @param employeeId the id of employee
     * @return the ids of project list
     */
    Set<Long> projectIdsParticipatedByEmployee(Long employeeId);
}
