package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.OvertimeWorkService;
import com.yijia.yy.service.UserService;
import com.yijia.yy.service.dto.OvertimeWorkSearchDTO;
import com.yijia.yy.web.rest.util.HeaderUtil;
import com.yijia.yy.service.dto.OvertimeWorkDTO;
import com.yijia.yy.web.util.QueryDslUtil;
import org.apache.commons.lang3.BooleanUtils;
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
 * REST controller for managing OvertimeWork.
 */
@RestController
@RequestMapping("/api")
public class OvertimeWorkResource {

    private final Logger log = LoggerFactory.getLogger(OvertimeWorkResource.class);

    @Inject
    private OvertimeWorkService overtimeWorkService;

    @Inject
    private UserService userService;

    /**
     * POST  /overtime-works : Create a new overtimeWork.
     *
     * @param overtimeWorkDTO the overtimeWorkDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new overtimeWorkDTO, or with status 400 (Bad Request) if the overtimeWork has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/overtime-works",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OvertimeWorkDTO> createOvertimeWork(@Valid @RequestBody OvertimeWorkDTO overtimeWorkDTO) throws URISyntaxException {
        log.debug("REST request to save OvertimeWork : {}", overtimeWorkDTO);
        if (overtimeWorkDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("overtimeWork", "idexists", "A new overtimeWork cannot already have an ID")).body(null);
        }
        OvertimeWorkDTO result = overtimeWorkService.save(overtimeWorkDTO);
        return ResponseEntity.created(new URI("/api/overtime-works/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("overtimeWork", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /overtime-works : Updates an existing overtimeWork.
     *
     * @param overtimeWorkDTO the overtimeWorkDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated overtimeWorkDTO,
     * or with status 400 (Bad Request) if the overtimeWorkDTO is not valid,
     * or with status 500 (Internal Server Error) if the overtimeWorkDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/overtime-works",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OvertimeWorkDTO> updateOvertimeWork(@Valid @RequestBody OvertimeWorkDTO overtimeWorkDTO) throws URISyntaxException {
        log.debug("REST request to update OvertimeWork : {}", overtimeWorkDTO);
        if (overtimeWorkDTO.getId() == null) {
            return createOvertimeWork(overtimeWorkDTO);
        }
        OvertimeWorkDTO result = overtimeWorkService.save(overtimeWorkDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("overtimeWork", overtimeWorkDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /overtime-works : get all the overtimeWorks.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of overtimeWorks in body
     */
    @RequestMapping(value = "/overtime-works",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<OvertimeWorkDTO> getAllOvertimeWorks(OvertimeWorkSearchDTO searchDTO, Sort sort) {
        log.debug("REST request to get all OvertimeWorks");
        if (BooleanUtils.toBoolean(searchDTO.getShownOwnedOnly())) {
            searchDTO.setOwnerId(userService.currentLoginEmployee().getId());
        }

        Predicate predicate = QueryDslUtil.OvertimeWorkSearchDTO2Predicate(searchDTO);

        return predicate == null ? overtimeWorkService.findAll(sort) : overtimeWorkService.findAll(predicate, sort);
    }

    /**
     * GET  /overtime-works/:id : get the "id" overtimeWork.
     *
     * @param id the id of the overtimeWorkDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the overtimeWorkDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/overtime-works/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OvertimeWorkDTO> getOvertimeWork(@PathVariable Long id) {
        log.debug("REST request to get OvertimeWork : {}", id);
        OvertimeWorkDTO overtimeWorkDTO = overtimeWorkService.findOne(id);
        return Optional.ofNullable(overtimeWorkDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /overtime-works/:id : delete the "id" overtimeWork.
     *
     * @param id the id of the overtimeWorkDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/overtime-works/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOvertimeWork(@PathVariable Long id) {
        log.debug("REST request to delete OvertimeWork : {}", id);
        overtimeWorkService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("overtimeWork", id.toString())).build();
    }

}
