package com.yijia.yy.service;

import com.yijia.yy.service.dto.RoomDTO;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Room.
 */
public interface RoomService {

    /**
     * Save a room.
     *
     * @param roomDTO the entity to save
     * @return the persisted entity
     */
    RoomDTO save(RoomDTO roomDTO);

    /**
     *  Get all the rooms.
     *  
     *  @return the list of entities
     */
    List<RoomDTO> findAll();

    /**
     *  Get the "id" room.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    RoomDTO findOne(Long id);

    /**
     *  Delete the "id" room.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
