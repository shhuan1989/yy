package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.StaffService;
import com.yijia.yy.service.dto.StaffSearchDTO;
import com.yijia.yy.web.rest.util.HeaderUtil;
import com.yijia.yy.web.rest.util.PaginationUtil;
import com.yijia.yy.service.dto.StaffDTO;
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
 * REST controller for managing Staff.
 */
@RestController
@RequestMapping("/api")
public class StaffResource {

    private final Logger log = LoggerFactory.getLogger(StaffResource.class);

    @Inject
    private StaffService staffService;

    /**
     * POST  /staff : Create a new staff.
     *
     * @param staffDTO the staffDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new staffDTO, or with status 400 (Bad Request) if the staff has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/staff",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StaffDTO> createStaff(@Valid @RequestBody StaffDTO staffDTO) throws URISyntaxException {
        log.debug("REST request to save Staff : {}", staffDTO);
        if (staffDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("staff", "idexists", "A new staff cannot already have an ID")).body(null);
        }
        StaffDTO result = staffService.save(staffDTO);
        return ResponseEntity.created(new URI("/api/staff/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("staff", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /staff : Updates an existing staff.
     *
     * @param staffDTO the staffDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated staffDTO,
     * or with status 400 (Bad Request) if the staffDTO is not valid,
     * or with status 500 (Internal Server Error) if the staffDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/staff",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StaffDTO> updateStaff(@Valid @RequestBody StaffDTO staffDTO) throws URISyntaxException {
        log.debug("REST request to update Staff : {}", staffDTO);
        if (staffDTO.getId() == null) {
            return createStaff(staffDTO);
        }
        StaffDTO result = staffService.save(staffDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("staff", staffDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /staff : get all the staff.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of staff in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/staff",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<StaffDTO>> getAllStaff(Pageable pageable, StaffSearchDTO searchDTO)
        throws URISyntaxException {
        log.debug("REST request to get a page of Staff");
        Predicate predicate = QueryDslUtil.StaffSearchDto2Predicate(searchDTO);
        Page<StaffDTO> page = predicate == null ? staffService.findAll(pageable) : staffService.findAll(predicate, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/staff");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /staff/:id : get the "id" staff.
     *
     * @param id the id of the staffDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the staffDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/staff/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StaffDTO> getStaff(@PathVariable Long id) {
        log.debug("REST request to get Staff : {}", id);
        StaffDTO staffDTO = staffService.findOne(id);
        return Optional.ofNullable(staffDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /staff/:id : delete the "id" staff.
     *
     * @param id the id of the staffDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/staff/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStaff(@PathVariable Long id) {
        log.debug("REST request to delete Staff : {}", id);
        staffService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("staff", id.toString())).build();
    }

}
