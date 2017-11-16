package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.yijia.yy.domain.QProjectRate;
import com.yijia.yy.service.ProjectRateService;
import com.yijia.yy.service.UserService;
import com.yijia.yy.service.dto.ProjectRateDTO;
import com.yijia.yy.service.dto.ProjectRateSearchDTO;
import com.yijia.yy.web.rest.util.HeaderUtil;
import com.yijia.yy.web.util.QueryDslUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing ProjectRate.
 */
@RestController
@RequestMapping("/api")
public class ProjectRateResource {

    private final Logger log = LoggerFactory.getLogger(ProjectRateResource.class);

    @Inject
    private ProjectRateService projectRateService;

    @Inject
    private UserService userService;

    /**
     * POST  /project-rates : Create a new projectRate.
     *
     * @param projectRateDTO the projectRateDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projectRateDTO, or with status 400 (Bad Request) if the projectRate has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/project-rates",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectRateDTO> createProjectRate(@Valid @RequestBody ProjectRateDTO projectRateDTO) throws URISyntaxException {
        log.debug("REST request to save ProjectRate : {}", projectRateDTO);
        if (projectRateDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("projectRate", "idexists", "A new projectRate cannot already have an ID")).body(null);
        }
        ProjectRateDTO result = projectRateService.save(projectRateDTO);
        return ResponseEntity.created(new URI("/api/project-rates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("projectRate", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /project-rates : Updates an existing projectRate.
     *
     * @param projectRateDTO the projectRateDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projectRateDTO,
     * or with status 400 (Bad Request) if the projectRateDTO is not valid,
     * or with status 500 (Internal Server Error) if the projectRateDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/project-rates",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectRateDTO> updateProjectRate(@Valid @RequestBody ProjectRateDTO projectRateDTO) throws URISyntaxException {
        log.debug("REST request to update ProjectRate : {}", projectRateDTO);
        if (projectRateDTO.getId() == null) {
            return createProjectRate(projectRateDTO);
        }
        ProjectRateDTO result = projectRateService.save(projectRateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("projectRate", projectRateDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /project-rates : get all the projectRates.
     *
     * @param searchDTO the dto for searching
     * @param sort the order
     * @return the ResponseEntity with status 200 (OK) and the list of projectRates in body
     */
    @RequestMapping(value = "/project-rates",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ProjectRateDTO> getAllProjectRates(ProjectRateSearchDTO searchDTO, Sort sort) {
        log.debug("REST request to get all ProjectRates");

        Predicate predicate;
        BooleanExpression p1 = null;
        if (searchDTO.getOnlyOwned() != null && searchDTO.getOnlyOwned()) {
            p1 = QProjectRate.projectRate.owner.id.eq(userService.currentLoginEmployee().getId());
        }
        Predicate p2 = QueryDslUtil.ProjectRateSearchDTO2Predicate(searchDTO);
        if (p1 != null && p2 != null) {
            predicate = p1.and(p2);
        } else if (p2 != null) {
            predicate = p2;
        } else {
            predicate = p1;
        }
        return predicate == null ? projectRateService.findAll(sort) : projectRateService.findAll(predicate, sort);
    }

    @RequestMapping(value = "/project-rates/privileges",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional
    public ResponseEntity<Map<String, Boolean>> getAllProjectRatePrivileges() throws IOException {
        Map<String, Boolean> result = projectRateService.getAllProjectRatePrivilegesOfEmployee(userService.currentLoginEmployee().getId());
        return ResponseEntity.ok(result);
    }

    /**
     * GET  /project-rates/:id : get the "id" projectRate.
     *
     * @param id the id of the projectRateDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projectRateDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/project-rates/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProjectRateDTO> getProjectRate(@PathVariable Long id) {
        log.debug("REST request to get ProjectRate : {}", id);
        ProjectRateDTO projectRateDTO = projectRateService.findOne(id);
        return Optional.ofNullable(projectRateDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /project-rates/:id : delete the "id" projectRate.
     *
     * @param id the id of the projectRateDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/project-rates/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProjectRate(@PathVariable Long id) {
        log.debug("REST request to delete ProjectRate : {}", id);
        projectRateService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("projectRate", id.toString())).build();
    }

}
