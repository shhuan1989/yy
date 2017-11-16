package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.querydsl.core.types.Predicate;
import com.yijia.yy.domain.ShootConfig;
import com.yijia.yy.domain.User;
import com.yijia.yy.security.AuthoritiesConstants;
import com.yijia.yy.service.ProjectService;
import com.yijia.yy.service.ShootConfigService;
import com.yijia.yy.service.UserService;
import com.yijia.yy.service.dto.ShootConfigSearchDTO;
import com.yijia.yy.web.rest.util.HeaderUtil;
import com.yijia.yy.service.dto.ShootConfigDTO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * REST controller for managing ShootConfig.
 */
@RestController
@RequestMapping("/api")
public class ShootConfigResource {

    private final Logger log = LoggerFactory.getLogger(ShootConfigResource.class);

    @Inject
    private ShootConfigService shootConfigService;

    @Inject
    private UserService userService;

    @Inject
    private ProjectService projectService;

    /**
     * POST  /shoot-configs : Create a new shootConfigs.
     *
     * @param shootConfigDTO the shootConfigDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new shootConfigDTO, or with status 400 (Bad Request) if the shootConfigs has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/shoot-configs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ShootConfigDTO> createShootConfigs(@RequestBody ShootConfigDTO shootConfigDTO) throws URISyntaxException {
        log.debug("REST request to save ShootConfig : {}", shootConfigDTO);
        if (shootConfigDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("shootConfigs", "idexists", "A new shootConfigs cannot already have an ID")).body(null);
        }
        ShootConfigDTO result = shootConfigService.save(shootConfigDTO);
        return ResponseEntity.created(new URI("/api/shoot-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("shootConfigs", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /shoot-configs : Updates an existing shootConfigs.
     *
     * @param shootConfigDTO the shootConfigDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated shootConfigDTO,
     * or with status 400 (Bad Request) if the shootConfigDTO is not valid,
     * or with status 500 (Internal Server Error) if the shootConfigDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/shoot-configs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ShootConfigDTO> updateShootConfigs(@RequestBody ShootConfigDTO shootConfigDTO) throws URISyntaxException {
        log.debug("REST request to update ShootConfig : {}", shootConfigDTO);
        if (shootConfigDTO.getId() == null) {
            return createShootConfigs(shootConfigDTO);
        }
        ShootConfigDTO result = shootConfigService.save(shootConfigDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("shootConfigs", shootConfigDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /shoot-configs : get all the shootConfigs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of shootConfigs in body
     */
    @RequestMapping(value = "/shoot-configs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ShootConfigDTO> getAllShootConfigs(ShootConfigSearchDTO searchDTO, Sort sort) {
        log.debug("REST request to get all ShootConfig");

        User user = userService.currentLoginUser().get();

        if (user == null || user.getEmployee() == null
            || (
                !user.hasAuthority(AuthoritiesConstants.ADMIN)
                && !user.hasAuthority(AuthoritiesConstants.VIEW_BUDGET)
                && !user.hasAuthority(AuthoritiesConstants.VIEW_COST)
                && !user.hasAuthority(AuthoritiesConstants.VIEW_BUDGET_VS_COST)
                && !user.hasAuthority(AuthoritiesConstants.VIEW_OWNED_SHOOT_BUDGET)
                && !user.hasAuthority(AuthoritiesConstants.VIEW_OWNED_SHOOT_COST)
                && !user.hasAuthority(AuthoritiesConstants.VIEW_OWNED_SHOOT_AUDIT)
            )
        ) {
            return new ArrayList<>();
        }


        boolean showOwnedOnly = user != null && user.getEmployee() != null
            && !user.hasAuthority(AuthoritiesConstants.ADMIN);

        if (searchDTO.getType() != null) {
            if (searchDTO.getType().equals(ShootConfig.TYPE_BUDGET)) {
                showOwnedOnly = showOwnedOnly && !user.hasAuthority(AuthoritiesConstants.VIEW_BUDGET);
            } else if (searchDTO.getType().equals(ShootConfig.TYPE_COST)) {
                showOwnedOnly = showOwnedOnly && !user.hasAuthority(AuthoritiesConstants.VIEW_COST);
            } else if (searchDTO.getType().equals(ShootConfig.TYPE_COST_FOR_AUDIT)) {
                showOwnedOnly = showOwnedOnly && !user.hasAuthority(AuthoritiesConstants.VIEW_BUDGET_VS_COST);
            }
        }

        if (searchDTO.getType() != null && searchDTO.getType().equals(ShootConfig.TYPE_COST_FOR_AUDIT)) {
            searchDTO.setType(ShootConfig.TYPE_COST);
        }
        Predicate predicate = QueryDslUtil.ShootConfigSearchDTO2Predicate(searchDTO);
        List<ShootConfigDTO> shootConfigs = predicate == null ? shootConfigService.findAll(sort) : shootConfigService.findAll(predicate, sort);

        if (showOwnedOnly) {
            Set<Long> pids = projectService.projectIdsParticipatedByEmployee(user.getEmployee().getId());
            shootConfigs = shootConfigs.stream()
                .filter(c -> pids.contains(c.getProjectId()))
                .collect(Collectors.toList());
        }

        return shootConfigs;
    }

    /**
     * GET  /shoot-configs/:id : get the "id" shootConfigs.
     *
     * @param id the id of the shootConfigsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the shootConfigsDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/shoot-configs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ShootConfigDTO> getShootConfigs(@PathVariable Long id) {
        log.debug("REST request to get ShootConfig : {}", id);
        ShootConfigDTO shootConfigDTO = shootConfigService.findOne(id);
        return Optional.ofNullable(shootConfigDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /shoot-configs/:id : delete the "id" shootConfigs.
     *
     * @param id the id of the shootConfigsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/shoot-configs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteShootConfigs(@PathVariable Long id) {
        log.debug("REST request to delete ShootConfig : {}", id);
        shootConfigService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("shootConfigs", id.toString())).build();
    }

}
