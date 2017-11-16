package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.yijia.yy.domain.Dept;
import com.yijia.yy.domain.Employee;
import com.yijia.yy.domain.Project;
import com.yijia.yy.domain.QNotice;
import com.yijia.yy.repository.ProjectRepository;
import com.yijia.yy.service.NoticeService;
import com.yijia.yy.service.UserService;
import com.yijia.yy.service.dto.NoticeSearchDTO;
import com.yijia.yy.service.dto.ProjectDTO;
import com.yijia.yy.web.rest.util.HeaderUtil;
import com.yijia.yy.service.dto.NoticeDTO;
import com.yijia.yy.web.util.QueryDslUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
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
 * REST controller for managing Notice.
 */
@RestController
@RequestMapping("/api")
public class NoticeResource {

    private final Logger log = LoggerFactory.getLogger(NoticeResource.class);

    @Inject
    private NoticeService noticeService;

    @Inject
    private UserService userService;

    /**
     * POST  /notices : Create a new notice.
     *
     * @param noticeDTO the noticeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new noticeDTO, or with status 400 (Bad Request) if the notice has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/notices",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NoticeDTO> createNotice(@RequestBody NoticeDTO noticeDTO) throws URISyntaxException {
        log.debug("REST request to save Notice : {}", noticeDTO);
        if (noticeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("notice", "idexists", "A new notice cannot already have an ID")).body(null);
        }
        NoticeDTO result = noticeService.save(noticeDTO);
        return ResponseEntity.created(new URI("/api/notices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("notice", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /notices : Updates an existing notice.
     *
     * @param noticeDTO the noticeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated noticeDTO,
     * or with status 400 (Bad Request) if the noticeDTO is not valid,
     * or with status 500 (Internal Server Error) if the noticeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/notices",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NoticeDTO> updateNotice(@RequestBody NoticeDTO noticeDTO) throws URISyntaxException {
        log.debug("REST request to update Notice : {}", noticeDTO);
        if (noticeDTO.getId() == null) {
            return createNotice(noticeDTO);
        }
        NoticeDTO result = noticeService.save(noticeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("notice", noticeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /notices : get all the notices.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of notices in body
     */
    @RequestMapping(value = "/notices",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<NoticeDTO> getAllNotices(NoticeSearchDTO searchDTO, Sort sort) {
        log.debug("REST request to get all Notices");
        Predicate predicate = QueryDslUtil.NoticeSearchDTO2Predicate(searchDTO);
        List<NoticeDTO> notices = predicate == null ? noticeService.findAll(sort) : noticeService.findAll(predicate, sort);

        boolean showOwnedOnly = BooleanUtils.toBoolean(searchDTO.getOwnedOnly());

        if (showOwnedOnly) {
            Employee currentLoginEmployee = userService.currentLoginEmployee();
            Long cid = currentLoginEmployee.getId();
            Dept dept = currentLoginEmployee.getDept();
            notices =
            notices.stream()
                .filter(t -> {
                    // contain current employee
                    boolean valid = t.getEmployees().stream()
                            .filter(e -> e.getId().equals(cid))
                            .findAny()
                            .isPresent();
                    if (valid) {
                        return true;
                    }

                    // contain current employee's dept
                    if (dept != null) {
                        valid =
                            dept == null
                                ||
                                t.getDepts().stream()
                                    .filter(d -> d.getId().equals(dept.getId()))
                                    .findAny()
                                    .isPresent();
                        if (valid) {
                            return true;
                        }
                    }

                    valid =
                        t.getProjects().stream()
                            .filter(p ->
                                p.getAllMembers().stream()
                                    .filter(m -> m.getId().equals(cid))
                                    .findAny()
                                    .isPresent()
                                    ||
                                    p.getViewers().stream()
                                        .filter(m -> m.getId().equals(cid))
                                        .findAny()
                                        .isPresent()
                            )
                            .findAny()
                            .isPresent();
                    return valid;
                })
                .collect(Collectors.toList());
        }

        return notices;
    }

    /**
     * GET  /notices/:id : get the "id" notice.
     *
     * @param id the id of the noticeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the noticeDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/notices/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NoticeDTO> getNotice(@PathVariable Long id) {
        log.debug("REST request to get Notice : {}", id);
        NoticeDTO noticeDTO = noticeService.findOne(id);
        return Optional.ofNullable(noticeDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /notices/:id : delete the "id" notice.
     *
     * @param id the id of the noticeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/notices/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        log.debug("REST request to delete Notice : {}", id);
        noticeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("notice", id.toString())).build();
    }

}
