package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yijia.yy.service.DirectorNeedsItemService;
import com.yijia.yy.web.rest.util.HeaderUtil;
import com.yijia.yy.service.dto.DirectorNeedsItemDTO;
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
 * REST controller for managing DirectorNeedsItem.
 */
@RestController
@RequestMapping("/api")
public class DirectorNeedsItemResource {

    private final Logger log = LoggerFactory.getLogger(DirectorNeedsItemResource.class);
        
    @Inject
    private DirectorNeedsItemService directorNeedsItemService;

    /**
     * POST  /director-needs-items : Create a new directorNeedsItem.
     *
     * @param directorNeedsItemDTO the directorNeedsItemDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new directorNeedsItemDTO, or with status 400 (Bad Request) if the directorNeedsItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/director-needs-items",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DirectorNeedsItemDTO> createDirectorNeedsItem(@RequestBody DirectorNeedsItemDTO directorNeedsItemDTO) throws URISyntaxException {
        log.debug("REST request to save DirectorNeedsItem : {}", directorNeedsItemDTO);
        if (directorNeedsItemDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("directorNeedsItem", "idexists", "A new directorNeedsItem cannot already have an ID")).body(null);
        }
        DirectorNeedsItemDTO result = directorNeedsItemService.save(directorNeedsItemDTO);
        return ResponseEntity.created(new URI("/api/director-needs-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("directorNeedsItem", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /director-needs-items : Updates an existing directorNeedsItem.
     *
     * @param directorNeedsItemDTO the directorNeedsItemDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated directorNeedsItemDTO,
     * or with status 400 (Bad Request) if the directorNeedsItemDTO is not valid,
     * or with status 500 (Internal Server Error) if the directorNeedsItemDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/director-needs-items",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DirectorNeedsItemDTO> updateDirectorNeedsItem(@RequestBody DirectorNeedsItemDTO directorNeedsItemDTO) throws URISyntaxException {
        log.debug("REST request to update DirectorNeedsItem : {}", directorNeedsItemDTO);
        if (directorNeedsItemDTO.getId() == null) {
            return createDirectorNeedsItem(directorNeedsItemDTO);
        }
        DirectorNeedsItemDTO result = directorNeedsItemService.save(directorNeedsItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("directorNeedsItem", directorNeedsItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /director-needs-items : get all the directorNeedsItems.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of directorNeedsItems in body
     */
    @RequestMapping(value = "/director-needs-items",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<DirectorNeedsItemDTO> getAllDirectorNeedsItems() {
        log.debug("REST request to get all DirectorNeedsItems");
        return directorNeedsItemService.findAll();
    }

    /**
     * GET  /director-needs-items/:id : get the "id" directorNeedsItem.
     *
     * @param id the id of the directorNeedsItemDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the directorNeedsItemDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/director-needs-items/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DirectorNeedsItemDTO> getDirectorNeedsItem(@PathVariable Long id) {
        log.debug("REST request to get DirectorNeedsItem : {}", id);
        DirectorNeedsItemDTO directorNeedsItemDTO = directorNeedsItemService.findOne(id);
        return Optional.ofNullable(directorNeedsItemDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /director-needs-items/:id : delete the "id" directorNeedsItem.
     *
     * @param id the id of the directorNeedsItemDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/director-needs-items/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDirectorNeedsItem(@PathVariable Long id) {
        log.debug("REST request to delete DirectorNeedsItem : {}", id);
        directorNeedsItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("directorNeedsItem", id.toString())).build();
    }

}
