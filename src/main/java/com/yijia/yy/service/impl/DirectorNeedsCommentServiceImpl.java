package com.yijia.yy.service.impl;

import com.yijia.yy.service.DirectorNeedsCommentService;
import com.yijia.yy.domain.DirectorNeedsComment;
import com.yijia.yy.repository.DirectorNeedsCommentRepository;
import com.yijia.yy.service.dto.DirectorNeedsCommentDTO;
import com.yijia.yy.service.mapper.DirectorNeedsCommentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing DirectorNeedsComment.
 */
@Service
@Transactional
public class DirectorNeedsCommentServiceImpl implements DirectorNeedsCommentService{

    private final Logger log = LoggerFactory.getLogger(DirectorNeedsCommentServiceImpl.class);
    
    @Inject
    private DirectorNeedsCommentRepository directorNeedsCommentRepository;

    @Inject
    private DirectorNeedsCommentMapper directorNeedsCommentMapper;

    /**
     * Save a directorNeedsComment.
     *
     * @param directorNeedsCommentDTO the entity to save
     * @return the persisted entity
     */
    public DirectorNeedsCommentDTO save(DirectorNeedsCommentDTO directorNeedsCommentDTO) {
        log.debug("Request to save DirectorNeedsComment : {}", directorNeedsCommentDTO);
        DirectorNeedsComment directorNeedsComment = directorNeedsCommentMapper.directorNeedsCommentDTOToDirectorNeedsComment(directorNeedsCommentDTO);
        directorNeedsComment = directorNeedsCommentRepository.save(directorNeedsComment);
        DirectorNeedsCommentDTO result = directorNeedsCommentMapper.directorNeedsCommentToDirectorNeedsCommentDTO(directorNeedsComment);
        return result;
    }

    /**
     *  Get all the directorNeedsComments.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<DirectorNeedsCommentDTO> findAll() {
        log.debug("Request to get all DirectorNeedsComments");
        List<DirectorNeedsCommentDTO> result = directorNeedsCommentRepository.findAll().stream()
            .map(directorNeedsCommentMapper::directorNeedsCommentToDirectorNeedsCommentDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one directorNeedsComment by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public DirectorNeedsCommentDTO findOne(Long id) {
        log.debug("Request to get DirectorNeedsComment : {}", id);
        DirectorNeedsComment directorNeedsComment = directorNeedsCommentRepository.findOne(id);
        DirectorNeedsCommentDTO directorNeedsCommentDTO = directorNeedsCommentMapper.directorNeedsCommentToDirectorNeedsCommentDTO(directorNeedsComment);
        return directorNeedsCommentDTO;
    }

    /**
     *  Delete the  directorNeedsComment by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete DirectorNeedsComment : {}", id);
        directorNeedsCommentRepository.delete(id);
    }
}
