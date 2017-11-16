package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yijia.yy.service.DeptService;
import com.yijia.yy.web.rest.util.HeaderUtil;
import com.yijia.yy.web.rest.util.PaginationUtil;
import com.yijia.yy.service.dto.DeptDTO;
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
 * REST controller for managing Dept.
 */
@RestController
@RequestMapping("/api")
public class DeptResource {

    private final Logger log = LoggerFactory.getLogger(DeptResource.class);
        
    @Inject
    private DeptService deptService;

    /**
     * POST  /depts : Create a new dept.
     *
     * @param deptDTO the deptDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new deptDTO, or with status 400 (Bad Request) if the dept has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/depts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DeptDTO> createDept(@RequestBody DeptDTO deptDTO) throws URISyntaxException {
        log.debug("REST request to save Dept : {}", deptDTO);
        if (deptDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("dept", "idexists", "A new dept cannot already have an ID")).body(null);
        }
        DeptDTO result = deptService.save(deptDTO);
        return ResponseEntity.created(new URI("/api/depts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("dept", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /depts : Updates an existing dept.
     *
     * @param deptDTO the deptDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated deptDTO,
     * or with status 400 (Bad Request) if the deptDTO is not valid,
     * or with status 500 (Internal Server Error) if the deptDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/depts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DeptDTO> updateDept(@RequestBody DeptDTO deptDTO) throws URISyntaxException {
        log.debug("REST request to update Dept : {}", deptDTO);
        if (deptDTO.getId() == null) {
            return createDept(deptDTO);
        }
        DeptDTO result = deptService.save(deptDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("dept", deptDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /depts : get all the depts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of depts in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/depts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<DeptDTO>> getAllDepts(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Depts");
        Page<DeptDTO> page = deptService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/depts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /depts/:id : get the "id" dept.
     *
     * @param id the id of the deptDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the deptDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/depts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DeptDTO> getDept(@PathVariable Long id) {
        log.debug("REST request to get Dept : {}", id);
        DeptDTO deptDTO = deptService.findOne(id);
        return Optional.ofNullable(deptDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /depts/:id : delete the "id" dept.
     *
     * @param id the id of the deptDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/depts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDept(@PathVariable Long id) {
        log.debug("REST request to delete Dept : {}", id);
        deptService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("dept", id.toString())).build();
    }

}
