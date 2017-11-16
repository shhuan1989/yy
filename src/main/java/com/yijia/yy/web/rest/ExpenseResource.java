package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.ExpenseService;
import com.yijia.yy.service.dto.ExpenseDTO;
import com.yijia.yy.service.dto.ExpenseSearchDTO;
import com.yijia.yy.web.rest.util.HeaderUtil;
import com.yijia.yy.web.util.QueryDslUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Expense.
 */
@RestController
@RequestMapping("/api")
public class ExpenseResource {

    private final Logger log = LoggerFactory.getLogger(ExpenseResource.class);

    @Inject
    private ExpenseService expenseService;

    /**
     * POST  /expenses : Create a new expense.
     *
     * @param expenseDTO the expenseDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new expenseDTO, or with status 400 (Bad Request) if the expense has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/expenses",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExpenseDTO> createExpense(@RequestBody ExpenseDTO expenseDTO) throws URISyntaxException {
        log.debug("REST request to save Expense : {}", expenseDTO);
        if (expenseDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("expense", "idexists", "A new expense cannot already have an ID")).body(null);
        }
        ExpenseDTO result = expenseService.save(expenseDTO);
        return ResponseEntity.created(new URI("/api/expenses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("expense", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /expenses : Updates an existing expense.
     *
     * @param expenseDTO the expenseDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated expenseDTO,
     * or with status 400 (Bad Request) if the expenseDTO is not valid,
     * or with status 500 (Internal Server Error) if the expenseDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/expenses",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExpenseDTO> updateExpense(@RequestBody ExpenseDTO expenseDTO) throws URISyntaxException {
        log.debug("REST request to update Expense : {}", expenseDTO);
        if (expenseDTO.getId() == null) {
            return createExpense(expenseDTO);
        }
        ExpenseDTO result = expenseService.save(expenseDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("expense", expenseDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /expenses : get all the expenses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of expenses in body
     */
    @RequestMapping(value = "/expenses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ExpenseDTO> getAllExpenses(ExpenseSearchDTO searchDTO, Sort sort) {
        log.debug("REST request to get all Expenses");

        Predicate predicate = QueryDslUtil.ExpenseSearchDTO2Predicate(searchDTO);

        return predicate == null ? expenseService.findAll(sort) : expenseService.findAll(predicate, sort);
    }

    /**
     * GET  /expenses/:id : get the "id" expense.
     *
     * @param id the id of the expenseDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the expenseDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/expenses/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExpenseDTO> getExpense(@PathVariable Long id) {
        log.debug("REST request to get Expense : {}", id);
        ExpenseDTO expenseDTO = expenseService.findOne(id);
        return Optional.ofNullable(expenseDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /expenses/:id : delete the "id" expense.
     *
     * @param id the id of the expenseDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/expenses/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        log.debug("REST request to delete Expense : {}", id);
        expenseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("expense", id.toString())).build();
    }

}
