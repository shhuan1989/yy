package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.ProjectCostService;
import com.yijia.yy.service.dto.ProjectCostSearchDTO;
import com.yijia.yy.web.rest.util.HeaderUtil;
import com.yijia.yy.service.dto.ProjectCostDTO;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing ProjectCost.
 */
@RestController
@RequestMapping("/api")
public class ProjectCostResource {

    private final Logger log = LoggerFactory.getLogger(ProjectCostResource.class);

    @Inject
    private ProjectCostService projectCostService;

    /**
     * POST  /project-costs : Create a new projectCost.
     *
     * @param projectCostDTO the projectCostDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projectCostDTO, or with status 400 (Bad Request) if the projectCost has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/project-costs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectCostDTO> createProjectCost(@RequestBody ProjectCostDTO projectCostDTO) throws URISyntaxException {
        log.debug("REST request to save ProjectCost : {}", projectCostDTO);
        if (projectCostDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("projectCost", "idexists", "A new projectCost cannot already have an ID")).body(null);
        }
        ProjectCostDTO result = projectCostService.save(projectCostDTO);
        return ResponseEntity.created(new URI("/api/project-costs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("projectCost", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /project-costs : Updates an existing projectCost.
     *
     * @param projectCostDTO the projectCostDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projectCostDTO,
     * or with status 400 (Bad Request) if the projectCostDTO is not valid,
     * or with status 500 (Internal Server Error) if the projectCostDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/project-costs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectCostDTO> updateProjectCost(@RequestBody ProjectCostDTO projectCostDTO) throws URISyntaxException {
        log.debug("REST request to update ProjectCost : {}", projectCostDTO);
        if (projectCostDTO.getId() == null) {
            return createProjectCost(projectCostDTO);
        }
        ProjectCostDTO result = projectCostService.save(projectCostDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("projectCost", projectCostDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /project-costs : get all the projectCosts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of projectCosts in body
     */
    @RequestMapping(value = "/project-costs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ProjectCostDTO> getAllProjectCosts(ProjectCostSearchDTO searchDTO, Sort sort) {
        log.debug("REST request to get all ProjectCosts");
        Predicate predicate = QueryDslUtil.ProjectCostSearchDTO2Predicate(searchDTO);
        return predicate == null ? projectCostService.findAll(sort) : projectCostService.findAll(predicate, sort);
    }

    /**
     * GET  /project-costs/:id : get the "id" projectCost.
     *
     * @param id the id of the projectCostDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projectCostDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/project-costs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectCostDTO> getProjectCost(@PathVariable Long id) {
        log.debug("REST request to get ProjectCost : {}", id);
        ProjectCostDTO projectCostDTO = projectCostService.findOne(id);
        return Optional.ofNullable(projectCostDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /project-costs/:id : delete the "id" projectCost.
     *
     * @param id the id of the projectCostDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/project-costs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProjectCost(@PathVariable Long id) {
        log.debug("REST request to delete ProjectCost : {}", id);
        projectCostService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("projectCost", id.toString())).build();
    }

}
