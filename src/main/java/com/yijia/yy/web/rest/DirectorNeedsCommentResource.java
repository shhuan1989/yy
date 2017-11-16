package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yijia.yy.service.DirectorNeedsCommentService;
import com.yijia.yy.web.rest.util.HeaderUtil;
import com.yijia.yy.service.dto.DirectorNeedsCommentDTO;
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
 * REST controller for managing DirectorNeedsComment.
 */
@RestController
@RequestMapping("/api")
public class DirectorNeedsCommentResource {

    private final Logger log = LoggerFactory.getLogger(DirectorNeedsCommentResource.class);
        
    @Inject
    private DirectorNeedsCommentService directorNeedsCommentService;

    /**
     * POST  /director-needs-comments : Create a new directorNeedsComment.
     *
     * @param directorNeedsCommentDTO the directorNeedsCommentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new directorNeedsCommentDTO, or with status 400 (Bad Request) if the directorNeedsComment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/director-needs-comments",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DirectorNeedsCommentDTO> createDirectorNeedsComment(@RequestBody DirectorNeedsCommentDTO directorNeedsCommentDTO) throws URISyntaxException {
        log.debug("REST request to save DirectorNeedsComment : {}", directorNeedsCommentDTO);
        if (directorNeedsCommentDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("directorNeedsComment", "idexists", "A new directorNeedsComment cannot already have an ID")).body(null);
        }
        DirectorNeedsCommentDTO result = directorNeedsCommentService.save(directorNeedsCommentDTO);
        return ResponseEntity.created(new URI("/api/director-needs-comments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("directorNeedsComment", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /director-needs-comments : Updates an existing directorNeedsComment.
     *
     * @param directorNeedsCommentDTO the directorNeedsCommentDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated directorNeedsCommentDTO,
     * or with status 400 (Bad Request) if the directorNeedsCommentDTO is not valid,
     * or with status 500 (Internal Server Error) if the directorNeedsCommentDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/director-needs-comments",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DirectorNeedsCommentDTO> updateDirectorNeedsComment(@RequestBody DirectorNeedsCommentDTO directorNeedsCommentDTO) throws URISyntaxException {
        log.debug("REST request to update DirectorNeedsComment : {}", directorNeedsCommentDTO);
        if (directorNeedsCommentDTO.getId() == null) {
            return createDirectorNeedsComment(directorNeedsCommentDTO);
        }
        DirectorNeedsCommentDTO result = directorNeedsCommentService.save(directorNeedsCommentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("directorNeedsComment", directorNeedsCommentDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /director-needs-comments : get all the directorNeedsComments.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of directorNeedsComments in body
     */
    @RequestMapping(value = "/director-needs-comments",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<DirectorNeedsCommentDTO> getAllDirectorNeedsComments() {
        log.debug("REST request to get all DirectorNeedsComments");
        return directorNeedsCommentService.findAll();
    }

    /**
     * GET  /director-needs-comments/:id : get the "id" directorNeedsComment.
     *
     * @param id the id of the directorNeedsCommentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the directorNeedsCommentDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/director-needs-comments/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DirectorNeedsCommentDTO> getDirectorNeedsComment(@PathVariable Long id) {
        log.debug("REST request to get DirectorNeedsComment : {}", id);
        DirectorNeedsCommentDTO directorNeedsCommentDTO = directorNeedsCommentService.findOne(id);
        return Optional.ofNullable(directorNeedsCommentDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /director-needs-comments/:id : delete the "id" directorNeedsComment.
     *
     * @param id the id of the directorNeedsCommentDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/director-needs-comments/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDirectorNeedsComment(@PathVariable Long id) {
        log.debug("REST request to delete DirectorNeedsComment : {}", id);
        directorNeedsCommentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("directorNeedsComment", id.toString())).build();
    }

}
