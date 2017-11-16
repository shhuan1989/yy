package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.querydsl.core.types.Predicate;
import com.yijia.yy.domain.PerformanceApprovalRequest;
import com.yijia.yy.service.PerformanceApprovalRequestService;
import com.yijia.yy.service.dto.ApprovalRequestDTO;
import com.yijia.yy.service.dto.PerformanceApprovalRequestDTO;
import com.yijia.yy.service.dto.PerformanceApprovalRequestSearchDTO;
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
 * REST controller for managing ApprovalRequest.
 */
@RestController
@RequestMapping("/api")
public class PerformanceApprovalRequestResource {

    private final Logger log = LoggerFactory.getLogger(ApprovalRequestResource.class);

    @Inject
    private PerformanceApprovalRequestService approvalRequestService;

    /**
     * POST  /performance-approval-requests : Create a new approvalRequest.
     *
     * @param approvalRequestDTO the approvalRequestDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new approvalRequestDTO, or with status 400 (Bad Request) if the approvalRequest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/performance-approval-requests",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PerformanceApprovalRequestDTO> createApprovalRequest(@RequestBody PerformanceApprovalRequestDTO approvalRequestDTO) throws URISyntaxException {
        log.debug("REST request to save ApprovalRequest : {}", approvalRequestDTO);
        if (approvalRequestDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("approvalRequest", "idexists", "A new approvalRequest cannot already have an ID")).body(null);
        }
        PerformanceApprovalRequestDTO result = approvalRequestService.save(approvalRequestDTO);
        return ResponseEntity.created(new URI("/api/performance-approval-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("approvalRequest", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /performance-approval-requests : Updates an existing approvalRequest.
     *
     * @param approvalRequestDTO the approvalRequestDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated approvalRequestDTO,
     * or with status 400 (Bad Request) if the approvalRequestDTO is not valid,
     * or with status 500 (Internal Server Error) if the approvalRequestDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/performance-approval-requests",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PerformanceApprovalRequestDTO> updateApprovalRequest(@RequestBody PerformanceApprovalRequestDTO approvalRequestDTO) throws URISyntaxException {
        log.debug("REST request to update ApprovalRequest : {}", approvalRequestDTO);
        if (approvalRequestDTO.getId() == null) {
            return createApprovalRequest(approvalRequestDTO);
        }
        PerformanceApprovalRequestDTO result = approvalRequestService.save(approvalRequestDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("approvalRequest", approvalRequestDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /performance-approval-requests : get all the performanceApprovalRequests.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of performanceApprovalRequests in body
     */
    @RequestMapping(value = "/performance-approval-requests",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PerformanceApprovalRequestDTO> getAllPerformanceApprovalRequests(PerformanceApprovalRequestSearchDTO searchDTO, Sort sort) {
        log.debug("REST request to get all PerformanceApprovalRequests");
        Predicate predicate = QueryDslUtil.PerformanceApprovalRequestSearchDTO2Predicate(searchDTO);
        return predicate == null ? approvalRequestService.findAll(sort) : approvalRequestService.findAll(predicate, sort);
    }

    /**
     * GET  /performance-approval-requests/:id : get the "id" approvalRequest.
     *
     * @param id the id of the approvalRequestDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the approvalRequestDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/performance-approval-requests/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PerformanceApprovalRequestDTO> getApprovalRequest(@PathVariable Long id) {
        log.debug("REST request to get ApprovalRequest : {}", id);
        PerformanceApprovalRequestDTO approvalRequestDTO = approvalRequestService.findOne(id);
        return Optional.ofNullable(approvalRequestDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/performance-approval-requests/{id}/start",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PerformanceApprovalRequestDTO> startApprovalRequest(@PathVariable Long id) {
        log.debug("REST request to start ApprovalRequest : {}", id);
        PerformanceApprovalRequestDTO approvalRequestDTO = approvalRequestService.start(id);
        return Optional.ofNullable(approvalRequestDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /performance-approval-requests/:id : delete the "id" approvalRequest.
     *
     * @param id the id of the approvalRequestDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/performance-approval-requests/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteApprovalRequest(@PathVariable Long id) {
        log.debug("REST request to delete ApprovalRequest : {}", id);
        approvalRequestService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("approvalRequest", id.toString())).build();
    }

    @RequestMapping(value = "/performance-approval-requests/{id}/issue",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> issueRequests(@PathVariable Long id) {
        log.debug("REST request to delete ApprovalRequest : {}", id);
        approvalRequestService.issue(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("approvalRequest", id.toString())).build();
    }

}
