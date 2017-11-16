package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.EmployeeService;
import com.yijia.yy.service.UserService;
import com.yijia.yy.service.dto.EmployeeSearchDTO;
import com.yijia.yy.service.mapper.EmployeeMapper;
import com.yijia.yy.web.rest.util.HeaderUtil;
import com.yijia.yy.web.rest.util.PaginationUtil;
import com.yijia.yy.service.dto.EmployeeDTO;
import com.yijia.yy.web.util.QueryDslUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Employee.
 */
@RestController
@RequestMapping("/api")
public class EmployeeResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeResource.class);

    @Inject
    private EmployeeService employeeService;

    @Inject
    private UserService userService;

    @Inject
    private EmployeeMapper employeeMapper;

    /**
     * POST  /employees : Create a new employee.
     *
     * @param employeeDTO the employeeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new employeeDTO, or with status 400 (Bad Request) if the employee has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/employees",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) throws URISyntaxException {
        log.debug("REST request to save Employee : {}", employeeDTO);
        if (employeeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("employee", "idexists", "A new employee cannot already have an ID")).body(null);
        }
        EmployeeDTO result = employeeService.save(employeeDTO);
        return ResponseEntity.created(new URI("/api/employees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("employee", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /employees : Updates an existing employee.
     *
     * @param employeeDTO the employeeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated employeeDTO,
     * or with status 400 (Bad Request) if the employeeDTO is not valid,
     * or with status 500 (Internal Server Error) if the employeeDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/employees",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeDTO> updateEmployee(@RequestBody EmployeeDTO employeeDTO) throws URISyntaxException {
        log.debug("REST request to update Employee : {}", employeeDTO);
        if (employeeDTO.getId() == null) {
            return createEmployee(employeeDTO);
        }
        EmployeeDTO result = employeeService.save(employeeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("employee", employeeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /employees : get all the employees.
     *
     * @param searchDTO the dto for seaching employee
     * @param sort the order
     * @return the ResponseEntity with status 200 (OK) and the list of employees in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/employees",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees(EmployeeSearchDTO searchDTO, Sort sort)
        throws URISyntaxException {
        log.debug("REST request to get a page of Employees");
        Predicate predicate = QueryDslUtil.EmployeeSearchDto2Predicate(searchDTO);
        List<EmployeeDTO> result = predicate == null ? employeeService.findAll(sort) : employeeService.findAll(predicate, sort);

        if (searchDTO.getHasLogin() != null) {
            result = result.stream()
                .filter(e -> searchDTO.getHasLogin() ? StringUtils.isNotBlank(e.getLogin()) : StringUtils.isBlank(e.getLogin()))
                .collect(Collectors.toList());
        }

        return ResponseEntity.ok(result);
    }

    /**
     * GET  /employees/:id : get the "id" employee.
     *
     * @param id the id of the employeeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the employeeDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/employees/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<EmployeeDTO> getEmployee(@PathVariable Long id) {
        log.debug("REST request to get Employee : {}", id);
        EmployeeDTO employeeDTO = employeeService.findOne(id);
        return Optional.ofNullable(employeeDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /employees/current get current Employee
     *
     * @return the ResponseEntity with status 200 (OK) and with body the employeeDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/employees/current",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<EmployeeDTO> getCurrentEmployee() {
        log.debug("REST request to get current Employee");
        EmployeeDTO employeeDTO = employeeMapper.employeeToEmployeeDTO(userService.currentLoginEmployee());
        return Optional.ofNullable(employeeDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /employees/:id : delete the "id" employee.
     *
     * @param id the id of the employeeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/employees/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        log.debug("REST request to delete Employee : {}", id);
        employeeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("employee", id.toString())).build();
    }

}
