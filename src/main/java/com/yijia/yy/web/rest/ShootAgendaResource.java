package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.yijia.yy.domain.QShootAgenda;
import com.yijia.yy.domain.ShootAgenda;
import com.yijia.yy.domain.User;
import com.yijia.yy.security.AuthoritiesConstants;
import com.yijia.yy.service.ShootAgendaService;
import com.yijia.yy.service.UserService;
import com.yijia.yy.web.rest.util.HeaderUtil;
import com.yijia.yy.service.dto.ShootAgendaDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing ShootAgenda.
 */
@RestController
@RequestMapping("/api")
public class ShootAgendaResource {

    private final Logger log = LoggerFactory.getLogger(ShootAgendaResource.class);

    @Inject
    private ShootAgendaService shootAgendaService;

    @Inject
    private UserService userService;

    /**
     * POST  /shoot-agenda : Create a new shootAgenda.
     *
     * @param shootAgendaDTO the shootAgendaDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new shootAgendaDTO, or with status 400 (Bad Request) if the shootAgenda has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/shoot-agenda",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ShootAgendaDTO> createShootAgenda(@Valid @RequestBody ShootAgendaDTO shootAgendaDTO) throws URISyntaxException {
        log.debug("REST request to save ShootAgenda : {}", shootAgendaDTO);
        if (shootAgendaDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("shootAgenda", "idexists", "A new shootAgenda cannot already have an ID")).body(null);
        }
        ShootAgendaDTO result = shootAgendaService.save(shootAgendaDTO);
        return ResponseEntity.created(new URI("/api/shoot-agenda/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("shootAgenda", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /shoot-agenda : Updates an existing shootAgenda.
     *
     * @param shootAgendaDTO the shootAgendaDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated shootAgendaDTO,
     * or with status 400 (Bad Request) if the shootAgendaDTO is not valid,
     * or with status 500 (Internal Server Error) if the shootAgendaDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/shoot-agenda",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ShootAgendaDTO> updateShootAgenda(@Valid @RequestBody ShootAgendaDTO shootAgendaDTO) throws URISyntaxException {
        log.debug("REST request to update ShootAgenda : {}", shootAgendaDTO);
        if (shootAgendaDTO.getId() == null) {
            return createShootAgenda(shootAgendaDTO);
        }

        if (!canEditAgenda(shootAgendaDTO.getId())) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("shootAgenda", "unauthorized", "")).body(null);
        }

        ShootAgendaDTO result = shootAgendaService.save(shootAgendaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("shootAgenda", shootAgendaDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /shoot-agenda : get all the shootAgenda.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of shootAgenda in body
     */
    @RequestMapping(value = "/shoot-agenda",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ShootAgendaDTO> getAllShootAgenda(Sort sort) {
        log.debug("REST request to get all ShootAgenda");
        User user = userService.currentLoginUser().get();

        if (user == null || user.getEmployee() == null
            || (
            !user.hasAuthority(AuthoritiesConstants.ADMIN)
                && !user.hasAuthority(AuthoritiesConstants.VIEW_SHOOT_AGENDA)
                && !user.hasAuthority(AuthoritiesConstants.VIEW_OWNED_AGENDA)
        )
            ) {
            return new ArrayList<>();
        }

        boolean showOwnedOnly = user != null && user.getEmployee() != null
            && !user.hasAuthority(AuthoritiesConstants.ADMIN)
            && !user.hasAuthority(AuthoritiesConstants.VIEW_SHOOT_AGENDA);

        if (showOwnedOnly) {
            BooleanExpression predicate = QShootAgenda.shootAgenda.creator.id.eq(user.getEmployee().getId());
            return shootAgendaService.findAll(predicate, sort);
        }
        return shootAgendaService.findAll(sort);
    }

    /**
     * GET  /shoot-agenda/:id : get the "id" shootAgenda.
     *
     * @param id the id of the shootAgendaDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the shootAgendaDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/shoot-agenda/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ShootAgendaDTO> getShootAgenda(@PathVariable Long id) {
        log.debug("REST request to get ShootAgenda : {}", id);
        ShootAgendaDTO shootAgendaDTO = shootAgendaService.findOne(id);
        return Optional.ofNullable(shootAgendaDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /shoot-agenda/:id : delete the "id" shootAgenda.
     *
     * @param id the id of the shootAgendaDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/shoot-agenda/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteShootAgenda(@PathVariable Long id) {
        log.debug("REST request to delete ShootAgenda : {}", id);
        if (!canEditAgenda(id)) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("shootAgenda", "unauthorized", "")).body(null);
        }
        shootAgendaService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("shootAgenda", id.toString())).build();
    }

    private boolean canEditAgenda(Long id) {
        if (id == null) {
            return false;
        }

        User user = userService.currentLoginUser().get();

        if (!user.hasAuthority(AuthoritiesConstants.ADMIN) && !user.hasAuthority(AuthoritiesConstants.EDIT_SHOOT_AGENDA)) {
            if (!user.hasAuthority(AuthoritiesConstants.EDIT_OWNED_AGENDA)) {
                return false;
            }

            if (user == null || user.getEmployee() == null) {
                return false;
            }

            ShootAgendaDTO agenda = shootAgendaService.findOne(id);
            if (agenda.getCreatorId() == null || !agenda.getCreatorId().equals(user.getEmployee().getId())) {
                return false;
            }
        }
        return true;
    }

}
