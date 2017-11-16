package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yijia.yy.domain.PictureInfo;
import com.yijia.yy.service.PictureInfoService;
import com.yijia.yy.service.dto.PictureInfoDTO;
import com.yijia.yy.web.rest.util.HeaderUtil;
import com.yijia.yy.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
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
 * REST controller for managing PictureInfo.
 */
@RestController
@RequestMapping("/api")
public class PictureInfoResource {

    private final Logger log = LoggerFactory.getLogger(PictureInfoResource.class);

    @Inject
    private PictureInfoService pictureInfoService;

    /**
     * POST  /picture-infos : Create a new pictureInfo.
     *
     * @param pictureInfo the pictureInfo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pictureInfo, or with status 400 (Bad Request) if the pictureInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/picture-infos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PictureInfoDTO> createPictureInfo(@RequestBody PictureInfo pictureInfo) throws URISyntaxException {
        log.debug("REST request to save PictureInfo : {}", pictureInfo);
        if (pictureInfo.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("pictureInfo", "idexists", "A new pictureInfo cannot already have an ID")).body(null);
        }
        PictureInfoDTO result = pictureInfoService.save(pictureInfo);
        return ResponseEntity.created(new URI("/api/picture-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("pictureInfo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /picture-infos : Updates an existing pictureInfo.
     *
     * @param pictureInfo the pictureInfo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pictureInfo,
     * or with status 400 (Bad Request) if the pictureInfo is not valid,
     * or with status 500 (Internal Server Error) if the pictureInfo couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/picture-infos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PictureInfoDTO> updatePictureInfo(@RequestBody PictureInfo pictureInfo) throws URISyntaxException {
        log.debug("REST request to update PictureInfo : {}", pictureInfo);
        if (pictureInfo.getId() == null) {
            return createPictureInfo(pictureInfo);
        }
        PictureInfoDTO result = pictureInfoService.save(pictureInfo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("pictureInfo", pictureInfo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /picture-infos : get all the pictureInfos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pictureInfos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/picture-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PictureInfoDTO>> getAllPictureInfos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of PictureInfos");
        Page<PictureInfoDTO> page = pictureInfoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/picture-infos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /picture-infos/:id : get the "id" pictureInfo.
     *
     * @param id the id of the pictureInfo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pictureInfo, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/picture-infos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PictureInfoDTO> getPictureInfo(@PathVariable Long id) {
        log.debug("REST request to get PictureInfo : {}", id);
        PictureInfoDTO pictureInfo = pictureInfoService.findOne(id);
        return Optional.ofNullable(pictureInfo)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /picture-infos/:id : delete the "id" pictureInfo.
     *
     * @param id the id of the pictureInfo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/picture-infos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePictureInfo(@PathVariable Long id) {
        log.debug("REST request to delete PictureInfo : {}", id);
        pictureInfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("pictureInfo", id.toString())).build();
    }

}
