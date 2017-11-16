package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.yijia.yy.domain.QEquipment;
import com.yijia.yy.service.EquipmentService;
import com.yijia.yy.service.dto.EquipmentDTO;
import com.yijia.yy.web.rest.util.HeaderUtil;
import com.yijia.yy.web.rest.util.PaginationUtil;
import javafx.scene.control.Pagination;
import org.apache.commons.lang3.StringUtils;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Equipment.
 */
@RestController
@RequestMapping("/api")
public class EquipmentResource {

    private final Logger log = LoggerFactory.getLogger(EquipmentResource.class);

    @Inject
    private EquipmentService equipmentService;

    /**
     * POST  /equipment : Create a new equipment.
     *
     * @param equipmentDTO the equipmentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new equipmentDTO, or with status 400 (Bad Request) if the equipment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/equipment",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EquipmentDTO> createEquipment(@Valid @RequestBody EquipmentDTO equipmentDTO) throws URISyntaxException {
        log.debug("REST request to save Equipment : {}", equipmentDTO);
        if (equipmentDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("equipment", "idexists", "A new equipment cannot already have an ID")).body(null);
        }
        EquipmentDTO result = equipmentService.save(equipmentDTO);
        return ResponseEntity.created(new URI("/api/equipment/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("equipment", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /equipment : Updates an existing equipment.
     *
     * @param equipmentDTO the equipmentDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated equipmentDTO,
     * or with status 400 (Bad Request) if the equipmentDTO is not valid,
     * or with status 500 (Internal Server Error) if the equipmentDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/equipment",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EquipmentDTO> updateEquipment(@Valid @RequestBody EquipmentDTO equipmentDTO) throws URISyntaxException {
        log.debug("REST request to update Equipment : {}", equipmentDTO);
        if (equipmentDTO.getId() == null) {
            return createEquipment(equipmentDTO);
        }
        EquipmentDTO result = equipmentService.save(equipmentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("equipment", equipmentDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /equipment : get all the equipment.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of equipment in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/equipment",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<EquipmentDTO>> getAllEquipment(Pageable pageable, String name, Long categoryId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Equipment");

        Page<EquipmentDTO> page = null;
        if (StringUtils.isNoneBlank(name) || categoryId != null) {
            List<BooleanExpression> predicates = new ArrayList<>();
            QEquipment equipment = QEquipment.equipment;
            if (StringUtils.isNoneBlank(name)) {
                predicates.add(equipment.name.like("%"+name+"%"));
            }
            if (categoryId != null) {
                predicates.add(equipment.category.id.eq(categoryId));
            }
            Predicate predicate = predicates.stream().reduce((a, b) -> a.and(b)).get();
            page = equipmentService.findAll(predicate, pageable);
        } else {
            page = equipmentService.findAll(pageable);
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/equipment");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /equipment/:id : get the "id" equipment.
     *
     * @param id the id of the equipmentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the equipmentDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/equipment/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EquipmentDTO> getEquipment(@PathVariable Long id) {
        log.debug("REST request to get Equipment : {}", id);
        EquipmentDTO equipmentDTO = equipmentService.findOne(id);
        return Optional.ofNullable(equipmentDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /equipment/:id : delete the "id" equipment.
     *
     * @param id the id of the equipmentDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/equipment/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {
        log.debug("REST request to delete Equipment : {}", id);
        equipmentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("equipment", id.toString())).build();
    }

}
