package com.yijia.yy.service;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.dto.TaskDTO;
import org.springframework.data.domain.Sort;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Task.
 */
public interface TaskService {

    /**
     * Save a task.
     *
     * @param taskDTO the entity to save
     * @return the persisted entity
     */
    TaskDTO save(TaskDTO taskDTO);

    /**
     *  Get all the tasks.
     *
     *  @return the list of entities
     */
    List<TaskDTO> findAll(Sort sort);

    /**
     *  Get the "id" task.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TaskDTO findOne(Long id);

    /**
     *  Delete the "id" task.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     *  find all tasks by predicate
     *
     *  @param predicate the predicate
     *  @param sort the order
     *  @return the task list
     */
    List<TaskDTO> findAll(Predicate predicate, Sort sort);

    /**
     * find unfinished tasks of project
     *
     * @param projectId the id of project
     * @return the tasks list
     */
    List<TaskDTO> findAllUnfinishedTasks(Long projectId);

    /**
     * whether all tasks of project {projectId} have been finished
     *
     * @param projectId the id of project
     * @return true if all tasks are done or false
     */
    boolean allTasksDone(Long projectId);

}
