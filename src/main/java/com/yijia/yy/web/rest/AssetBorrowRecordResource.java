package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.AssetBorrowRecordService;
import com.yijia.yy.service.UserService;
import com.yijia.yy.service.dto.AssetBorrowRecordDTO;
import com.yijia.yy.service.dto.AssetBorrowRecordSearchDTO;
import com.yijia.yy.web.rest.util.HeaderUtil;
import com.yijia.yy.web.util.QueryDslUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing AssetBorrowRecord.
 */
@RestController
@RequestMapping("/api")
public class AssetBorrowRecordResource {

    private final Logger log = LoggerFactory.getLogger(AssetBorrowRecordResource.class);

    @Inject
    private AssetBorrowRecordService assetBorrowRecordService;

    @Inject
    private UserService userService;

    /**
     * POST  /asset-borrow-records : Create a new assetBorrowRecord.
     *
     * @param assetBorrowRecordDTO the assetBorrowRecordDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new assetBorrowRecordDTO, or with status 400 (Bad Request) if the assetBorrowRecord has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/asset-borrow-records",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AssetBorrowRecordDTO> createAssetBorrowRecord(@Valid @RequestBody AssetBorrowRecordDTO assetBorrowRecordDTO) throws URISyntaxException {
        log.debug("REST request to save AssetBorrowRecord : {}", assetBorrowRecordDTO);
        if (assetBorrowRecordDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("assetBorrowRecord", "idexists", "A new assetBorrowRecord cannot already have an ID")).body(null);
        }
        AssetBorrowRecordDTO result = assetBorrowRecordService.save(assetBorrowRecordDTO);
        return ResponseEntity.created(new URI("/api/asset-borrow-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("assetBorrowRecord", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /asset-borrow-records : Updates an existing assetBorrowRecord.
     *
     * @param assetBorrowRecordDTO the assetBorrowRecordDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated assetBorrowRecordDTO,
     * or with status 400 (Bad Request) if the assetBorrowRecordDTO is not valid,
     * or with status 500 (Internal Server Error) if the assetBorrowRecordDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/asset-borrow-records",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AssetBorrowRecordDTO> updateAssetBorrowRecord(@Valid @RequestBody AssetBorrowRecordDTO assetBorrowRecordDTO) throws URISyntaxException {
        log.debug("REST request to update AssetBorrowRecord : {}", assetBorrowRecordDTO);
        if (assetBorrowRecordDTO.getId() == null) {
            return createAssetBorrowRecord(assetBorrowRecordDTO);
        }
        AssetBorrowRecordDTO result = assetBorrowRecordService.save(assetBorrowRecordDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("assetBorrowRecord", assetBorrowRecordDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /asset-borrow-records : get all the assetBorrowRecords.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of assetBorrowRecords in body
     */
    @RequestMapping(value = "/asset-borrow-records",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AssetBorrowRecordDTO> getAllAssetBorrowRecords(AssetBorrowRecordSearchDTO searchDTO, Sort sort) {
        log.debug("REST request to get all AssetBorrowRecords");

        if (BooleanUtils.toBoolean(searchDTO.getShownOwnedOnly())) {
            searchDTO.setOwnerId(userService.currentLoginEmployee().getId());
        }
        Predicate predicate = QueryDslUtil.AssetBorrowSearchSearchDTO2Predicate(searchDTO);

        return predicate == null ? assetBorrowRecordService.findAll(sort) : assetBorrowRecordService.findAll(predicate, sort);
    }

    /**
     * GET  /asset-borrow-records/:id : get the "id" assetBorrowRecord.
     *
     * @param id the id of the assetBorrowRecordDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the assetBorrowRecordDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/asset-borrow-records/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AssetBorrowRecordDTO> getAssetBorrowRecord(@PathVariable Long id) {
        log.debug("REST request to get AssetBorrowRecord : {}", id);
        AssetBorrowRecordDTO assetBorrowRecordDTO = assetBorrowRecordService.findOne(id);
        return Optional.ofNullable(assetBorrowRecordDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /asset-borrow-records/:id : delete the "id" assetBorrowRecord.
     *
     * @param id the id of the assetBorrowRecordDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/asset-borrow-records/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAssetBorrowRecord(@PathVariable Long id) {
        log.debug("REST request to delete AssetBorrowRecord : {}", id);
        assetBorrowRecordService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("assetBorrowRecord", id.toString())).build();
    }

    /**
     * POST  /asset-borrow-records/:id : return the borrowed item
     *
     * @param id the id of the assetBorrowRecordDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/asset-borrow-records/{id}/return",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> returnAssetBorrowRecord(@PathVariable Long id) {
        log.debug("REST request to delete AssetBorrowRecord : {}", id);
        assetBorrowRecordService.returnItem(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("assetBorrowRecord", id.toString())).build();
    }

}
