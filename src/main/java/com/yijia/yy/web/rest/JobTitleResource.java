package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yijia.yy.service.JobTitleService;
import com.yijia.yy.web.rest.util.HeaderUtil;
import com.yijia.yy.web.rest.util.PaginationUtil;
import com.yijia.yy.service.dto.JobTitleDTO;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing JobTitle.
 */
@RestController
@RequestMapping("/api")
public class JobTitleResource {

    private final Logger log = LoggerFactory.getLogger(JobTitleResource.class);
        
    @Inject
    private JobTitleService jobTitleService;

    /**
     * POST  /job-titles : Create a new jobTitle.
     *
     * @param jobTitleDTO the jobTitleDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new jobTitleDTO, or with status 400 (Bad Request) if the jobTitle has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/job-titles",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JobTitleDTO> createJobTitle(@RequestBody JobTitleDTO jobTitleDTO) throws URISyntaxException {
        log.debug("REST request to save JobTitle : {}", jobTitleDTO);
        if (jobTitleDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("jobTitle", "idexists", "A new jobTitle cannot already have an ID")).body(null);
        }
        JobTitleDTO result = jobTitleService.save(jobTitleDTO);
        return ResponseEntity.created(new URI("/api/job-titles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("jobTitle", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /job-titles : Updates an existing jobTitle.
     *
     * @param jobTitleDTO the jobTitleDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated jobTitleDTO,
     * or with status 400 (Bad Request) if the jobTitleDTO is not valid,
     * or with status 500 (Internal Server Error) if the jobTitleDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/job-titles",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JobTitleDTO> updateJobTitle(@RequestBody JobTitleDTO jobTitleDTO) throws URISyntaxException {
        log.debug("REST request to update JobTitle : {}", jobTitleDTO);
        if (jobTitleDTO.getId() == null) {
            return createJobTitle(jobTitleDTO);
        }
        JobTitleDTO result = jobTitleService.save(jobTitleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("jobTitle", jobTitleDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /job-titles : get all the jobTitles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of jobTitles in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/job-titles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<JobTitleDTO>> getAllJobTitles(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of JobTitles");
        Page<JobTitleDTO> page = jobTitleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/job-titles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /job-titles/:id : get the "id" jobTitle.
     *
     * @param id the id of the jobTitleDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the jobTitleDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/job-titles/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JobTitleDTO> getJobTitle(@PathVariable Long id) {
        log.debug("REST request to get JobTitle : {}", id);
        JobTitleDTO jobTitleDTO = jobTitleService.findOne(id);
        return Optional.ofNullable(jobTitleDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /job-titles/:id : delete the "id" jobTitle.
     *
     * @param id the id of the jobTitleDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/job-titles/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteJobTitle(@PathVariable Long id) {
        log.debug("REST request to delete JobTitle : {}", id);
        jobTitleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("jobTitle", id.toString())).build();
    }

}
