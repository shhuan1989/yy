package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yijia.yy.service.NoticeChatService;
import com.yijia.yy.web.rest.util.HeaderUtil;
import com.yijia.yy.service.dto.NoticeChatDTO;
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
import java.util.stream.StreamSupport;

/**
 * REST controller for managing NoticeChat.
 */
@RestController
@RequestMapping("/api")
public class NoticeChatResource {

    private final Logger log = LoggerFactory.getLogger(NoticeChatResource.class);
        
    @Inject
    private NoticeChatService noticeChatService;

    /**
     * POST  /notice-chats : Create a new noticeChat.
     *
     * @param noticeChatDTO the noticeChatDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new noticeChatDTO, or with status 400 (Bad Request) if the noticeChat has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/notice-chats",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NoticeChatDTO> createNoticeChat(@RequestBody NoticeChatDTO noticeChatDTO) throws URISyntaxException {
        log.debug("REST request to save NoticeChat : {}", noticeChatDTO);
        if (noticeChatDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("noticeChat", "idexists", "A new noticeChat cannot already have an ID")).body(null);
        }
        NoticeChatDTO result = noticeChatService.save(noticeChatDTO);
        return ResponseEntity.created(new URI("/api/notice-chats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("noticeChat", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /notice-chats : Updates an existing noticeChat.
     *
     * @param noticeChatDTO the noticeChatDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated noticeChatDTO,
     * or with status 400 (Bad Request) if the noticeChatDTO is not valid,
     * or with status 500 (Internal Server Error) if the noticeChatDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/notice-chats",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NoticeChatDTO> updateNoticeChat(@RequestBody NoticeChatDTO noticeChatDTO) throws URISyntaxException {
        log.debug("REST request to update NoticeChat : {}", noticeChatDTO);
        if (noticeChatDTO.getId() == null) {
            return createNoticeChat(noticeChatDTO);
        }
        NoticeChatDTO result = noticeChatService.save(noticeChatDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("noticeChat", noticeChatDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /notice-chats : get all the noticeChats.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of noticeChats in body
     */
    @RequestMapping(value = "/notice-chats",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<NoticeChatDTO> getAllNoticeChats(@RequestParam(required = false) String filter) {
        if ("pictureinfo-is-null".equals(filter)) {
            log.debug("REST request to get all NoticeChats where pictureInfo is null");
            return noticeChatService.findAllWherePictureInfoIsNull();
        }
        if ("fileinfo-is-null".equals(filter)) {
            log.debug("REST request to get all NoticeChats where fileInfo is null");
            return noticeChatService.findAllWhereFileInfoIsNull();
        }
        log.debug("REST request to get all NoticeChats");
        return noticeChatService.findAll();
    }

    /**
     * GET  /notice-chats/:id : get the "id" noticeChat.
     *
     * @param id the id of the noticeChatDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the noticeChatDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/notice-chats/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NoticeChatDTO> getNoticeChat(@PathVariable Long id) {
        log.debug("REST request to get NoticeChat : {}", id);
        NoticeChatDTO noticeChatDTO = noticeChatService.findOne(id);
        return Optional.ofNullable(noticeChatDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /notice-chats/:id : delete the "id" noticeChat.
     *
     * @param id the id of the noticeChatDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/notice-chats/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteNoticeChat(@PathVariable Long id) {
        log.debug("REST request to delete NoticeChat : {}", id);
        noticeChatService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("noticeChat", id.toString())).build();
    }

}
