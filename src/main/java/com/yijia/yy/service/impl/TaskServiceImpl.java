package com.yijia.yy.service.impl;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.yijia.yy.domain.*;
import com.yijia.yy.domain.enumeration.ProjectTimelineEventType;
import com.yijia.yy.domain.enumeration.TaskStatus;
import com.yijia.yy.exception.ProjectArchiveHasUnfinishedTaskException;
import com.yijia.yy.repository.EmployeeRepository;
import com.yijia.yy.repository.ProjectTimelineRepository;
import com.yijia.yy.repository.TaskRepository;
import com.yijia.yy.service.TaskService;
import com.yijia.yy.service.UserService;
import com.yijia.yy.service.dto.TaskDTO;
import com.yijia.yy.service.event.ProjectEvent;
import com.yijia.yy.service.mapper.TaskMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.bus.Event;
import reactor.bus.EventBus;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing Task.
 */
@Service
@Transactional
public class TaskServiceImpl implements TaskService{

    private final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Inject
    private TaskRepository taskRepository;

    @Inject
    private TaskMapper taskMapper;

    @Inject
    private UserService userService;

    @Inject
    private EmployeeRepository employeeRepository;

    @Inject
    private EventBus eventBus;

    @Inject
    private ProjectTimelineRepository projectTimelineRepository;

    /**
     * Save a task.
     *
     * @param taskDTO the entity to save
     * @return the persisted entity
     */
    public TaskDTO save(TaskDTO taskDTO) {
        log.debug("Request to save Task : {}", taskDTO);

        Employee currentEmployee = userService.currentLoginEmployee();
        Long currentTime = System.currentTimeMillis();

        Task task = taskMapper.taskDTOToTask(taskDTO);
        Task oldTask = null;
        if (task.getId() == null) {
            task.setCreateTime(currentTime);
            task.setCreator(currentEmployee);
            task.setStatus(TaskStatus.NEW);
        } else {
            oldTask = taskRepository.findOne(task.getId());
        }

        task.setLastUpdateTime(currentTime);
        task.setLastModifier(currentEmployee);
        if (task != null && task.getOwner() != null && task.getOwner().getId() == null) {
            task.setOwner(null);
        }
        task = taskRepository.save(task);

        ProjectTimeline timeline = new ProjectTimeline();
        timeline.setCreateTime(currentTime);
        timeline.setCreator(currentEmployee);
        timeline.setType(ProjectTimelineEventType.TASK);
        timeline.setProject(task.getProject());

        boolean statusChanged = (oldTask == null || oldTask.getStatus() != task.getStatus()) && task.getStatus() != null;
        if (statusChanged) {
            switch (task.getStatus()) {
                case DONE:
                    timeline.setInfo(currentEmployee.getName() + " 完成了任务 " + task.getName());
                    break;
                case NEW:
                    timeline.setInfo(currentEmployee.getName() + " 创建了任务 " + task.getName());
                    break;
                case INPROGRESS:
                    timeline.setInfo(currentEmployee.getName() + " 打开了任务 " + task.getName());
                    break;
                default:
                    timeline.setInfo(currentEmployee.getName() + " 编辑了任务 " + task.getName());
                    break;
            }
        } else {
            timeline.setInfo(currentEmployee.getName() + " 编辑了任务 " + task.getName());
        }
        projectTimelineRepository.saveAndFlush(timeline);

        TaskDTO result = taskMapper.taskToTaskDTO(task);
        return result;
    }

    /**
     *  Get all the tasks.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<TaskDTO> findAll(Sort sort) {
        log.debug("Request to get all Tasks");
        List<TaskDTO> result = taskRepository.findAll(sort).stream()
            .map(taskMapper::taskToTaskDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get all the tasks.
     *  @param predicate the predicate
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    @Override
    public List<TaskDTO> findAll(Predicate predicate, Sort sort) {
        log.debug("Request to get all Tasks by predicate");
        List<TaskDTO> result = StreamSupport.stream(taskRepository.findAll(predicate, sort).spliterator(), false)
            .map(taskMapper::taskToTaskDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one task by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public TaskDTO findOne(Long id) {
        log.debug("Request to get Task : {}", id);
        Task task = taskRepository.findOneWithEagerRelationships(id);
        TaskDTO taskDTO = taskMapper.taskToTaskDTO(task);
        return taskDTO;
    }

    /**
     *  Delete the  task by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Task : {}", id);

        Task task = taskRepository.findOne(id);

        Employee currentEmployee = userService.currentLoginEmployee();
        ProjectTimeline timeline = new ProjectTimeline();
        timeline.setCreateTime(System.currentTimeMillis());
        timeline.setCreator(currentEmployee);
        timeline.setProject(task.getProject());
        timeline.setType(ProjectTimelineEventType.TASK);
        timeline.setInfo(currentEmployee.getName() + " 删除了任务：" + task.getName());
        projectTimelineRepository.save(timeline);

        taskRepository.delete(id);

    }

    /**
     * find unfinished tasks of project
     *
     * @param projectId the id of project
     * @return the tasks list
     */
    public List<TaskDTO> findAllUnfinishedTasks(Long projectId) {
        if (projectId == null) {
            return new ArrayList<>();
        }


        QTask task = QTask.task;
        BooleanExpression predicate = task.project.id.eq(projectId)
            .and(task.status.ne(TaskStatus.DONE));

        return StreamSupport.stream(taskRepository.findAll(predicate).spliterator(), false)
            .map(t -> taskMapper.taskToTaskDTO(t))
            .collect(Collectors.toList());
    }

    /**
     * whether all tasks of project {projectId} have been finished
     *
     * @param projectId the id of project
     * @return true if all tasks are done or false
     */
    public boolean allTasksDone(Long projectId) {
        List<TaskDTO> unfinishedTasks = findAllUnfinishedTasks(projectId);
        return unfinishedTasks.isEmpty();
    }

}
