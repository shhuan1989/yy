package com.yijia.yy.service.impl;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.domain.Project;
import com.yijia.yy.repository.ProjectRepository;
import com.yijia.yy.service.ProjectProgressTableService;
import com.yijia.yy.domain.ProjectProgressTable;
import com.yijia.yy.repository.ProjectProgressTableRepository;
import com.yijia.yy.service.UserService;
import com.yijia.yy.service.dto.ProjectProgressTableDTO;
import com.yijia.yy.service.mapper.ProjectProgressTableMapper;
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
 * Service Implementation for managing ProjectProgressTable.
 */
@Service
@Transactional
public class ProjectProgressTableServiceImpl implements ProjectProgressTableService{

    private final Logger log = LoggerFactory.getLogger(ProjectProgressTableServiceImpl.class);

    @Inject
    private ProjectProgressTableRepository projectProgressTableRepository;

    @Inject
    private ProjectProgressTableMapper projectProgressTableMapper;

    @Inject
    private UserService userService;

    @Inject
    private ProjectRepository projectRepository;

    /**
     * Save a projectProgressTable.
     *
     * @param projectProgressTableDTO the entity to save
     * @return the persisted entity
     */
    public ProjectProgressTableDTO save(ProjectProgressTableDTO projectProgressTableDTO) {
        log.debug("Request to save ProjectProgressTable : {}", projectProgressTableDTO);
        ProjectProgressTable projectProgressTable = projectProgressTableMapper.projectProgressTableDTOToProjectProgressTable(projectProgressTableDTO);

        if (projectProgressTable.getCreator() == null || projectProgressTable.getCreator().getId() == null) {
            projectProgressTable.setCreator(userService.currentLoginEmployee());
            projectProgressTable.setCreateTime(System.currentTimeMillis());
        }

        final ProjectProgressTable pt = projectProgressTable;
        projectProgressTable.getItems().forEach(i -> i.setProjectProgressTable(pt));

        projectProgressTable = projectProgressTableRepository.save(projectProgressTable);
        ProjectProgressTableDTO result = projectProgressTableMapper.projectProgressTableToProjectProgressTableDTO(projectProgressTable);

        Project project = projectRepository.findOne(pt.getProject().getId());
        project.setProgressStatus(
            Math.toIntExact(
                projectProgressTable.getItems().stream()
                    .filter(i -> i.getFinishTime() != null)
                    .count()
            )
        );
        projectRepository.save(project);

        return result;
    }

    /**
     *  Get all the projectProgressTables.
     *
     *  @param sort the order
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ProjectProgressTableDTO> findAll(Sort sort) {
        log.debug("Request to get all ProjectProgressTables");

        List<ProjectProgressTableDTO> result = projectProgressTableRepository.findAll().stream()
            .map(projectProgressTableMapper::projectProgressTableToProjectProgressTableDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get all the projectProgressTables.
     *
     *  @predicate the predicate
     *  @param  sort the order
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ProjectProgressTableDTO> findAll(Predicate predicate, Sort sort) {
        if (predicate == null) {
            return findAll(sort);
        }

        return StreamSupport.stream(projectProgressTableRepository.findAll(predicate, sort).spliterator(), false)
            .map(projectProgressTableMapper::projectProgressTableToProjectProgressTableDTO)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one projectProgressTable by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ProjectProgressTableDTO findOne(Long id) {
        log.debug("Request to get ProjectProgressTable : {}", id);
        ProjectProgressTable projectProgressTable = projectProgressTableRepository.findOne(id);
        ProjectProgressTableDTO projectProgressTableDTO = projectProgressTableMapper.projectProgressTableToProjectProgressTableDTO(projectProgressTable);
        return projectProgressTableDTO;
    }

    /**
     *  Delete the  projectProgressTable by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProjectProgressTable : {}", id);
        projectProgressTableRepository.delete(id);
    }
}
