package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yijia.yy.service.ExpenseItemService;
import com.yijia.yy.web.rest.util.HeaderUtil;
import com.yijia.yy.service.dto.ExpenseItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing ExpenseItem.
 */
@RestController
@RequestMapping("/api")
public class ExpenseItemResource {

    private final Logger log = LoggerFactory.getLogger(ExpenseItemResource.class);
        
    @Inject
    private ExpenseItemService expenseItemService;

    /**
     * POST  /expense-items : Create a new expenseItem.
     *
     * @param expenseItemDTO the expenseItemDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new expenseItemDTO, or with status 400 (Bad Request) if the expenseItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/expense-items",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExpenseItemDTO> createExpenseItem(@RequestBody ExpenseItemDTO expenseItemDTO) throws URISyntaxException {
        log.debug("REST request to save ExpenseItem : {}", expenseItemDTO);
        if (expenseItemDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("expenseItem", "idexists", "A new expenseItem cannot already have an ID")).body(null);
        }
        ExpenseItemDTO result = expenseItemService.save(expenseItemDTO);
        return ResponseEntity.created(new URI("/api/expense-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("expenseItem", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /expense-items : Updates an existing expenseItem.
     *
     * @param expenseItemDTO the expenseItemDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated expenseItemDTO,
     * or with status 400 (Bad Request) if the expenseItemDTO is not valid,
     * or with status 500 (Internal Server Error) if the expenseItemDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/expense-items",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExpenseItemDTO> updateExpenseItem(@RequestBody ExpenseItemDTO expenseItemDTO) throws URISyntaxException {
        log.debug("REST request to update ExpenseItem : {}", expenseItemDTO);
        if (expenseItemDTO.getId() == null) {
            return createExpenseItem(expenseItemDTO);
        }
        ExpenseItemDTO result = expenseItemService.save(expenseItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("expenseItem", expenseItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /expense-items : get all the expenseItems.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of expenseItems in body
     */
    @RequestMapping(value = "/expense-items",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ExpenseItemDTO> getAllExpenseItems() {
        log.debug("REST request to get all ExpenseItems");
        return expenseItemService.findAll();
    }

    /**
     * GET  /expense-items/:id : get the "id" expenseItem.
     *
     * @param id the id of the expenseItemDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the expenseItemDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/expense-items/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExpenseItemDTO> getExpenseItem(@PathVariable Long id) {
        log.debug("REST request to get ExpenseItem : {}", id);
        ExpenseItemDTO expenseItemDTO = expenseItemService.findOne(id);
        return Optional.ofNullable(expenseItemDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /expense-items/:id : delete the "id" expenseItem.
     *
     * @param id the id of the expenseItemDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/expense-items/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteExpenseItem(@PathVariable Long id) {
        log.debug("REST request to delete ExpenseItem : {}", id);
        expenseItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("expenseItem", id.toString())).build();
    }

}
