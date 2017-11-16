package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.querydsl.core.types.Predicate;
import com.yijia.yy.domain.Employee;
import com.yijia.yy.domain.User;
import com.yijia.yy.security.AuthoritiesConstants;
import com.yijia.yy.service.ContractService;
import com.yijia.yy.service.EmployeeService;
import com.yijia.yy.service.UserService;
import com.yijia.yy.service.dto.*;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for managing Contract.
 */
@RestController
@RequestMapping("/api")
public class ContractResource {

    private final Logger log = LoggerFactory.getLogger(ContractResource.class);

    @Inject
    private ContractService contractService;

    @Inject
    private UserService userService;

    @Inject
    private EmployeeService employeeService;

    /**
     * POST  /contracts : Create a new contract.
     *
     * @param contractDTO the contractDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contractDTO, or with status 400 (Bad Request) if the contract has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/contracts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ContractDTO> createContract(@RequestBody ContractDTO contractDTO) throws URISyntaxException {
        log.debug("REST request to save Contract : {}", contractDTO);
        if (contractDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("contract", "idexists", "A new contract cannot already have an ID")).body(null);
        }

        ContractDTO result = null;
        try {
            result = contractService.save(contractDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("contract", "idNumberExists", e.getMessage())).body(null);
        }
        return ResponseEntity.created(new URI("/api/contracts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("contract", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contracts : Updates an existing contract.
     *
     * @param contractDTO the contractDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contractDTO,
     * or with status 400 (Bad Request) if the contractDTO is not valid,
     * or with status 500 (Internal Server Error) if the contractDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/contracts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ContractDTO> updateContract(@RequestBody ContractDTO contractDTO) throws URISyntaxException {
        log.debug("REST request to update Contract : {}", contractDTO);
        if (contractDTO.getId() == null) {
            return createContract(contractDTO);
        }
        ContractDTO result = contractService.save(contractDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("contract", contractDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contracts : get all the contracts.
     *
     * @param sort the order
     * @return the ResponseEntity with status 200 (OK) and the list of contracts in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/contracts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ContractDTO>> getAllContracts(Sort sort, ContractSearchDTO searchDTO)
        throws URISyntaxException {
        log.debug("REST request to get a page of Contracts");

        User user = userService.currentLoginUser().get();
        if (user == null || user.getEmployee() == null) {
            return ResponseEntity.ok(new ArrayList<>());
        }

        Predicate predicate = QueryDslUtil.ContractSearchDTO2Predicate(searchDTO);
        List<ContractDTO> result = predicate == null ? contractService.findAll(sort) : contractService.findAll(predicate, sort);

        boolean showOwnedOnly = !user.hasAuthority(AuthoritiesConstants.VIEW_CONTRACT_MANAGE_ALL)
            && !user.hasAuthority(AuthoritiesConstants.ADMIN);

        if (showOwnedOnly) {
            Employee employee = userService.currentLoginEmployee();
            if (employee == null) {
                return ResponseEntity.ok(new ArrayList<>());
            }

            List<Employee> employees = employeeService.subordinates(employee.getId());
            employees.add(employee);

            result = result.stream()
                .filter(c ->
                    c.getCreatorId() != null
                    && employees.stream()
                        .filter(e -> e.getId().equals(c.getCreatorId()))
                        .findAny()
                        .isPresent()

                )
                .collect(Collectors.toList());
        }

        return ResponseEntity.ok(result);
    }

    /**
     * GET  /contracts/:id : get the "id" contract.
     *
     * @param id the id of the contractDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contractDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/contracts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ContractDTO> getContract(@PathVariable Long id) {
        log.debug("REST request to get Contract : {}", id);
        ContractDTO contractDTO = contractService.findOne(id);
        return Optional.ofNullable(contractDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /contract-payment : get payment statistics
     *
     * @param timeFrom the start time
     * @param timeTo the end time
     * @return the ResponseEntity with status 200 (OK) and with body the statistics, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/contract-payment",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PaymentStatisticDTO> getPaymentStatistics(Long timeFrom, Long timeTo) {
        log.debug("REST request to get payment : {} - {}", timeFrom, timeTo);

        final Long t0 = timeFrom == null ? 0L : timeFrom;
        final Long t1 = timeTo == null ? Long.MAX_VALUE : timeTo;
        List<ContractDTO> contracts = contractService.findAll(null);

        Double totalAmount =
        contracts.stream().mapToDouble(c -> {
            List<ContractInstallmentDTO> installments = c.getInstallments();
            return installments.stream()
                .filter(i -> i.getEta() > t0 && i.getEta() < t1)
                .mapToDouble(i -> i.getAmount() == null ? 0 : i.getAmount())
                .sum();
        }).sum();

        Double paidAmount =
            contracts.stream().mapToDouble(c -> {
                List<ContractInstallmentDTO> installments = c.getInstallments();
                return installments.stream()
                    .filter(i -> i.getEta() > t0 && i.getEta() < t1)
                    .mapToDouble(i -> i.getActualAmount() == null ? 0 : i.getActualAmount())
                    .sum();
            }).sum();

        return new ResponseEntity<>(new PaymentStatisticDTO(paidAmount, totalAmount), HttpStatus.OK);
    }

    /**
     * DELETE  /contracts/:id : delete the "id" contract.
     *
     * @param id the id of the contractDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/contracts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteContract(@PathVariable Long id) {
        log.debug("REST request to delete Contract : {}", id);
        contractService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("contract", id.toString())).build();
    }


    /**
     * POST  /contracts/:id : get the "id" contract.
     *
     * @param id the id of the contractDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contractDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/contracts/{id}/pay_next_installment",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ContractDTO> payNextInstallment(@PathVariable Long id, @RequestBody ContractPaymentDTO paymentDTO) {
        log.debug("REST request to get Contract : {}", id);
        if (paymentDTO == null && paymentDTO.getAmount() == null || paymentDTO.getPayTime() == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("contract", "payment", "amount and payTime can't be null")).body(null);
        }
        try {
            return ResponseEntity.ok().body(
                contractService.payNextInstallment(id, paymentDTO.getAmount(), paymentDTO.getPayTime())
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("contract", "payment", e.getMessage())).body(null);
        }
    }

}
