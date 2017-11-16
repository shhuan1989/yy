package com.yijia.yy.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.querydsl.core.types.Predicate;
import com.yijia.yy.domain.Employee;
import com.yijia.yy.domain.QTask;
import com.yijia.yy.domain.ShootConfig;
import com.yijia.yy.domain.User;
import com.yijia.yy.domain.enumeration.ApprovalStatus;
import com.yijia.yy.domain.enumeration.TaskStatus;
import com.yijia.yy.exception.ProjectArchiveHasUnfinishedTaskException;
import com.yijia.yy.repository.ShootConfigRepository;
import com.yijia.yy.security.AuthoritiesConstants;
import com.yijia.yy.service.*;
import com.yijia.yy.service.dto.*;
import com.yijia.yy.web.rest.util.HeaderUtil;
import com.yijia.yy.web.util.QueryDslUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing Project.
 */
@RestController
@RequestMapping("/api")
public class ProjectResource {

    private final Logger log = LoggerFactory.getLogger(ProjectResource.class);

    @Inject
    private ProjectService projectService;

    @Inject
    private UserService userService;

    @Inject
    private EmployeeService employeeService;

    @Inject
    private ProjectCostService projectCostService;

    @Inject
    private ShootConfigRepository shootConfigRepository;

    @Inject
    private ProjectRateService rateService;

    @Inject
    private TaskService taskService;

    @Inject
    private ExpenseService expenseService;

    /**
     * POST  /projects : Create a new project.
     *
     * @param projectDTO the projectDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projectDTO, or with status 400 (Bad Request) if the project has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/projects",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.EDIT_PROJECT_MANAGE})
    public ResponseEntity<ProjectDTO> createProject(@RequestBody ProjectDTO projectDTO) throws URISyntaxException {
        log.debug("REST request to save Project : {}", projectDTO);
        if (projectDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("project", "idexists", "A new project cannot already have an ID")).body(null);
        }
        ProjectDTO result = projectService.save(projectDTO);
        return ResponseEntity.created(new URI("/api/projects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("project", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /projects : Updates an existing project.
     *
     * @param projectDTO the projectDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projectDTO,
     * or with status 400 (Bad Request) if the projectDTO is not valid,
     * or with status 500 (Internal Server Error) if the projectDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/projects",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
//    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.EDIT_PROJECT_MANAGE})
    public ResponseEntity<ProjectDTO> updateProject(@RequestBody ProjectDTO projectDTO) throws URISyntaxException {
        log.debug("REST request to update Project : {}", projectDTO);
        if (projectDTO.getId() == null) {
            return createProject(projectDTO);
        }
        ProjectDTO result = projectService.save(projectDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("project", projectDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /projects : get all the projects.
     *
     * @param searchDTO the dto for searching
     * @param sort the order
     * @return the ResponseEntity with status 200 (OK) and the list of projects in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/projects",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
//    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.VIEW_PROJECT_MANAGE, AuthoritiesConstants.EDIT_PROJECT_MANAGE})
    public ResponseEntity<List<ProjectDTO>> getAllProjects(ProjectSearchDTO searchDTO, Sort sort)
        throws URISyntaxException {
        log.debug("REST request to get a page of Projects");

        User user = userService.currentLoginUser().get();
        if (user == null) {
            return ResponseEntity.ok(new ArrayList<>());
        }

        Predicate predicate = QueryDslUtil.ProjectSearchDTO2Predicate(searchDTO);
        List<ProjectDTO> result = predicate == null ? projectService.findAll(sort) : projectService.findAll(predicate, sort);
        if (BooleanUtils.toBoolean(searchDTO.getOnlyActiveApproval())) {
            result.forEach(p -> {
                p.setBonusApprovals(
                    p.getBonusApprovals().stream()
                    .filter(a -> BooleanUtils.toBoolean(a.getActive()))
                    .collect(Collectors.toSet())
                );
            });
        }

        if (BooleanUtils.toBoolean(searchDTO.getHasShootConfig())) {
            List<ShootConfig> shootConfigs = shootConfigRepository.findAll();
            Set<Long> pids = shootConfigs.stream()
                .map(s -> s.getProject().getId())
                .collect(Collectors.toSet());

            result = result.stream()
                .filter(p -> !pids.contains(p.getId()))
                .collect(Collectors.toList());
        }

        // filter by member id
        if (searchDTO.getMemberId() != null) {
            result = result.stream()
                .filter(p ->
                    p.getAllMembers().stream()
                        .filter(m -> searchDTO.getMemberId().equals(m.getId()))
                        .findAny()
                        .isPresent()
                )
                .collect(Collectors.toList());
        }


        boolean showOwnedOnly = !user.hasAuthority(AuthoritiesConstants.VIEW_PROJECT_MANAGE_ALL)
            && !user.hasAuthority(AuthoritiesConstants.ADMIN);

        if (showOwnedOnly) {
            Employee employee = userService.currentLoginEmployee();
            if (employee == null) {
                return ResponseEntity.ok(new ArrayList<>());
            }

            List<Employee> employees = employeeService.subordinates(employee.getId());
            employees.add(employee);

            result = result.stream()
                .filter(p -> {
                    Set<EmployeeDTO> members = new HashSet<>();
                    if (p.getAes() != null) {
                        members.addAll(p.getAes());
                    }
                    if (p.getMembers() != null) {
                        members.addAll(p.getMembers());
                    }

                    return members.stream()
                        .filter(e -> employees.stream().filter(e1 -> e1.getId().equals(e.getId())).findAny().isPresent())
                        .findAny()
                        .isPresent();
                })
                .collect(Collectors.toList());
        }

        // get cost of project
        List<ProjectCostDTO> costs = projectCostService.findAll(null);
        result.forEach(p -> {
            p.setCost(
                costs.stream()
                .filter(c -> c.getProjectId() != null && c.getProjectId().equals(p.getId()))
                .mapToDouble(c -> c.getAmount())
                .sum()
            );
        });

        // get expense of project
        ExpenseSearchDTO expenseSearchDTO = new ExpenseSearchDTO();
        expenseSearchDTO.setApprovalStatusId(ApprovalStatus.APPROVED.ordinal());
        List<ExpenseDTO> expenses = expenseService.findAll(QueryDslUtil.ExpenseSearchDTO2Predicate(expenseSearchDTO), null);
        result.forEach(p -> {
            p.setCost(
                p.getCost()
                + expenses.stream()
                .filter(e -> e.getProjectId() != null && e.getProjectId().equals(p.getId()))
                .mapToDouble(e -> e.getTotal())
                .sum()
            );
        });

        // get shoot cost of project
        ShootConfigSearchDTO ss = new ShootConfigSearchDTO();
        ss.setType(ShootConfig.TYPE_COST);
        ss.setApprovalStatusId(ApprovalStatus.APPROVED.ordinal());
        Predicate sp = QueryDslUtil.ShootConfigSearchDTO2Predicate(ss);
        List<ShootConfig> shootConfigs = StreamSupport.stream(shootConfigRepository.findAll(sp).spliterator(), false)
            .collect(Collectors.toList());
        result.forEach(p -> {
            p.setCost(
                p.getCost()
                + shootConfigs.stream()
                    .filter(s -> s.getProject() != null && p.getId().equals(s.getProject().getId()))
                    .mapToDouble(s -> s.getActualCost())
                    .sum()
                + taxAndInfoCostOfProject(p)
            );
        });

        for (ProjectDTO p : result) {
            if (p.getContract() != null && p.getContract().getMoneyAmount() != null) {
                p.setBonusPool(p.getContract().getMoneyAmount() - p.getCost());
            }
        }

        return ResponseEntity.ok(result);
    }


    /**
     * POST /projects/:id : close the "id" project
     *
     * @param id the id of the projectDTO to retrieve
     * @return the ResponseEntity with status 200 (OK), or with status 404 (Not Found)
     */
    @RequestMapping(value = "/projects/close",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
//    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.EDIT_PROJECT_MANAGE})
    public ResponseEntity<Void> closeProject(Long id) {
        log.debug("REST request to close project");
        projectService.close(id);

        return ResponseEntity.ok().build();
    }

    /**
     * POST /projects/:id : archive the "id" project
     *
     * @param id the id of the projectDTO to retrieve
     * @return the ResponseEntity with status 200 (OK), or with status 404 (Not Found)
     */
    @RequestMapping(value = "/projects/archive",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
//    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.EDIT_PROJECT_MANAGE})
    public ResponseEntity<Void> archiveProject(Long id) {
        log.debug("REST request to close project");
        try {
            projectService.archive(id);
        } catch (ProjectArchiveHasUnfinishedTaskException e) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("project_archive", "unfinished_tasks_exits", "Can't archive project because there are some unfinished tasks"))
                .body(null);
        }

        return ResponseEntity.ok().build();
    }

    /**
     * POST /projects/:id : pause the "id" project
     *
     * @param id the id of the projectDTO to retrieve
     * @return the ResponseEntity with status 200 (OK), or with status 404 (Not Found)
     */
    @RequestMapping(value = "/projects/pause",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
//    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.EDIT_PROJECT_MANAGE})
    public ResponseEntity<Void> pauseProject(Long id) {
        log.debug("REST request to close project");
        projectService.pause(id);

        return ResponseEntity.ok().build();
    }

    /**
     * POST /projects/:id : restart the "id" project
     *
     * @param id the id of the projectDTO to retrieve
     * @return the ResponseEntity with status 200 (OK), or with status 404 (Not Found)
     */
    @RequestMapping(value = "/projects/restart",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
//    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.EDIT_PROJECT_MANAGE})
    public ResponseEntity<Void> restartProject(Long id) {
        log.debug("REST request to close project");
        projectService.restart(id);

        return ResponseEntity.ok().build();
    }

    /**
     * POST  /projects/:id/close_rate : close rates of the "id" project.
     *
     * @param id the id of the projectDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projectDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/projects/{id}/close_rates",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
//    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.VIEW_PROJECT_MANAGE, AuthoritiesConstants.EDIT_PROJECT_MANAGE})
    public ResponseEntity<Void> closeProjectRates(@PathVariable Long id) {
        log.debug("REST request to close rates of Project : {}", id);

        rateService.closeProjectRates(id);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /projects/:id : get the "id" project.
     *
     * @param id the id of the projectDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projectDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/projects/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
//    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.VIEW_PROJECT_MANAGE, AuthoritiesConstants.EDIT_PROJECT_MANAGE})
    public ResponseEntity<ProjectDTO> getProject(@PathVariable Long id) {
        log.debug("REST request to get Project : {}", id);
        ProjectDTO projectDTO = projectService.findOne(id);

        return Optional.ofNullable(projectDTO)
            .map(result -> {

                // get cost of project
                List<ProjectCostDTO> costs = projectCostService.findAll(null);
                double cost = costs.stream()
                    .filter(c -> c.getProjectId() != null && c.getProjectId().equals(result.getId()))
                    .mapToDouble(c -> c.getAmount())
                    .sum();

                // get shoot cost of project
                ShootConfigSearchDTO ss = new ShootConfigSearchDTO();
                ss.setType(ShootConfig.TYPE_COST);
                ss.setApprovalStatusId(ApprovalStatus.APPROVED.ordinal());
                ss.setProjectId(id);
                Predicate sp = QueryDslUtil.ShootConfigSearchDTO2Predicate(ss);
                List<ShootConfig> shootConfigs = StreamSupport.stream(shootConfigRepository.findAll(sp).spliterator(), false)
                    .collect(Collectors.toList());

                cost += shootConfigs.stream()
                    .mapToDouble(s -> s.getActualCost())
                    .sum()
                    + taxAndInfoCostOfProject(result);

                // other costs of project
                // get expense of project
                ExpenseSearchDTO expenseSearchDTO = new ExpenseSearchDTO();
                expenseSearchDTO.setApprovalStatusId(ApprovalStatus.APPROVED.ordinal());
                expenseSearchDTO.setProjectId(id);
                List<ExpenseDTO> expenses = expenseService.findAll(QueryDslUtil.ExpenseSearchDTO2Predicate(expenseSearchDTO), null);
                cost += expenses.stream()
                    .mapToDouble(e -> e.getTotal())
                    .sum();

                result.setCost(cost);

                if (result.getContract() != null && result.getContract().getMoneyAmount() != null) {
                    result.setBonusPool(result.getContract().getMoneyAmount() - result.getCost());
                }

                return new ResponseEntity<>(
                    result,
                    HttpStatus.OK);
            })
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    private double taxAndInfoCostOfProject(ProjectDTO project) {
        double taxAndInfo = 0.0;
        if (project.getContract() != null) {
            try {
                taxAndInfo = Double.parseDouble(project.getContract().getTax());
                if (project.getContract().getInfoCost() != null) {
                    taxAndInfo += project.getContract().getInfoCost();
                }
            } catch (Exception e) {
            }
        }

        return taxAndInfo;
    }

    /**
     * GET  /projects/:id/members/:memberId/hastask : where the member has task or not
     *
     * @param id the id of the projectDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projectDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/projects/{id}/members/{memberId}/hastask",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
//    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.VIEW_PROJECT_MANAGE, AuthoritiesConstants.EDIT_PROJECT_MANAGE})
    public ResponseEntity<Boolean> memberHasTask(@PathVariable Long id, @PathVariable Long memberId) {
        log.debug("REST request to get where user :{} has task in project : {}", memberId, id);

        if (id == null || memberId == null) {
            return ResponseEntity.badRequest().body(false);
        }

        QTask task = QTask.task;
        Boolean hasTask = !taskService.findAll(task.project.id.eq(id).and(task.owner.id.eq(memberId)).and(task.status.ne(TaskStatus.DONE)), null).isEmpty();

        return new ResponseEntity<>(
            hasTask,
            HttpStatus.OK);
    }

    /**
     * DELETE  /projects/:id : delete the "id" project.
     *
     * @param id the id of the projectDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/projects/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
//    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.EDIT_PROJECT_MANAGE})
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        log.debug("REST request to delete Project : {}", id);
        projectService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("project", id.toString())).build();
    }

}
