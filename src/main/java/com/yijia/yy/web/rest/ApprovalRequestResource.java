package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yijia.yy.service.ApprovalRequestService;
import com.yijia.yy.web.rest.util.HeaderUtil;
import com.yijia.yy.service.dto.ApprovalRequestDTO;
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
 * REST controller for managing ApprovalRequest.
 */
@RestController
@RequestMapping("/api")
public class ApprovalRequestResource {

    private final Logger log = LoggerFactory.getLogger(ApprovalRequestResource.class);

    @Inject
    private ApprovalRequestService approvalRequestService;

    /**
     * POST  /approval-requests : Create a new approvalRequest.
     *
     * @param approvalRequestDTO the approvalRequestDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new approvalRequestDTO, or with status 400 (Bad Request) if the approvalRequest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/approval-requests",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ApprovalRequestDTO> createApprovalRequest(@Valid @RequestBody ApprovalRequestDTO approvalRequestDTO) throws URISyntaxException {
        log.debug("REST request to save ApprovalRequest : {}", approvalRequestDTO);
        if (approvalRequestDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("approvalRequest", "idexists", "A new approvalRequest cannot already have an ID")).body(null);
        }
        ApprovalRequestDTO result = approvalRequestService.save(approvalRequestDTO);
        return ResponseEntity.created(new URI("/api/approval-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("approvalRequest", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /approval-requests : Updates an existing approvalRequest.
     *
     * @param approvalRequestDTO the approvalRequestDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated approvalRequestDTO,
     * or with status 400 (Bad Request) if the approvalRequestDTO is not valid,
     * or with status 500 (Internal Server Error) if the approvalRequestDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/approval-requests",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ApprovalRequestDTO> updateApprovalRequest(@Valid @RequestBody ApprovalRequestDTO approvalRequestDTO) throws URISyntaxException {
        log.debug("REST request to update ApprovalRequest : {}", approvalRequestDTO);
        if (approvalRequestDTO.getId() == null) {
            return createApprovalRequest(approvalRequestDTO);
        }
        ApprovalRequestDTO result = approvalRequestService.save(approvalRequestDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("approvalRequest", approvalRequestDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /approval-requests : get all the approvalRequests.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of approvalRequests in body
     */
    @RequestMapping(value = "/approval-requests",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ApprovalRequestDTO> getAllApprovalRequests() {
        log.debug("REST request to get all ApprovalRequests");
        return approvalRequestService.findAll();
    }

    /**
     * GET  /approval-requests/:id : get the "id" approvalRequest.
     *
     * @param id the id of the approvalRequestDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the approvalRequestDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/approval-requests/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ApprovalRequestDTO> getApprovalRequest(@PathVariable Long id) {
        log.debug("REST request to get ApprovalRequest : {}", id);
        ApprovalRequestDTO approvalRequestDTO = approvalRequestService.findOne(id);
        return Optional.ofNullable(approvalRequestDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /approval-requests/:id : delete the "id" approvalRequest.
     *
     * @param id the id of the approvalRequestDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/approval-requests/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteApprovalRequest(@PathVariable Long id) {
        log.debug("REST request to delete ApprovalRequest : {}", id);
        approvalRequestService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("approvalRequest", id.toString())).build();
    }


    /**
     * POST  /approval-requests/:id : start the "id" approvalRequest.
     *
     * @param id the id of the approvalRequestDTO to start
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/approval-requests/{id}/start",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> startApprovalRequest(@PathVariable Long id) {
        log.debug("REST request to delete ApprovalRequest : {}", id);
        approvalRequestService.start(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("approvalRequest", id.toString())).build();
    }
}
