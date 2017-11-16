package com.yijia.yy.service;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.dto.CommentDTO;
import org.springframework.data.domain.Sort;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Comment.
 */
public interface CommentService {

    /**
     * Save a comment.
     *
     * @param commentDTO the entity to save
     * @return the persisted entity
     */
    CommentDTO save(CommentDTO commentDTO);

    /**
     *  Get all the comments.
     *
     *  @param sort the order
     *  @return the list of entities
     */
    List<CommentDTO> findAll(Sort sort);

    /**
     *  Get all the comments by predicate.
     *
     *  @param predicate the predicate
     *  @param sort the order
     *  @return the list of entities
     */
    List<CommentDTO> findAll(Predicate predicate, Sort sort);

    /**
     *  Get the "id" comment.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    CommentDTO findOne(Long id);

    /**
     *  Delete the "id" comment.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
