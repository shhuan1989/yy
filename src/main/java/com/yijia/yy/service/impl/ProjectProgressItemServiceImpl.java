package com.yijia.yy.service.impl;

import com.yijia.yy.service.ProjectProgressItemService;
import com.yijia.yy.domain.ProjectProgressItem;
import com.yijia.yy.repository.ProjectProgressItemRepository;
import com.yijia.yy.service.dto.ProjectProgressItemDTO;
import com.yijia.yy.service.mapper.ProjectProgressItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ProjectProgressItem.
 */
@Service
@Transactional
public class ProjectProgressItemServiceImpl implements ProjectProgressItemService{

    private final Logger log = LoggerFactory.getLogger(ProjectProgressItemServiceImpl.class);
    
    @Inject
    private ProjectProgressItemRepository projectProgressItemRepository;

    @Inject
    private ProjectProgressItemMapper projectProgressItemMapper;

    /**
     * Save a projectProgressItem.
     *
     * @param projectProgressItemDTO the entity to save
     * @return the persisted entity
     */
    public ProjectProgressItemDTO save(ProjectProgressItemDTO projectProgressItemDTO) {
        log.debug("Request to save ProjectProgressItem : {}", projectProgressItemDTO);
        ProjectProgressItem projectProgressItem = projectProgressItemMapper.projectProgressItemDTOToProjectProgressItem(projectProgressItemDTO);
        projectProgressItem = projectProgressItemRepository.save(projectProgressItem);
        ProjectProgressItemDTO result = projectProgressItemMapper.projectProgressItemToProjectProgressItemDTO(projectProgressItem);
        return result;
    }

    /**
     *  Get all the projectProgressItems.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<ProjectProgressItemDTO> findAll() {
        log.debug("Request to get all ProjectProgressItems");
        List<ProjectProgressItemDTO> result = projectProgressItemRepository.findAll().stream()
            .map(projectProgressItemMapper::projectProgressItemToProjectProgressItemDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one projectProgressItem by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ProjectProgressItemDTO findOne(Long id) {
        log.debug("Request to get ProjectProgressItem : {}", id);
        ProjectProgressItem projectProgressItem = projectProgressItemRepository.findOne(id);
        ProjectProgressItemDTO projectProgressItemDTO = projectProgressItemMapper.projectProgressItemToProjectProgressItemDTO(projectProgressItem);
        return projectProgressItemDTO;
    }

    /**
     *  Delete the  projectProgressItem by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProjectProgressItem : {}", id);
        projectProgressItemRepository.delete(id);
    }
}
