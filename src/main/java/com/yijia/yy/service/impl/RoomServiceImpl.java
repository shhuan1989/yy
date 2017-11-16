package com.yijia.yy.service.impl;

import com.yijia.yy.service.RoomService;
import com.yijia.yy.domain.Room;
import com.yijia.yy.repository.RoomRepository;
import com.yijia.yy.service.dto.RoomDTO;
import com.yijia.yy.service.mapper.RoomMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Room.
 */
@Service
@Transactional
public class RoomServiceImpl implements RoomService{

    private final Logger log = LoggerFactory.getLogger(RoomServiceImpl.class);
    
    @Inject
    private RoomRepository roomRepository;

    @Inject
    private RoomMapper roomMapper;

    /**
     * Save a room.
     *
     * @param roomDTO the entity to save
     * @return the persisted entity
     */
    public RoomDTO save(RoomDTO roomDTO) {
        log.debug("Request to save Room : {}", roomDTO);
        Room room = roomMapper.roomDTOToRoom(roomDTO);
        room = roomRepository.save(room);
        RoomDTO result = roomMapper.roomToRoomDTO(room);
        return result;
    }

    /**
     *  Get all the rooms.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<RoomDTO> findAll() {
        log.debug("Request to get all Rooms");
        List<RoomDTO> result = roomRepository.findAll().stream()
            .map(roomMapper::roomToRoomDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one room by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public RoomDTO findOne(Long id) {
        log.debug("Request to get Room : {}", id);
        Room room = roomRepository.findOne(id);
        RoomDTO roomDTO = roomMapper.roomToRoomDTO(room);
        return roomDTO;
    }

    /**
     *  Delete the  room by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Room : {}", id);
        roomRepository.delete(id);
    }
}
