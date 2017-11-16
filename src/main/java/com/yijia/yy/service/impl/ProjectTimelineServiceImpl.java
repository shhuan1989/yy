package com.yijia.yy.service.impl;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.ProjectTimelineService;
import com.yijia.yy.domain.ProjectTimeline;
import com.yijia.yy.repository.ProjectTimelineRepository;
import com.yijia.yy.service.dto.ProjectTimelineDTO;
import com.yijia.yy.service.mapper.ProjectTimelineMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing ProjectTimeline.
 */
@Service
@Transactional
public class ProjectTimelineServiceImpl implements ProjectTimelineService{

    private final Logger log = LoggerFactory.getLogger(ProjectTimelineServiceImpl.class);

    @Inject
    private ProjectTimelineRepository projectTimelineRepository;

    @Inject
    private ProjectTimelineMapper projectTimelineMapper;

    /**
     * Save a projectTimeline.
     *
     * @param projectTimelineDTO the entity to save
     * @return the persisted entity
     */
    public ProjectTimelineDTO save(ProjectTimelineDTO projectTimelineDTO) {
        log.debug("Request to save ProjectTimeline : {}", projectTimelineDTO);
        ProjectTimeline projectTimeline = projectTimelineMapper.projectTimelineDTOToProjectTimeline(projectTimelineDTO);
        projectTimeline = projectTimelineRepository.save(projectTimeline);
        ProjectTimelineDTO result = projectTimelineMapper.projectTimelineToProjectTimelineDTO(projectTimeline);
        return result;
    }

    /**
     *  Get all the projectTimelines.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ProjectTimelineDTO> findAll(Sort sort) {
        log.debug("Request to get all ProjectTimelines");
        List<ProjectTimelineDTO> result = projectTimelineRepository.findAll(sort).stream()
            .map(projectTimelineMapper::projectTimelineToProjectTimelineDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    @Transactional(readOnly = true)
    public List<ProjectTimelineDTO> findAll(Predicate predicate, Sort sort) {
        log.debug("Request to get all ProjectTimelines");
        if (predicate == null) {
            return findAll(sort);
        }
        List<ProjectTimelineDTO> result = StreamSupport.stream(projectTimelineRepository.findAll(predicate, sort).spliterator(), false)
            .map(projectTimelineMapper::projectTimelineToProjectTimelineDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one projectTimeline by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ProjectTimelineDTO findOne(Long id) {
        log.debug("Request to get ProjectTimeline : {}", id);
        ProjectTimeline projectTimeline = projectTimelineRepository.findOne(id);
        ProjectTimelineDTO projectTimelineDTO = projectTimelineMapper.projectTimelineToProjectTimelineDTO(projectTimeline);
        return projectTimelineDTO;
    }

    /**
     *  Delete the  projectTimeline by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProjectTimeline : {}", id);
        projectTimelineRepository.delete(id);
    }
}
