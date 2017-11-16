package com.yijia.yy.service;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.dto.ActorDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Actor.
 */
public interface ActorService {

    /**
     * Save a actor.
     *
     * @param actorDTO the entity to save
     * @return the persisted entity
     */
    ActorDTO save(ActorDTO actorDTO);

    /**
     *  Get all the actors.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ActorDTO> findAll(Pageable pageable);

    /**
     *  Get all the actors by predicate.
     *
     *  @param predicate the predicate
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ActorDTO> findAll(Predicate predicate, Pageable pageable);

    /**
     *  Get the "id" actor.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ActorDTO findOne(Long id);

    /**
     *  Delete the "id" actor.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     *
     * @return count of actors
     */
    long count();

    /**
     *
     * @return count of actors
     */
    long count(Predicate predicate);
}
