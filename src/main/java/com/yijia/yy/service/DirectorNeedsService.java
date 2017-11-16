package com.yijia.yy.service;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.dto.DirectorNeedsDTO;
import org.springframework.data.domain.Sort;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing DirectorNeeds.
 */
public interface DirectorNeedsService {

    /**
     * Save a directorNeeds.
     *
     * @param directorNeedsDTO the entity to save
     * @return the persisted entity
     */
    DirectorNeedsDTO save(DirectorNeedsDTO directorNeedsDTO);

    /**
     *  Get all the directorNeeds.
     *
     *  @param sort the order
     *  @return the list of entities
     */
    List<DirectorNeedsDTO> findAll(Sort sort);

    /**
     *  Get all the directorNeeds.
     *
     *  @param predicate the predicate
     *  @param sort the order
     *  @return the list of entities
     */
    List<DirectorNeedsDTO> findAll(Predicate predicate, Sort sort);

    /**
     *  Get the "id" directorNeeds.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    DirectorNeedsDTO findOne(Long id);

    /**
     *  Delete the "id" directorNeeds.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
