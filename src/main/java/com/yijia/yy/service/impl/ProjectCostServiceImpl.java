package com.yijia.yy.service.impl;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.domain.Employee;
import com.yijia.yy.domain.ProjectCost;
import com.yijia.yy.domain.ProjectTimeline;
import com.yijia.yy.domain.enumeration.ProjectTimelineEventType;
import com.yijia.yy.repository.ProjectCostRepository;
import com.yijia.yy.repository.ProjectTimelineRepository;
import com.yijia.yy.service.ProjectCostService;
import com.yijia.yy.service.UserService;
import com.yijia.yy.service.dto.ProjectCostDTO;
import com.yijia.yy.service.mapper.ProjectCostMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing ProjectCost.
 */
@Service
@Transactional
public class ProjectCostServiceImpl implements ProjectCostService{

    private final Logger log = LoggerFactory.getLogger(ProjectCostServiceImpl.class);

    @Inject
    private ProjectCostRepository projectCostRepository;

    @Inject
    private ProjectCostMapper projectCostMapper;

    @Inject
    private UserService userService;

    @Inject
    private ProjectTimelineRepository projectTimelineRepository;


    /**
     * Save a projectCost.
     *
     * @param projectCostDTO the entity to save
     * @return the persisted entity
     */
    public ProjectCostDTO save(ProjectCostDTO projectCostDTO) {
        log.debug("Request to save ProjectCost : {}", projectCostDTO);

        Employee currentEmployee  = userService.currentLoginEmployee();

        ProjectTimeline projectTimeline = new ProjectTimeline();
        projectTimeline.setType(ProjectTimelineEventType.COST);
        projectTimeline.setCreateTime(System.currentTimeMillis());
        projectTimeline.setCreator(currentEmployee);

        ProjectCost projectCost = projectCostMapper.projectCostDTOToProjectCost(projectCostDTO);
        projectTimeline.setProject(projectCost.getProject());
        if (projectCost.getId() == null) {
            projectCost.setCreator(currentEmployee);
            projectCost.setCreateTime(System.currentTimeMillis());

            projectTimeline.setInfo(currentEmployee.getName() + "  新建了项目支出：" + projectCost.getCategory().getName() + " - " + projectCost.getAmount());
        } else {
            projectTimeline.setInfo(currentEmployee.getName() + " 修改了项目支出： " + projectCost.getCategory().getName() + " - " + projectCost.getAmount());
        }
        projectTimelineRepository.saveAndFlush(projectTimeline);

        projectCost = projectCostRepository.save(projectCost);
        ProjectCostDTO result = projectCostMapper.projectCostToProjectCostDTO(projectCost);
        return result;
    }

    /**
     *  Get all the projectCosts.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ProjectCostDTO> findAll(Sort sort) {
        log.debug("Request to get all ProjectCosts");
        List<ProjectCostDTO> result = projectCostRepository.findAll(sort).stream()
            .map(projectCostMapper::projectCostToProjectCostDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    @Transactional(readOnly = true)
    public List<ProjectCostDTO> findAll(Predicate predicate, Sort sort) {
        log.debug("Request to get all ProjectCosts");
        List<ProjectCostDTO> result = StreamSupport.stream(projectCostRepository.findAll(predicate, sort).spliterator(), false)
            .map(projectCostMapper::projectCostToProjectCostDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }


    /**
     *  Get one projectCost by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ProjectCostDTO findOne(Long id) {
        log.debug("Request to get ProjectCost : {}", id);
        ProjectCost projectCost = projectCostRepository.findOne(id);
        ProjectCostDTO projectCostDTO = projectCostMapper.projectCostToProjectCostDTO(projectCost);
        return projectCostDTO;
    }

    /**
     *  Delete the  projectCost by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProjectCost : {}", id);
        ProjectCost projectCost = projectCostRepository.findOne(id);
        if (projectCost != null) {

            Employee currentEmployee  = userService.currentLoginEmployee();

            ProjectTimeline projectTimeline = new ProjectTimeline();
            projectTimeline.setType(ProjectTimelineEventType.COST);
            projectTimeline.setCreateTime(System.currentTimeMillis());
            projectTimeline.setCreator(currentEmployee);
            projectTimeline.setProject(projectCost.getProject());
            projectTimeline.setInfo(currentEmployee.getName() + " 删除了项目支出: " + projectCost.getCategory().getName() + " - " + projectCost.getAmount());

            projectTimelineRepository.save(projectTimeline);
            projectCostRepository.delete(id);

        }
    }
}
