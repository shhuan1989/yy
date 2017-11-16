package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.ProjectTimelineService;
import com.yijia.yy.service.dto.ProjectTimelineSearchDTO;
import com.yijia.yy.web.rest.util.HeaderUtil;
import com.yijia.yy.service.dto.ProjectTimelineDTO;
import com.yijia.yy.web.util.QueryDslUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
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
 * REST controller for managing ProjectTimeline.
 */
@RestController
@RequestMapping("/api")
public class ProjectTimelineResource {

    private final Logger log = LoggerFactory.getLogger(ProjectTimelineResource.class);

    @Inject
    private ProjectTimelineService projectTimelineService;

    /**
     * POST  /project-timelines : Create a new projectTimeline.
     *
     * @param projectTimelineDTO the projectTimelineDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projectTimelineDTO, or with status 400 (Bad Request) if the projectTimeline has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/project-timelines",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectTimelineDTO> createProjectTimeline(@Valid @RequestBody ProjectTimelineDTO projectTimelineDTO) throws URISyntaxException {
        log.debug("REST request to save ProjectTimeline : {}", projectTimelineDTO);
        if (projectTimelineDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("projectTimeline", "idexists", "A new projectTimeline cannot already have an ID")).body(null);
        }
        ProjectTimelineDTO result = projectTimelineService.save(projectTimelineDTO);
        return ResponseEntity.created(new URI("/api/project-timelines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("projectTimeline", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /project-timelines : Updates an existing projectTimeline.
     *
     * @param projectTimelineDTO the projectTimelineDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projectTimelineDTO,
     * or with status 400 (Bad Request) if the projectTimelineDTO is not valid,
     * or with status 500 (Internal Server Error) if the projectTimelineDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/project-timelines",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectTimelineDTO> updateProjectTimeline(@Valid @RequestBody ProjectTimelineDTO projectTimelineDTO) throws URISyntaxException {
        log.debug("REST request to update ProjectTimeline : {}", projectTimelineDTO);
        if (projectTimelineDTO.getId() == null) {
            return createProjectTimeline(projectTimelineDTO);
        }
        ProjectTimelineDTO result = projectTimelineService.save(projectTimelineDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("projectTimeline", projectTimelineDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /project-timelines : get all the projectTimelines.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of projectTimelines in body
     */
    @RequestMapping(value = "/project-timelines",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ProjectTimelineDTO> getAllProjectTimelines(ProjectTimelineSearchDTO searchDTO, Sort sort) {
        log.debug("REST request to get all ProjectTimelines");
        Predicate predicate = QueryDslUtil.ProjectTimelineSearchDTO2Predicate(searchDTO);
        return predicate == null ? projectTimelineService.findAll(sort) : projectTimelineService.findAll(predicate, sort);
    }

    /**
     * GET  /project-timelines/:id : get the "id" projectTimeline.
     *
     * @param id the id of the projectTimelineDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projectTimelineDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/project-timelines/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectTimelineDTO> getProjectTimeline(@PathVariable Long id) {
        log.debug("REST request to get ProjectTimeline : {}", id);
        ProjectTimelineDTO projectTimelineDTO = projectTimelineService.findOne(id);
        return Optional.ofNullable(projectTimelineDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /project-timelines/:id : delete the "id" projectTimeline.
     *
     * @param id the id of the projectTimelineDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/project-timelines/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProjectTimeline(@PathVariable Long id) {
        log.debug("REST request to delete ProjectTimeline : {}", id);
        projectTimelineService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("projectTimeline", id.toString())).build();
    }

}
