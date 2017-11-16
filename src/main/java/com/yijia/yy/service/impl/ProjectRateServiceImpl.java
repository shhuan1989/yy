package com.yijia.yy.service.impl;

import com.querydsl.core.types.Predicate;
import com.sun.mail.util.QPDecoderStream;
import com.yijia.yy.domain.Employee;
import com.yijia.yy.domain.Project;
import com.yijia.yy.domain.ProjectRate;
import com.yijia.yy.domain.QProjectRate;
import com.yijia.yy.domain.enumeration.ProjectBonusStatus;
import com.yijia.yy.repository.EmployeeRepository;
import com.yijia.yy.repository.ProjectRateRepository;
import com.yijia.yy.repository.ProjectRepository;
import com.yijia.yy.service.ProjectRateService;
import com.yijia.yy.service.UserService;
import com.yijia.yy.service.dto.ProjectRateDTO;
import com.yijia.yy.service.event.ProjectEvent;
import com.yijia.yy.service.mapper.ProjectRateMapper;
import com.yijia.yy.service.util.DomainObjectUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.bus.Event;
import reactor.bus.EventBus;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing ProjectRate.
 */
@Service
@Transactional
public class ProjectRateServiceImpl implements ProjectRateService{

    private final Logger log = LoggerFactory.getLogger(ProjectRateServiceImpl.class);

    @Inject
    private ProjectRateRepository projectRateRepository;

    @Inject
    private ProjectRateMapper projectRateMapper;

    @Inject
    private EventBus eventBus;

    @Inject
    private ApplicationContext appContext;

    @Inject
    private EmployeeRepository employeeRepository;

    @Inject
    private UserService userService;

    @Inject
    private ProjectRepository projectRepository;

    /**
     * Save a projectRate.
     * Synchronize to avoid multiple user update same records simultaneously.
     * @param projectRateDTO the entity to save
     * @return the persisted entity
     */
    @Transactional
    public synchronized ProjectRateDTO save(ProjectRateDTO projectRateDTO) {
        log.debug("Request to save ProjectRate : {}", projectRateDTO);
        ProjectRate projectRate = projectRateMapper.projectRateDTOToProjectRate(projectRateDTO);
        if (BooleanUtils.toBoolean(projectRate.getFinished())) {
            projectRate.setFinishTime(System.currentTimeMillis());
        }

        if (projectRate.getId() != null) {
            ProjectRate oldRate = projectRateRepository.findOne(projectRate.getId());
            DomainObjectUtils.copyNonNullFields(projectRate, oldRate);
            projectRate = oldRate;
        }

        DomainObjectUtils.syncRateScoreAndDesc(projectRate);

        projectRate = projectRateRepository.saveAndFlush(projectRate);
        if (BooleanUtils.toBoolean(projectRate.getFinished())) {
            ProjectEvent projectEvent = ProjectEvent.rateEvent();
            projectEvent.setRateId(projectRate.getId());
            projectEvent.projectId(projectRate.getProject().getId());
            eventBus.notify("rate", Event.wrap(projectEvent));
        }
        ProjectRateDTO result = projectRateMapper.projectRateToProjectRateDTO(projectRate);
        return result;
    }

    /**
     *  Get all the projectRates.
     *
     *  @param sort
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ProjectRateDTO> findAll(Sort sort) {
        log.debug("Request to get all ProjectRates");
        return projectRateRepository.findAll(sort).stream()
            .map(projectRateMapper::projectRateToProjectRateDTO)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the projectRates.
     *
     *  @param predicate
     *  @param sort
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ProjectRateDTO> findAll(Predicate predicate, Sort sort) {
        log.debug("Request to get all ProjectRates");
        return StreamSupport.stream(projectRateRepository.findAll(predicate, sort).spliterator(), true)
            .map(projectRateMapper::projectRateToProjectRateDTO)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one projectRate by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ProjectRateDTO findOne(Long id) {
        log.debug("Request to get ProjectRate : {}", id);
        ProjectRate projectRate = projectRateRepository.findOne(id);
        ProjectRateDTO projectRateDTO = projectRateMapper.projectRateToProjectRateDTO(projectRate);
        return projectRateDTO;
    }

    /**
     *  Delete the  projectRate by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ProjectRate : {}", id);
        projectRateRepository.delete(id);
    }

    @Override
    public Map<String, Boolean> getAllProjectRatePrivilegesOfEmployee(Long employeeId) throws IOException {
        if (employeeId == null) {
            return new HashMap<>();
        }

        Employee employee = employeeRepository.findOne(employeeId);
        if (employee == null) {
            return new HashMap<>();
        }
        Resource resource = appContext.getResource("classpath:project_rate_privilege.txt");
        InputStream inputStream = resource.getInputStream();
        String[] lines = IOUtils.toString(inputStream, "UTF-8").split("\n");



        Map<String, Boolean> result = new HashMap<>();
        String[] ops = lines[0].split(",");
        for (int i = 1; i < lines.length; i++) {
            String[] ps = lines[i].split(",");
            if (employee.hasTitle(ps[0])) {
                for (int j = 1; j < ps.length; j++) {
                    result.put(ops[j], ps[j].trim().equalsIgnoreCase("1"));
                }
            }
        }

        return result;
    }


    /**
     *  Close rate of project
     * @param id the project id
     */
    @Override
    public void closeProjectRates(Long id) {
        if (id == null) {
            return;
        }

        QProjectRate projectRate = QProjectRate.projectRate;
        Predicate predicate = projectRate.project.id.eq(id);
        Employee employee = userService.currentLoginEmployee();
        projectRateRepository.findAll(predicate)
        .forEach(p -> {
            p.setFinished(true);
            p.setFinishTime(System.currentTimeMillis());
            p.setClosed(true);
            p.setClosedBy(employee);
            projectRateRepository.save(p);
        });

        Project project = projectRepository.findOne(id);
        if (project != null) {
            project.setAllRatesDone(true);
            project.setBonusStatus(ProjectBonusStatus.IN_PROGRESS);
            project.setRateClosed(true);
            projectRepository.saveAndFlush(project);
        }

    }
}
