package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.IncomeService;
import com.yijia.yy.service.dto.IncomeSearchDTO;
import com.yijia.yy.web.rest.util.HeaderUtil;
import com.yijia.yy.service.dto.IncomeDTO;
import com.yijia.yy.web.util.QueryDslUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Income.
 */
@RestController
@RequestMapping("/api")
public class IncomeResource {

    private final Logger log = LoggerFactory.getLogger(IncomeResource.class);

    @Inject
    private IncomeService incomeService;

    /**
     * POST  /incomes : Create a new income.
     *
     * @param incomeDTO the incomeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new incomeDTO, or with status 400 (Bad Request) if the income has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/incomes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<IncomeDTO> createIncome(@RequestBody IncomeDTO incomeDTO) throws URISyntaxException {
        log.debug("REST request to save Income : {}", incomeDTO);
        if (incomeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("income", "idexists", "A new income cannot already have an ID")).body(null);
        }
        IncomeDTO result = incomeService.save(incomeDTO);
        return ResponseEntity.created(new URI("/api/incomes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("income", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /incomes : Updates an existing income.
     *
     * @param incomeDTO the incomeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated incomeDTO,
     * or with status 400 (Bad Request) if the incomeDTO is not valid,
     * or with status 500 (Internal Server Error) if the incomeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/incomes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<IncomeDTO> updateIncome(@RequestBody IncomeDTO incomeDTO) throws URISyntaxException {
        log.debug("REST request to update Income : {}", incomeDTO);
        if (incomeDTO.getId() == null) {
            return createIncome(incomeDTO);
        }
        IncomeDTO result = incomeService.save(incomeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("income", incomeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /incomes : get all the incomes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of incomes in body
     */
    @RequestMapping(value = "/incomes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<IncomeDTO> getAllIncomes(IncomeSearchDTO searchDTO, Sort sort) {
        log.debug("REST request to get all Incomes");

        if (searchDTO.getIdNumber() != null
            || StringUtils.isNotBlank(searchDTO.getClientName())
            || StringUtils.isNotBlank(searchDTO.getProjectIdNumber())
            || StringUtils.isNotBlank(searchDTO.getProjectName())) {
            return new ArrayList<>();
        }

        Predicate predicate = QueryDslUtil.IncomeSearchDTO2Predicate(searchDTO);
        return predicate == null ? incomeService.findAll(sort) : incomeService.findAll(predicate, sort);
    }

    /**
     * GET  /incomes/:id : get the "id" income.
     *
     * @param id the id of the incomeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the incomeDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/incomes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<IncomeDTO> getIncome(@PathVariable Long id) {
        log.debug("REST request to get Income : {}", id);
        IncomeDTO incomeDTO = incomeService.findOne(id);
        return Optional.ofNullable(incomeDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /incomes/:id : delete the "id" income.
     *
     * @param id the id of the incomeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/incomes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteIncome(@PathVariable Long id) {
        log.debug("REST request to delete Income : {}", id);
        incomeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("income", id.toString())).build();
    }

}
