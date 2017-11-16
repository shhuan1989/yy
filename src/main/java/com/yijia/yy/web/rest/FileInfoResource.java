package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yijia.yy.service.FileInfoService;
import com.yijia.yy.web.rest.util.HeaderUtil;
import com.yijia.yy.service.dto.FileInfoDTO;
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
 * REST controller for managing FileInfo.
 */
@RestController
@RequestMapping("/api")
public class FileInfoResource {

    private final Logger log = LoggerFactory.getLogger(FileInfoResource.class);
        
    @Inject
    private FileInfoService fileInfoService;

    /**
     * POST  /file-infos : Create a new fileInfo.
     *
     * @param fileInfoDTO the fileInfoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fileInfoDTO, or with status 400 (Bad Request) if the fileInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/file-infos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FileInfoDTO> createFileInfo(@RequestBody FileInfoDTO fileInfoDTO) throws URISyntaxException {
        log.debug("REST request to save FileInfo : {}", fileInfoDTO);
        if (fileInfoDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("fileInfo", "idexists", "A new fileInfo cannot already have an ID")).body(null);
        }
        FileInfoDTO result = fileInfoService.save(fileInfoDTO);
        return ResponseEntity.created(new URI("/api/file-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("fileInfo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /file-infos : Updates an existing fileInfo.
     *
     * @param fileInfoDTO the fileInfoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fileInfoDTO,
     * or with status 400 (Bad Request) if the fileInfoDTO is not valid,
     * or with status 500 (Internal Server Error) if the fileInfoDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/file-infos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FileInfoDTO> updateFileInfo(@RequestBody FileInfoDTO fileInfoDTO) throws URISyntaxException {
        log.debug("REST request to update FileInfo : {}", fileInfoDTO);
        if (fileInfoDTO.getId() == null) {
            return createFileInfo(fileInfoDTO);
        }
        FileInfoDTO result = fileInfoService.save(fileInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("fileInfo", fileInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /file-infos : get all the fileInfos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of fileInfos in body
     */
    @RequestMapping(value = "/file-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<FileInfoDTO> getAllFileInfos() {
        log.debug("REST request to get all FileInfos");
        return fileInfoService.findAll();
    }

    /**
     * GET  /file-infos/:id : get the "id" fileInfo.
     *
     * @param id the id of the fileInfoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fileInfoDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/file-infos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FileInfoDTO> getFileInfo(@PathVariable Long id) {
        log.debug("REST request to get FileInfo : {}", id);
        FileInfoDTO fileInfoDTO = fileInfoService.findOne(id);
        return Optional.ofNullable(fileInfoDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /file-infos/:id : delete the "id" fileInfo.
     *
     * @param id the id of the fileInfoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/file-infos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFileInfo(@PathVariable Long id) {
        log.debug("REST request to delete FileInfo : {}", id);
        fileInfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("fileInfo", id.toString())).build();
    }

}
