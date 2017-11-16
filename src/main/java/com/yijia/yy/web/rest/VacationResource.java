package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.querydsl.core.types.Predicate;
import com.yijia.yy.domain.Employee;
import com.yijia.yy.service.EmployeeService;
import com.yijia.yy.service.UserService;
import com.yijia.yy.service.VacationService;
import com.yijia.yy.service.dto.VacationDTO;
import com.yijia.yy.service.dto.VacationSearchDTO;
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
import java.util.Set;
import java.util.stream.Collectors;

/**
 * REST controller for managing Vacation.
 */
@RestController
@RequestMapping("/api")
public class VacationResource {

    private final Logger log = LoggerFactory.getLogger(VacationResource.class);

    @Inject
    private VacationService vacationService;

    @Inject
    private UserService userService;

    @Inject
    private EmployeeService employeeService;

    /**
     * POST  /vacations : Create a new vacation.
     *
     * @param vacationDTO the vacationDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new vacationDTO, or with status 400 (Bad Request) if the vacation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/vacations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<VacationDTO> createVacation(@Valid @RequestBody VacationDTO vacationDTO) throws URISyntaxException {
        log.debug("REST request to save Vacation : {}", vacationDTO);
        if (vacationDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("vacation", "idexists", "A new vacation cannot already have an ID")).body(null);
        }
        VacationDTO result = vacationService.save(vacationDTO);
        return ResponseEntity.created(new URI("/api/vacations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("vacation", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /vacations : Updates an existing vacation.
     *
     * @param vacationDTO the vacationDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated vacationDTO,
     * or with status 400 (Bad Request) if the vacationDTO is not valid,
     * or with status 500 (Internal Server Error) if the vacationDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/vacations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<VacationDTO> updateVacation(@Valid @RequestBody VacationDTO vacationDTO) throws URISyntaxException {
        log.debug("REST request to update Vacation : {}", vacationDTO);
        if (vacationDTO.getId() == null) {
            return createVacation(vacationDTO);
        }
        VacationDTO result = vacationService.save(vacationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("vacation", vacationDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /vacations : get all the vacations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of vacations in body
     */
    @RequestMapping(value = "/vacations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<VacationDTO> getAllVacations(VacationSearchDTO searchDTO, Sort sort) {
        log.debug("REST request to get all Vacations");

        Predicate predicate = QueryDslUtil.VacationSearchDTO2Predicate(searchDTO);

        List<VacationDTO> vacations = predicate == null ? vacationService.findAll(sort) : vacationService.findAll(predicate, sort);

        if (BooleanUtils.toBoolean(searchDTO.getShownOwnedOnly())) {
            Long uid = userService.currentLoginEmployee().getId();
            Set<Long> sbs = employeeService.subordinates(uid)
                .stream().map(e -> e.getId()).collect(Collectors.toSet());
            sbs.add(uid);
            vacations = vacations.stream()
                .filter(v -> v.getOwner() != null && sbs.contains(v.getOwner().getId()))
                .collect(Collectors.toList());
        }

        return vacations;
    }

    /**
     * GET  /vacations/:id : get the "id" vacation.
     *
     * @param id the id of the vacationDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the vacationDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/vacations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<VacationDTO> getVacation(@PathVariable Long id) {
        log.debug("REST request to get Vacation : {}", id);
        VacationDTO vacationDTO = vacationService.findOne(id);
        return Optional.ofNullable(vacationDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /vacations/:id : delete the "id" vacation.
     *
     * @param id the id of the vacationDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/vacations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteVacation(@PathVariable Long id) {
        log.debug("REST request to delete Vacation : {}", id);
        vacationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("vacation", id.toString())).build();
    }

}
