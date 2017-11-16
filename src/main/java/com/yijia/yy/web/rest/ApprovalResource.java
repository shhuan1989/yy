package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yijia.yy.exception.OutOfRepoException;
import com.yijia.yy.service.ApprovalService;
import com.yijia.yy.web.rest.util.HeaderUtil;
import com.yijia.yy.service.dto.ApprovalDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Approval.
 */
@RestController
@RequestMapping("/api")
public class    ApprovalResource {

    private final Logger log = LoggerFactory.getLogger(ApprovalResource.class);

    @Inject
    private ApprovalService approvalService;

    /**
     * POST  /approvals : Create a new approval.
     *
     * @param approvalDTO the approvalDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new approvalDTO, or with status 400 (Bad Request) if the approval has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/approvals",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ApprovalDTO> createApproval(@Valid @RequestBody ApprovalDTO approvalDTO) throws URISyntaxException {
        log.debug("REST request to save Approval : {}", approvalDTO);
        if (approvalDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("approval", "idexists", "A new approval cannot already have an ID")).body(null);
        }
        ApprovalDTO result = approvalService.save(approvalDTO);
        return ResponseEntity.created(new URI("/api/approvals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("approval", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /approvals : Updates an existing approval.
     *
     * @param approvalDTO the approvalDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated approvalDTO,
     * or with status 400 (Bad Request) if the approvalDTO is not valid,
     * or with status 500 (Internal Server Error) if the approvalDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/approvals",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ApprovalDTO> updateApproval(@Valid @RequestBody ApprovalDTO approvalDTO) throws URISyntaxException {
        log.debug("REST request to update Approval : {}", approvalDTO);
        if (approvalDTO.getId() == null) {
            return createApproval(approvalDTO);
        }
        ApprovalDTO result = approvalService.save(approvalDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("approval", approvalDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /approvals : get all the approvals.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of approvals in body
     */
    @RequestMapping(value = "/approvals",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ApprovalDTO> getAllApprovals() {
        log.debug("REST request to get all Approvals");
        return approvalService.findAll();
    }

    /**
     * GET  /approvals/:id : get the "id" approval.
     *
     * @param id the id of the approvalDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the approvalDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/approvals/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ApprovalDTO> getApproval(@PathVariable Long id) {
        log.debug("REST request to get Approval : {}", id);
        ApprovalDTO approvalDTO = approvalService.findOne(id);
        return Optional.ofNullable(approvalDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * POST /approvals/:id : approve the "id" approval.
     *
     * @param id the id of the approvalDTO to approve
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/approvals/{id}/approve",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ApprovalDTO> approval(@PathVariable Long id) {
        log.debug("REST request to get Approval : {}", id);
        ApprovalDTO approvalDTO = null;
        try {
            approvalDTO = approvalService.approval(id);
        } catch (OutOfRepoException e) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("approval", "outofstock", id.toString()))
                .body(null);
        }
        return Optional.ofNullable(approvalDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * POST /approvals/:id : reject the "id" approval.
     *
     * @param id the id of the approvalDTO to reject
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/approvals/{id}/reject",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ApprovalDTO> reject(@PathVariable Long id, @RequestBody String reason) {
        log.debug("REST request to get Approval : {}", id);
        ApprovalDTO approvalDTO = approvalService.reject(id, reason);
        return Optional.ofNullable(approvalDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /approvals/:id : delete the "id" approval.
     *
     * @param id the id of the approvalDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/approvals/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteApproval(@PathVariable Long id) {
        log.debug("REST request to delete Approval : {}", id);
        approvalService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("approval", id.toString())).build();
    }

}
