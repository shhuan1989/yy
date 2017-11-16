package com.yijia.yy.service;

import com.yijia.yy.service.dto.DirectorNeedsItemDTO;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing DirectorNeedsItem.
 */
public interface DirectorNeedsItemService {

    /**
     * Save a directorNeedsItem.
     *
     * @param directorNeedsItemDTO the entity to save
     * @return the persisted entity
     */
    DirectorNeedsItemDTO save(DirectorNeedsItemDTO directorNeedsItemDTO);

    /**
     *  Get all the directorNeedsItems.
     *  
     *  @return the list of entities
     */
    List<DirectorNeedsItemDTO> findAll();

    /**
     *  Get the "id" directorNeedsItem.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    DirectorNeedsItemDTO findOne(Long id);

    /**
     *  Delete the "id" directorNeedsItem.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
