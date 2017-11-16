package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.querydsl.core.types.Predicate;
import com.yijia.yy.domain.QContractInvoice;
import com.yijia.yy.service.ContractInvoiceService;
import com.yijia.yy.service.ContractService;
import com.yijia.yy.service.dto.ContractDTO;
import com.yijia.yy.service.dto.ContractInvoiceSearchDTO;
import com.yijia.yy.service.dto.InvoiceStatisticsDTO;
import com.yijia.yy.web.rest.util.HeaderUtil;
import com.yijia.yy.service.dto.ContractInvoiceDTO;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing ContractInvoice.
 */
@RestController
@RequestMapping("/api")
public class ContractInvoiceResource {

    private final Logger log = LoggerFactory.getLogger(ContractInvoiceResource.class);

    @Inject
    private ContractInvoiceService contractInvoiceService;

    @Inject
    private ContractService contractService;

    /**
     * POST  /contract-invoices : Create a new contractInvoice.
     *
     * @param contractInvoiceDTO the contractInvoiceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contractInvoiceDTO, or with status 400 (Bad Request) if the contractInvoice has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/contract-invoices",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ContractInvoiceDTO> createContractInvoice(@Valid @RequestBody ContractInvoiceDTO contractInvoiceDTO) throws URISyntaxException {
        log.debug("REST request to save ContractInvoice : {}", contractInvoiceDTO);
        if (contractInvoiceDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("contractInvoice", "idexists", "A new contractInvoice cannot already have an ID")).body(null);
        }
        try {
            ContractInvoiceDTO result = contractInvoiceService.save(contractInvoiceDTO);
            return ResponseEntity.created(new URI("/api/contract-invoices/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("contractInvoice", result.getId().toString()))
                .body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("contract", "invoice", e.getMessage())).body(null);
        }
    }

    /**
     * PUT  /contract-invoices : Updates an existing contractInvoice.
     *
     * @param contractInvoiceDTO the contractInvoiceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contractInvoiceDTO,
     * or with status 400 (Bad Request) if the contractInvoiceDTO is not valid,
     * or with status 500 (Internal Server Error) if the contractInvoiceDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/contract-invoices",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ContractInvoiceDTO> updateContractInvoice(@Valid @RequestBody ContractInvoiceDTO contractInvoiceDTO) throws URISyntaxException {
        log.debug("REST request to update ContractInvoice : {}", contractInvoiceDTO);
        if (contractInvoiceDTO.getId() == null) {
            return createContractInvoice(contractInvoiceDTO);
        }
        ContractInvoiceDTO result = contractInvoiceService.save(contractInvoiceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("contractInvoice", contractInvoiceDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contract-invoices : get all the contractInvoices.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of contractInvoices in body
     */
    @RequestMapping(value = "/contract-invoices",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ContractInvoiceDTO> getAllContractInvoices(ContractInvoiceSearchDTO searchDTO, Sort sort) {
        log.debug("REST request to get all ContractInvoices");

        Predicate predicate = searchDTO.getContractId() == null ? null : QContractInvoice.contractInvoice.contract.id.eq(searchDTO.getContractId());
        return predicate == null ? contractInvoiceService.findAll(sort) : contractInvoiceService.findAll(predicate, sort);
    }

    /**
     * GET  /invoice-statistics : get statistics info of invoices
     *
     * @return the ResponseEntity with status 200 (OK) and the invoice statistics in body
     */
    @RequestMapping(value = "/invoice-statistics",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InvoiceStatisticsDTO> getInvoiceStatistics() {
        log.debug("REST request to get invoice statistics");

        List<ContractInvoiceDTO> invoices = contractInvoiceService.findAll(null);
        List<ContractDTO> contracts = contractService.findAll(null);

        Double totalAmount =
            contracts.stream().mapToDouble(c -> c.getMoneyAmount()).sum();

        Double invoiceAmount =
            invoices.stream().mapToDouble(i -> i.getAmount()).sum();

        return new ResponseEntity<>(new InvoiceStatisticsDTO(totalAmount, invoiceAmount), HttpStatus.OK);
    }

    /**
     * GET  /contract-invoices/:id : get the "id" contractInvoice.
     *
     * @param id the id of the contractInvoiceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contractInvoiceDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/contract-invoices/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ContractInvoiceDTO> getContractInvoice(@PathVariable Long id) {
        log.debug("REST request to get ContractInvoice : {}", id);
        ContractInvoiceDTO contractInvoiceDTO = contractInvoiceService.findOne(id);
        return Optional.ofNullable(contractInvoiceDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /contract-invoices/:id : delete the "id" contractInvoice.
     *
     * @param id the id of the contractInvoiceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/contract-invoices/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteContractInvoice(@PathVariable Long id) {
        log.debug("REST request to delete ContractInvoice : {}", id);
        contractInvoiceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("contractInvoice", id.toString())).build();
    }

}
