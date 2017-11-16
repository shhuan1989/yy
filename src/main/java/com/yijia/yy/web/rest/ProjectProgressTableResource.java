package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.ProjectProgressTableService;
import com.yijia.yy.service.dto.ProjectProgressTableSearchDTO;
import com.yijia.yy.web.rest.util.HeaderUtil;
import com.yijia.yy.service.dto.ProjectProgressTableDTO;
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
 * REST controller for managing ProjectProgressTable.
 */
@RestController
@RequestMapping("/api")
public class ProjectProgressTableResource {

    private final Logger log = LoggerFactory.getLogger(ProjectProgressTableResource.class);

    @Inject
    private ProjectProgressTableService projectProgressTableService;

    /**
     * POST  /project-progress-tables : Create a new projectProgressTable.
     *
     * @param projectProgressTableDTO the projectProgressTableDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projectProgressTableDTO, or with status 400 (Bad Request) if the projectProgressTable has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/project-progress-tables",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectProgressTableDTO> createProjectProgressTable(@RequestBody ProjectProgressTableDTO projectProgressTableDTO) throws URISyntaxException {
        log.debug("REST request to save ProjectProgressTable : {}", projectProgressTableDTO);
        if (projectProgressTableDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("projectProgressTable", "idexists", "A new projectProgressTable cannot already have an ID")).body(null);
        }
        ProjectProgressTableDTO result = projectProgressTableService.save(projectProgressTableDTO);
        return ResponseEntity.created(new URI("/api/project-progress-tables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("projectProgressTable", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /project-progress-tables : Updates an existing projectProgressTable.
     *
     * @param projectProgressTableDTO the projectProgressTableDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projectProgressTableDTO,
     * or with status 400 (Bad Request) if the projectProgressTableDTO is not valid,
     * or with status 500 (Internal Server Error) if the projectProgressTableDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/project-progress-tables",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectProgressTableDTO> updateProjectProgressTable(@RequestBody ProjectProgressTableDTO projectProgressTableDTO) throws URISyntaxException {
        log.debug("REST request to update ProjectProgressTable : {}", projectProgressTableDTO);
        if (projectProgressTableDTO.getId() == null) {
            return createProjectProgressTable(projectProgressTableDTO);
        }
        ProjectProgressTableDTO result = projectProgressTableService.save(projectProgressTableDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("projectProgressTable", projectProgressTableDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /project-progress-tables : get all the projectProgressTables.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of projectProgressTables in body
     */
    @RequestMapping(value = "/project-progress-tables",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ProjectProgressTableDTO> getAllProjectProgressTables(ProjectProgressTableSearchDTO searchDTO, Sort sort) {
        log.debug("REST request to get all ProjectProgressTables");
        Predicate predicate = QueryDslUtil.ProjectProgressTableSearchDTO2Predicate(searchDTO);
        return predicate == null ? projectProgressTableService.findAll(sort) : projectProgressTableService.findAll(predicate, sort);
    }

    /**
     * GET  /project-progress-tables/:id : get the "id" projectProgressTable.
     *
     * @param id the id of the projectProgressTableDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projectProgressTableDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/project-progress-tables/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectProgressTableDTO> getProjectProgressTable(@PathVariable Long id) {
        log.debug("REST request to get ProjectProgressTable : {}", id);
        ProjectProgressTableDTO projectProgressTableDTO = projectProgressTableService.findOne(id);
        return Optional.ofNullable(projectProgressTableDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /project-progress-tables/:id : delete the "id" projectProgressTable.
     *
     * @param id the id of the projectProgressTableDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/project-progress-tables/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProjectProgressTable(@PathVariable Long id) {
        log.debug("REST request to delete ProjectProgressTable : {}", id);
        projectProgressTableService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("projectProgressTable", id.toString())).build();
    }

}
