package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.querydsl.core.types.Predicate;
import com.yijia.yy.domain.User;
import com.yijia.yy.security.AuthoritiesConstants;
import com.yijia.yy.service.DirectorNeedsService;
import com.yijia.yy.service.UserService;
import com.yijia.yy.service.dto.DirectorNeedsSearchDTO;
import com.yijia.yy.web.rest.util.HeaderUtil;
import com.yijia.yy.service.dto.DirectorNeedsDTO;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing DirectorNeeds.
 */
@RestController
@RequestMapping("/api")
public class DirectorNeedsResource {

    private final Logger log = LoggerFactory.getLogger(DirectorNeedsResource.class);

    @Inject
    private DirectorNeedsService directorNeedsService;

    @Inject
    private UserService userService;

    /**
     * POST  /director-needs : Create a new directorNeeds.
     *
     * @param directorNeedsDTO the directorNeedsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new directorNeedsDTO, or with status 400 (Bad Request) if the directorNeeds has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/director-needs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DirectorNeedsDTO> createDirectorNeeds(@RequestBody DirectorNeedsDTO directorNeedsDTO) throws URISyntaxException {
        log.debug("REST request to save DirectorNeeds : {}", directorNeedsDTO);
        if (directorNeedsDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("directorNeeds", "idexists", "A new directorNeeds cannot already have an ID")).body(null);
        }
        DirectorNeedsDTO result = directorNeedsService.save(directorNeedsDTO);
        return ResponseEntity.created(new URI("/api/director-needs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("directorNeeds", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /director-needs : Updates an existing directorNeeds.
     *
     * @param directorNeedsDTO the directorNeedsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated directorNeedsDTO,
     * or with status 400 (Bad Request) if the directorNeedsDTO is not valid,
     * or with status 500 (Internal Server Error) if the directorNeedsDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/director-needs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DirectorNeedsDTO> updateDirectorNeeds(@RequestBody DirectorNeedsDTO directorNeedsDTO) throws URISyntaxException {
        log.debug("REST request to update DirectorNeeds : {}", directorNeedsDTO);
        if (directorNeedsDTO.getId() == null) {
            return createDirectorNeeds(directorNeedsDTO);
        }
        if (!canEdit(directorNeedsDTO.getId())) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("directorNeeds", "noauth", "can't update " + directorNeedsDTO.getId())).body(null);
        }

        DirectorNeedsDTO result = directorNeedsService.save(directorNeedsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("directorNeeds", directorNeedsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /director-needs : get all the directorNeeds.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of directorNeeds in body
     */
    @RequestMapping(value = "/director-needs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<DirectorNeedsDTO> getAllDirectorNeeds(DirectorNeedsSearchDTO searchDTO, Sort sort) {
        log.debug("REST request to get all DirectorNeeds");
        User user = userService.currentLoginUser().get();
        if (user == null) {
            return new ArrayList<>();
        }

        boolean showOwnedOnly = false;
        if (!user.hasAuthority(AuthoritiesConstants.ADMIN)
            && !user.hasAuthority(AuthoritiesConstants.VIEW_SHOOT_CONFIG)
            && !user.hasAuthority(AuthoritiesConstants.EDIT_SHOOT_CONFIG)) {

            if (!user.hasAuthority(AuthoritiesConstants.VIEW_OWNED_SHOOT_CONFIG)
                && !user.hasAuthority(AuthoritiesConstants.EDIT_OWNED_SHOOT_CONFIG)) {
                return new ArrayList<>();
            }
            showOwnedOnly = true;
        }

        Predicate predicate = QueryDslUtil.DirectorNeedsSearchDTO2Predicate(searchDTO);
        List<DirectorNeedsDTO> result = predicate == null ? directorNeedsService.findAll(sort) : directorNeedsService.findAll(predicate, sort);


        if (showOwnedOnly) {
            result = result.stream()
                .filter(d -> d.getCreatorId() != null && user.getEmployee() != null && (d.getCreatorId().equals(user.getEmployee().getId())))
                .collect(Collectors.toList());
        }

        return result;
    }

    /**
     * GET  /director-needs/:id : get the "id" directorNeeds.
     *
     * @param id the id of the directorNeedsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the directorNeedsDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/director-needs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DirectorNeedsDTO> getDirectorNeeds(@PathVariable Long id) {
        log.debug("REST request to get DirectorNeeds : {}", id);
        DirectorNeedsDTO directorNeedsDTO = directorNeedsService.findOne(id);
        return Optional.ofNullable(directorNeedsDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /director-needs/:id : delete the "id" directorNeeds.
     *
     * @param id the id of the directorNeedsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/director-needs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDirectorNeeds(@PathVariable Long id) {
        log.debug("REST request to delete DirectorNeeds : {}", id);

        if (!canEdit(id)) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("directorNeeds", "noauth", "can't delete " + id)).body(null);
        }

        directorNeedsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("directorNeeds", id.toString())).build();
    }

    private boolean canEdit(Long id) {
        if (id == null) {
            return true;
        }
        User user = userService.currentLoginUser().get();
        if (user == null) {
            return false;
        }

        if (user.hasAuthority(AuthoritiesConstants.ADMIN) || user.hasAuthority(AuthoritiesConstants.EDIT_SHOOT_CONFIG)) {
            return true;
        }

        if (!user.hasAuthority(AuthoritiesConstants.EDIT_OWNED_SHOOT_CONFIG)) {
            return false;
        }

        DirectorNeedsDTO dto = directorNeedsService.findOne(id);

        if (dto.getCreatorId() == null || user.getEmployee() == null
            || !dto.getCreatorId().equals(user.getEmployee().getId())) {
            return false;
        }

        return true;
    }

}
