package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yijia.yy.service.ProjectProgressItemService;
import com.yijia.yy.web.rest.util.HeaderUtil;
import com.yijia.yy.service.dto.ProjectProgressItemDTO;
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
 * REST controller for managing ProjectProgressItem.
 */
@RestController
@RequestMapping("/api")
public class ProjectProgressItemResource {

    private final Logger log = LoggerFactory.getLogger(ProjectProgressItemResource.class);
        
    @Inject
    private ProjectProgressItemService projectProgressItemService;

    /**
     * POST  /project-progress-items : Create a new projectProgressItem.
     *
     * @param projectProgressItemDTO the projectProgressItemDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projectProgressItemDTO, or with status 400 (Bad Request) if the projectProgressItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/project-progress-items",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectProgressItemDTO> createProjectProgressItem(@RequestBody ProjectProgressItemDTO projectProgressItemDTO) throws URISyntaxException {
        log.debug("REST request to save ProjectProgressItem : {}", projectProgressItemDTO);
        if (projectProgressItemDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("projectProgressItem", "idexists", "A new projectProgressItem cannot already have an ID")).body(null);
        }
        ProjectProgressItemDTO result = projectProgressItemService.save(projectProgressItemDTO);
        return ResponseEntity.created(new URI("/api/project-progress-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("projectProgressItem", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /project-progress-items : Updates an existing projectProgressItem.
     *
     * @param projectProgressItemDTO the projectProgressItemDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projectProgressItemDTO,
     * or with status 400 (Bad Request) if the projectProgressItemDTO is not valid,
     * or with status 500 (Internal Server Error) if the projectProgressItemDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/project-progress-items",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectProgressItemDTO> updateProjectProgressItem(@RequestBody ProjectProgressItemDTO projectProgressItemDTO) throws URISyntaxException {
        log.debug("REST request to update ProjectProgressItem : {}", projectProgressItemDTO);
        if (projectProgressItemDTO.getId() == null) {
            return createProjectProgressItem(projectProgressItemDTO);
        }
        ProjectProgressItemDTO result = projectProgressItemService.save(projectProgressItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("projectProgressItem", projectProgressItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /project-progress-items : get all the projectProgressItems.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of projectProgressItems in body
     */
    @RequestMapping(value = "/project-progress-items",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ProjectProgressItemDTO> getAllProjectProgressItems() {
        log.debug("REST request to get all ProjectProgressItems");
        return projectProgressItemService.findAll();
    }

    /**
     * GET  /project-progress-items/:id : get the "id" projectProgressItem.
     *
     * @param id the id of the projectProgressItemDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projectProgressItemDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/project-progress-items/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectProgressItemDTO> getProjectProgressItem(@PathVariable Long id) {
        log.debug("REST request to get ProjectProgressItem : {}", id);
        ProjectProgressItemDTO projectProgressItemDTO = projectProgressItemService.findOne(id);
        return Optional.ofNullable(projectProgressItemDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /project-progress-items/:id : delete the "id" projectProgressItem.
     *
     * @param id the id of the projectProgressItemDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/project-progress-items/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProjectProgressItem(@PathVariable Long id) {
        log.debug("REST request to delete ProjectProgressItem : {}", id);
        projectProgressItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("projectProgressItem", id.toString())).build();
    }

}
