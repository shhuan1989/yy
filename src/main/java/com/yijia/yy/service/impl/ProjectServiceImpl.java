package com.yijia.yy.service.impl;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.yijia.yy.domain.*;
import com.yijia.yy.domain.enumeration.HasEnum;
import com.yijia.yy.domain.enumeration.ProjectStatus;
import com.yijia.yy.domain.enumeration.ProjectTimelineEventType;
import com.yijia.yy.domain.enumeration.TaskStatus;
import com.yijia.yy.exception.ProjectArchiveHasUnfinishedTaskException;
import com.yijia.yy.repository.EmployeeRepository;
import com.yijia.yy.repository.ProjectRepository;
import com.yijia.yy.repository.ProjectTimelineRepository;
import com.yijia.yy.repository.TaskRepository;
import com.yijia.yy.service.EmployeeService;
import com.yijia.yy.service.ProjectService;
import com.yijia.yy.service.TaskService;
import com.yijia.yy.service.UserService;
import com.yijia.yy.service.dto.ProjectDTO;
import com.yijia.yy.service.event.ProjectEvent;
import com.yijia.yy.service.mapper.ProjectMapper;
import io.jsonwebtoken.lang.Collections;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.bus.Event;
import reactor.bus.EventBus;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing Project.
 */
@Service
@Transactional
public class ProjectServiceImpl implements ProjectService{

    private final Logger log = LoggerFactory.getLogger(ProjectServiceImpl.class);

    @Inject
    private ProjectRepository projectRepository;

    @Inject
    private ProjectMapper projectMapper;

    @Inject
    private UserService userService;

    @Inject
    private EventBus eventBus;

    @Inject
    private ApplicationContext appContext;

    @Inject
    private TaskService taskService;

    @Inject
    private EmployeeRepository employeeRepository;

    @Inject
    private ProjectTimelineRepository timelineRepository;

    /**
     * Save a project.
     *
     * @param projectDTO the entity to save
     * @return the persisted entity
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    public ProjectDTO save(ProjectDTO projectDTO) {
        log.debug("Request to save Project : {}", projectDTO);
        Employee currentEmployee = userService.currentLoginEmployee();
        Project project = projectMapper.projectDTOToProject(projectDTO);
        Project oldProject = null;
        Set<Employee> addedMembers = new HashSet<>();
        Set<Employee> deletedMembers = new HashSet<>();
        if (project.getId() == null) {
            project.setCreateTime(System.currentTimeMillis());
            project.setStatus(ProjectStatus.INCOMPLETE);
            project.setDirector(currentEmployee);
        } else {
            oldProject = projectRepository.findOne(project.getId());
            if (oldProject.getStatus() == ProjectStatus.CLOSED || oldProject.getStatus() == ProjectStatus.ARCHIVED) {
                throw new IllegalStateException("不能修改已经关闭的任务");
            }

            Set<Employee> oldMembers = oldProject.getMembers();
            Set<Employee> newMembers = project.getMembers();

            if (oldMembers != null) {
                deletedMembers = oldMembers.stream()
                    .filter(m -> newMembers == null || !newMembers.contains(m))
                    .collect(Collectors.toSet());
            }
            if (newMembers != null) {
                addedMembers = newMembers.stream()
                    .filter(m -> oldMembers == null || !oldMembers.contains(m))
                    .collect(Collectors.toSet());
            }
        }

        if (StringUtils.isBlank(project.getIdNumber())
            || (BooleanUtils.toBoolean(project.isHuaWei()) && !project.getIdNumber().startsWith("HW"))
            || (!BooleanUtils.toBoolean(project.isHuaWei())) && !project.getIdNumber().startsWith("F"))
        {
            List<Project> projects = projectRepository.findByIsHuaWeiOrderByIdDesc(project.isHuaWei());
            String idNumber = "001";

            if (projects != null && !projects.isEmpty()) {
                Integer maxNumber =
                projects.stream()
                    .filter(p -> !StringUtils.isBlank(p.getIdNumber()))
                    .mapToInt(p -> {
                        if (p.getIdNumber().startsWith("HW")) {
                            return Integer.parseInt(p.getIdNumber().substring(10));
                        } else {
                            return Integer.parseInt(p.getIdNumber().substring(9));
                        }
                    })
                    .max().getAsInt() + 1;
                idNumber = "" + maxNumber;
            }

            int c = 3 - idNumber.length();
            for (int i = 0; i < c; i++) {
                idNumber = "0" + idNumber;
            }

            LocalDate now = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String d = now.format(formatter);
            if (project.isHuaWei()) {
                project.setIdNumber("HW"+d+idNumber);
            } else {
                project.setIdNumber("F"+d+idNumber);
            }
        }


        if (project.getCreator() == null || project.getCreator().getId() == null) {
            project.setCreator(currentEmployee);
        }
        project.setLastUpdateTime(System.currentTimeMillis());
        project.setLastModifier(currentEmployee);
        if (project.getContract() != null && project.getContract().getId() != null) {
            project.setHasContract(HasEnum.YES);
        } else {
            project.setHasContract(HasEnum.NO);
        }
        boolean statusChanged = oldProject == null || project.getStatus() != oldProject.getStatus();
        project = projectRepository.saveAndFlush(project);
        if (statusChanged) {
            eventBus.notify("project", Event.wrap(ProjectEvent.projectEvent().projectId(project.getId())));
        }

        Long ct = System.currentTimeMillis();
        final Project p = project;
        if (!addedMembers.isEmpty()) {
            addedMembers.forEach(m -> {
                m = employeeRepository.findOne(m.getId());
                ProjectTimeline timeline = new ProjectTimeline();
                timeline.setCreateTime(ct);
                timeline.setCreator(currentEmployee);
                timeline.setProject(p);
                timeline.setType(ProjectTimelineEventType.PROJECT);
                timeline.setInfo(currentEmployee.getName() + " 添加了项目成员：" + m.getName());
                timelineRepository.save(timeline);
            });
        }

        if (!deletedMembers.isEmpty()) {
            deletedMembers.forEach(m -> {
                ProjectTimeline timeline = new ProjectTimeline();
                timeline.setCreateTime(ct);
                timeline.setCreator(currentEmployee);
                timeline.setProject(p);
                timeline.setType(ProjectTimelineEventType.PROJECT);
                timeline.setInfo(currentEmployee.getName() + " 删除了项目成员：" + m.getName());
                timelineRepository.save(timeline);
            });
        }

        ProjectDTO result = projectMapper.projectToProjectDTO(project);
        return result;
    }

    /**
     *  Get all the projects.
     *
     *  @param sort the order
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ProjectDTO> findAll(Sort sort) {
        log.debug("Request to get all Projects");
        return projectRepository.findAll(sort).stream()
            .map(project -> {
                setRateMembers(project);
                updateTaskFinishStatus(project);
                return project;
            })
            .map(project -> projectMapper.projectToProjectDTO(project))
            .collect(Collectors.toList());
    }

    /**
     *  Get all the projects by predicate.
     *
     *  @param predicate the predicate
     *  @param sort the order
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProjectDTO> findAll(Predicate predicate, Sort sort) {
        log.debug("Request to get all Projects by predicate");
        // parallel stream will casuse: Resolved exception caused by Handler execution: org.hibernate.AssertionFailure: bug adding collection twice
        return StreamSupport.stream(projectRepository.findAll(predicate, sort).spliterator(), false) // true cause issue!!
            .map(project -> {
                setRateMembers(project);
                updateTaskFinishStatus(project);
                return project;
            })
            .map(project -> projectMapper.projectToProjectDTO(project))
            .collect(Collectors.toList());
    }

    /**
     *  Get one project by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ProjectDTO findOne(Long id) {
        log.debug("Request to get Project : {}", id);
        Project project = projectRepository.findOneWithEagerRelationships(id);
        setRateMembers(project);
        updateTaskFinishStatus(project);
        ProjectDTO projectDTO = projectMapper.projectToProjectDTO(project);
        return projectDTO;
    }

    /**
     *  Delete the  project by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Project : {}", id);
        projectRepository.delete(id);
    }

    @Override
    @Transactional
    public void close(Long id) {
        log.debug("Request to close Project : {}", id);
        updateProjectStatus(id, ProjectStatus.CLOSED);
    }

    @Override
    @Transactional
    public void archive(Long id) throws ProjectArchiveHasUnfinishedTaskException {
        log.debug("Request to archive Project : {}", id);
        if (id != null) {
            if (!taskService.allTasksDone(id)) {
                throw new ProjectArchiveHasUnfinishedTaskException();
            }

            updateProjectStatus(id, ProjectStatus.ARCHIVED);
        }
    }

    @Override
    public void restart(Long id) {
        log.debug("Request to restart Project : {}", id);
        updateProjectStatus(id, ProjectStatus.INCOMPLETE);
    }

    @Override
    public void pause(Long id) {
        log.debug("Request to pasue Project : {}", id);
        updateProjectStatus(id, ProjectStatus.PAUSED);
    }

    /**
     * get projects of employee "employeeId"
     *
     * @param employeeId the id of employee
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProjectDTO> projectsParticipatedByEmployee(Long employeeId) {
        return projectRepository.findAll().stream()
            .filter(p -> employeeId==null
                || p.getAllMembers().stream()
                .filter(m -> m.getId().equals(employeeId))
                .findAny()
                .isPresent()
                )
            .map(project -> {
                setRateMembers(project);
                updateTaskFinishStatus(project);
                return project;
            })
            .map(project -> projectMapper.projectToProjectDTO(project))
            .collect(Collectors.toList());
    }

    /**
     * get projects of employee "employeeId"
     *
     * @param employeeId the id of employee
     * @return the ids of project list
     */
    @Override
    @Transactional(readOnly = true)
    public Set<Long> projectIdsParticipatedByEmployee(Long employeeId) {
        return projectRepository.findAll().stream()
            .filter(p -> employeeId==null
                || p.getAllMembers().stream()
                .filter(m -> m.getId().equals(employeeId))
                .findAny()
                .isPresent()
            )
            .map(project -> project.getId())
            .collect(Collectors.toSet());
    }

    private void updateProjectStatus(Long id, ProjectStatus status) {
        if (id != null) {
            Project project = projectRepository.findOne(id);
            if (project != null) {
                project.setStatus(status);
                projectRepository.saveAndFlush(project);

                Employee e = userService.currentLoginEmployee();
                ProjectTimeline timeline = new ProjectTimeline();
                timeline.setCreator(e);
                timeline.setProject(project);
                timeline.setType(ProjectTimelineEventType.PROJECT);
                timeline.setCreateTime(System.currentTimeMillis());

                String info = e.getName();
                switch (status) {
                    case PAUSED:
                        info += " 暂停了项目 ";
                        break;
                    case INCOMPLETE:
                        info += " 重启了项目 ";
                        break;
                    case CLOSED:
                        info += " 关闭了项目 ";
                        break;
                    case ARCHIVED:
                        info += " 归档了项目 ";
                        break;
                    default:
                        info += " 修改了项目 ";
                        break;
                }
                info += project.getName();
                timeline.setInfo(info);

                timelineRepository.save(timeline);
            }
        }
    }

    private void setRateMembers(Project project) {
        Resource resource = appContext.getResource("classpath:project_rate_privilege.txt");
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
            String content = IOUtils.toString(inputStream, "UTF-8");


            project.setFirstLevelRateMembers(
                project.getGroundLevelMembers().stream()
                    .filter(m -> !Collections.isEmpty(m.getJobTitles())
                        && m.getJobTitles().stream().filter(t -> content.contains(t.getName())).findAny().isPresent())
                    .collect(Collectors.toSet())
            );

            project.setSecondLevelRateMembers(
                project.getAllMembers().stream()
                    .filter(m -> m.getHighestLevel() <= JobTitle.LEVEL_DIRECTOR)
                    .filter(m -> !Collections.isEmpty(m.getJobTitles())
                        && m.getJobTitles().stream().filter(t -> content.contains(t.getName())).findAny().isPresent())
                .collect(Collectors.toSet())
            );
        } catch (IOException e) {
            log.warn("Failed to get rate members", e);

        }
    }

    private void updateTaskFinishStatus(Project project) {
        if (project == null || project.getId() == null) {
            return;
        }

        project.setAllTaskDone(taskService.allTasksDone(project.getId()));
    }
}
