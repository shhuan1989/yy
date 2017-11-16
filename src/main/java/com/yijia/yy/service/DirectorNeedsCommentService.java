package com.yijia.yy.service;

import com.yijia.yy.service.dto.DirectorNeedsCommentDTO;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing DirectorNeedsComment.
 */
public interface DirectorNeedsCommentService {

    /**
     * Save a directorNeedsComment.
     *
     * @param directorNeedsCommentDTO the entity to save
     * @return the persisted entity
     */
    DirectorNeedsCommentDTO save(DirectorNeedsCommentDTO directorNeedsCommentDTO);

    /**
     *  Get all the directorNeedsComments.
     *  
     *  @return the list of entities
     */
    List<DirectorNeedsCommentDTO> findAll();

    /**
     *  Get the "id" directorNeedsComment.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    DirectorNeedsCommentDTO findOne(Long id);

    /**
     *  Delete the "id" directorNeedsComment.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
