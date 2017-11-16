package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.ActorService;
import com.yijia.yy.service.dto.ActorSearchDTO;
import com.yijia.yy.web.rest.util.HeaderUtil;
import com.yijia.yy.web.rest.util.PaginationUtil;
import com.yijia.yy.service.dto.ActorDTO;
import com.yijia.yy.web.util.QueryDslUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Actor.
 */
@RestController
@RequestMapping("/api")
public class ActorResource {

    private final Logger log = LoggerFactory.getLogger(ActorResource.class);

    @Inject
    private ActorService actorService;

    /**
     * POST  /actors : Create a new actor.
     *
     * @param actorDTO the actorDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new actorDTO, or with status 400 (Bad Request) if the actor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/actors",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ActorDTO> createActor(@Valid @RequestBody ActorDTO actorDTO) throws URISyntaxException {
        log.debug("REST request to save Actor : {}", actorDTO);
        if (actorDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("actor", "idexists", "A new actor cannot already have an ID")).body(null);
        }
        ActorDTO result = actorService.save(actorDTO);
        return ResponseEntity.created(new URI("/api/actors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("actor", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /actors : Updates an existing actor.
     *
     * @param actorDTO the actorDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated actorDTO,
     * or with status 400 (Bad Request) if the actorDTO is not valid,
     * or with status 500 (Internal Server Error) if the actorDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/actors",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ActorDTO> updateActor(@Valid @RequestBody ActorDTO actorDTO) throws URISyntaxException {
        log.debug("REST request to update Actor : {}", actorDTO);
        if (actorDTO.getId() == null) {
            return createActor(actorDTO);
        }
        ActorDTO result = actorService.save(actorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("actor", actorDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /actors : get all the actors.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of actors in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/actors",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Page<ActorDTO>> getAllActors(Pageable pageable, ActorSearchDTO searchDTO)
        throws URISyntaxException {
        log.debug("REST request to get a page of Actors");
        Predicate predicate = QueryDslUtil.ActorSearchDTO2Predicate(searchDTO);
        Page<ActorDTO> page = predicate == null ? actorService.findAll(pageable) : actorService.findAll(predicate, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/actors");
        return new ResponseEntity<>(page, headers, HttpStatus.OK);
    }

    /**
     * GET  /actors/:id : get the "id" actor.
     *
     * @param id the id of the actorDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the actorDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/actors/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ActorDTO> getActor(@PathVariable Long id) {
        log.debug("REST request to get Actor : {}", id);
        ActorDTO actorDTO = actorService.findOne(id);
        return Optional.ofNullable(actorDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /actors/:id : delete the "id" actor.
     *
     * @param id the id of the actorDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/actors/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteActor(@PathVariable Long id) {
        log.debug("REST request to delete Actor : {}", id);
        actorService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("actor", id.toString())).build();
    }

}
