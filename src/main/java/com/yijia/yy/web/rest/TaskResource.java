package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.yijia.yy.domain.QTask;
import com.yijia.yy.service.TaskService;
import com.yijia.yy.service.UserService;
import com.yijia.yy.service.dto.TaskDTO;
import com.yijia.yy.service.dto.TaskSearchDTO;
import com.yijia.yy.web.rest.util.HeaderUtil;
import com.yijia.yy.web.util.QueryDslUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.xpath.operations.Bool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Task.
 */
@RestController
@RequestMapping("/api")
public class TaskResource {

    private final Logger log = LoggerFactory.getLogger(TaskResource.class);

    @Inject
    private TaskService taskService;

    @Inject
    private UserService userService;

    /**
     * POST  /tasks : Create a new task.
     *
     * @param taskDTO the taskDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new taskDTO, or with status 400 (Bad Request) if the task has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/tasks",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) throws URISyntaxException {
        log.debug("REST request to save Task : {}", taskDTO);
        if (taskDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("task", "idexists", "A new task cannot already have an ID")).body(null);
        }
        TaskDTO result = taskService.save(taskDTO);
        return ResponseEntity.created(new URI("/api/tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("task", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tasks : Updates an existing task.
     *
     * @param taskDTO the taskDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated taskDTO,
     * or with status 400 (Bad Request) if the taskDTO is not valid,
     * or with status 500 (Internal Server Error) if the taskDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/tasks",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TaskDTO> updateTask(@RequestBody TaskDTO taskDTO) throws URISyntaxException {
        log.debug("REST request to update Task : {}", taskDTO);
        if (taskDTO.getId() == null) {
            return createTask(taskDTO);
        }
        TaskDTO result = taskService.save(taskDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("task", taskDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tasks : get all the tasks.
     *
     * @param taskSearchDTO  query params
     * @return the ResponseEntity with status 200 (OK) and the list of tasks in body
     */
    @RequestMapping(value = "/tasks",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TaskDTO> getAllTasks(TaskSearchDTO taskSearchDTO, Sort sort) {
        log.debug("REST request to get all Tasks");

        Predicate predicate;
        BooleanExpression p1 = null;
        if (BooleanUtils.toBoolean(taskSearchDTO.getShownOwnedOnly())) {
            p1 = QTask.task.owner.id.eq(userService.currentLoginEmployee().getId());
        }
        if (BooleanUtils.toBoolean(taskSearchDTO.getShowCreatedOnly())) {
            p1 = QTask.task.creator.id.eq(userService.currentLoginEmployee().getId());
        }

        Predicate p2 = QueryDslUtil.TaskSearchDTO2Predicate(taskSearchDTO);
        if (p1 != null && p2 != null) {
            predicate = p1.and(p2);
        } else if (p2 != null) {
            predicate = p2;
        } else {
            predicate = p1;
        }

        return predicate == null ? taskService.findAll(sort) : taskService.findAll(predicate, sort);
    }

    /**
     * GET  /tasks/:id : get the "id" task.
     *
     * @param id the id of the taskDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the taskDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/tasks/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long id) {
        log.debug("REST request to get Task : {}", id);
        TaskDTO taskDTO = taskService.findOne(id);
        return Optional.ofNullable(taskDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tasks/:id : delete the "id" task.
     *
     * @param id the id of the taskDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/tasks/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        log.debug("REST request to delete Task : {}", id);
        taskService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("task", id.toString())).build();
    }

}
