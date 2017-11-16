package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yijia.yy.service.RoomService;
import com.yijia.yy.web.rest.util.HeaderUtil;
import com.yijia.yy.service.dto.RoomDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Room.
 */
@RestController
@RequestMapping("/api")
public class RoomResource {

    private final Logger log = LoggerFactory.getLogger(RoomResource.class);
        
    @Inject
    private RoomService roomService;

    /**
     * POST  /rooms : Create a new room.
     *
     * @param roomDTO the roomDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new roomDTO, or with status 400 (Bad Request) if the room has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/rooms",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RoomDTO> createRoom(@RequestBody RoomDTO roomDTO) throws URISyntaxException {
        log.debug("REST request to save Room : {}", roomDTO);
        if (roomDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("room", "idexists", "A new room cannot already have an ID")).body(null);
        }
        RoomDTO result = roomService.save(roomDTO);
        return ResponseEntity.created(new URI("/api/rooms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("room", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rooms : Updates an existing room.
     *
     * @param roomDTO the roomDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated roomDTO,
     * or with status 400 (Bad Request) if the roomDTO is not valid,
     * or with status 500 (Internal Server Error) if the roomDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/rooms",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RoomDTO> updateRoom(@RequestBody RoomDTO roomDTO) throws URISyntaxException {
        log.debug("REST request to update Room : {}", roomDTO);
        if (roomDTO.getId() == null) {
            return createRoom(roomDTO);
        }
        RoomDTO result = roomService.save(roomDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("room", roomDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rooms : get all the rooms.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of rooms in body
     */
    @RequestMapping(value = "/rooms",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<RoomDTO> getAllRooms() {
        log.debug("REST request to get all Rooms");
        return roomService.findAll();
    }

    /**
     * GET  /rooms/:id : get the "id" room.
     *
     * @param id the id of the roomDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the roomDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/rooms/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RoomDTO> getRoom(@PathVariable Long id) {
        log.debug("REST request to get Room : {}", id);
        RoomDTO roomDTO = roomService.findOne(id);
        return Optional.ofNullable(roomDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /rooms/:id : delete the "id" room.
     *
     * @param id the id of the roomDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/rooms/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        log.debug("REST request to delete Room : {}", id);
        roomService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("room", id.toString())).build();
    }

}
