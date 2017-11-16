package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.querydsl.core.types.Predicate;
import com.yijia.yy.domain.PerformanceBonus;
import com.yijia.yy.service.PerformanceBonusService;
import com.yijia.yy.service.UserService;
import com.yijia.yy.service.dto.PerformanceBonusSearchDTO;
import com.yijia.yy.web.rest.util.HeaderUtil;
import com.yijia.yy.service.dto.PerformanceBonusDTO;
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
 * REST controller for managing PerformanceBonus.
 */
@RestController
@RequestMapping("/api")
public class PerformanceBonusResource {

    private final Logger log = LoggerFactory.getLogger(PerformanceBonusResource.class);

    @Inject
    private PerformanceBonusService performanceBonusService;

    @Inject
    private UserService userService;

    /**
     * POST  /performance-bonuses : Create a new performanceBonus.
     *
     * @param performanceBonusDTO the performanceBonusDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new performanceBonusDTO, or with status 400 (Bad Request) if the performanceBonus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/performance-bonuses",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PerformanceBonusDTO> createPerformanceBonus(@Valid @RequestBody PerformanceBonusDTO performanceBonusDTO) throws URISyntaxException {
        log.debug("REST request to save PerformanceBonus : {}", performanceBonusDTO);
        if (performanceBonusDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("performanceBonus", "idexists", "A new performanceBonus cannot already have an ID")).body(null);
        }
        PerformanceBonusDTO result = performanceBonusService.save(performanceBonusDTO);
        return ResponseEntity.created(new URI("/api/performance-bonuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("performanceBonus", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /performance-bonuses : Updates an existing performanceBonus.
     *
     * @param performanceBonusDTO the performanceBonusDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated performanceBonusDTO,
     * or with status 400 (Bad Request) if the performanceBonusDTO is not valid,
     * or with status 500 (Internal Server Error) if the performanceBonusDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/performance-bonuses",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PerformanceBonusDTO> updatePerformanceBonus(@Valid @RequestBody PerformanceBonusDTO performanceBonusDTO) throws URISyntaxException {
        log.debug("REST request to update PerformanceBonus : {}", performanceBonusDTO);
        if (performanceBonusDTO.getId() == null) {
            return createPerformanceBonus(performanceBonusDTO);
        }
        PerformanceBonusDTO result = performanceBonusService.save(performanceBonusDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("performanceBonus", performanceBonusDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /performance-bonuses : get all the performanceBonuses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of performanceBonuses in body
     */
    @RequestMapping(value = "/performance-bonuses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PerformanceBonusDTO> getAllPerformanceBonuses(PerformanceBonusSearchDTO searchDTO, Sort sort) {
        log.debug("REST request to get all PerformanceBonuses");
        if (BooleanUtils.toBoolean(searchDTO.getShownOwnedOnly())) {
            searchDTO.setOwnerId(userService.currentLoginEmployee().getId());
        }
        Predicate predicate = QueryDslUtil.PerformanceBonusSearchDTO2Predicate(searchDTO);
        return predicate == null ? performanceBonusService.findAll(sort) : performanceBonusService.findAll(predicate, sort);
    }

    /**
     * GET  /performance-bonuses/:id : get the "id" performanceBonus.
     *
     * @param id the id of the performanceBonusDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the performanceBonusDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/performance-bonuses/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PerformanceBonusDTO> getPerformanceBonus(@PathVariable Long id) {
        log.debug("REST request to get PerformanceBonus : {}", id);
        PerformanceBonusDTO performanceBonusDTO = performanceBonusService.findOne(id);
        return Optional.ofNullable(performanceBonusDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /performance-bonuses/:id : delete the "id" performanceBonus.
     *
     * @param id the id of the performanceBonusDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/performance-bonuses/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePerformanceBonus(@PathVariable Long id) {
        log.debug("REST request to delete PerformanceBonus : {}", id);
        performanceBonusService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("performanceBonus", id.toString())).build();
    }

}
