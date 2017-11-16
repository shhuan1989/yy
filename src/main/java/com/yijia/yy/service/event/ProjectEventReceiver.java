package com.yijia.yy.service.event;

import com.codahale.metrics.annotation.Timed;
import com.yijia.yy.domain.*;
import com.yijia.yy.domain.enumeration.ProjectBonusStatus;
import com.yijia.yy.domain.enumeration.ProjectRateStatus;
import com.yijia.yy.domain.enumeration.ProjectTimelineEventType;
import com.yijia.yy.domain.enumeration.TaskStatus;
import com.yijia.yy.repository.*;
import com.yijia.yy.service.ProjectRateService;
import com.yijia.yy.service.ProjectService;
import com.yijia.yy.service.mapper.ProjectMapper;
import com.yijia.yy.service.util.DomainObjectUtils;
import io.jsonwebtoken.lang.Collections;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.xpath.operations.Bool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.bus.Event;
import reactor.fn.Consumer;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Message receiver for project management related message
 */

@Service
@Qualifier("project")
public class ProjectEventReceiver implements Consumer<Event<ProjectEvent>>{

    private final Logger log = LoggerFactory.getLogger(ProjectEventReceiver.class);

    @Inject
    private ProjectService projectService;

    @Inject
    private ProjectMapper projectMapper;

    @Inject
    private ProjectRepository projectRepository;

    @Inject
    private TaskRepository taskRepository;

    @Inject
    private ProjectTimelineRepository timelineRepository;

    @Inject
    private ProjectRateRepository rateRepository;

    @Inject
    private JobTitleRepository jobTitleRepository;

    @Inject
    private ProjectRateService rateService;

    @Inject
    private EmployeeRepository employeeRepository;

    @Override
    @Timed
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    public void accept(Event<ProjectEvent> eventWrapper) {
        log.info("Received project event", eventWrapper);
        ProjectEvent event = eventWrapper.getData();
        if (event == null) {
            return;
        }

        Long projectId = event.getProjectId();
        if (projectId == null) {
            return;
        }

        Project project = projectMapper.projectDTOToProject(projectService.findOne(projectId));

        if (project == null) {
            return;
        }

        ProjectTimeline timeline = new ProjectTimeline();
        timeline.setCreateTime(System.currentTimeMillis());
        timeline.setProject(project);

        switch (event.getCategory()) {
            case PROJECT:
                timeline.setType(ProjectTimelineEventType.PROJECT);
                if (project.getLastModifier() == null) {
                    return;
                }
                timeline.setCreator(project.getLastModifier());
                switch (project.getStatus()) {
                    case INCOMPLETE:
                        timeline.setInfo(project.getLastModifier().getName() + " 创建了项目 " + project.getName());
                        break;
                    case ARCHIVED:
                        timeline.setInfo(project.getLastModifier().getName() + " 归档了项目 " + project.getName());
                        project.setRateStatus(ProjectRateStatus.IN_PROGRESS);
                        rateRepository.delete(rateRepository.findAllByProjectId(projectId));
                        sendRateNotificationToMembers(project.getFirstLevelRateMembers(), project, false);
                        ProjectRate rate = new ProjectRate();
                        rate.setProject(project);
                        rate.setAvg(true);
                        rate.setCreateTime(System.currentTimeMillis());
                        rateRepository.saveAndFlush(rate);
                        break;
                    case CLOSED:
                        timeline.setInfo(project.getLastModifier().getName() + " 关闭了项目 " + project.getName());
                        break;
                    default:
                        timeline.setInfo("项目 " + project.getName() + " 的状态更新了");
                        break;
                }
                timelineRepository.save(timeline);
                break;
            case RATE:
                List<ProjectRate> rates = rateRepository.findAllByProjectId(projectId);
                if (rates == null || project.getAllMembers() == null) {
                    return;
                }
                // calculate average rate
                // and send notification to director if all members finished rating
                Map<Long, ProjectRate> userRates = rates.stream()
                    .filter(r -> r.getOwner() != null)
                    .collect(Collectors.toMap(r -> r.getOwner().getId(), r -> r));
                boolean membersDone = project.getFirstLevelRateMembers().stream()
                    .allMatch(m -> {
                        ProjectRate r = userRates.get(m.getId());
                        return r == null || BooleanUtils.toBoolean(r.getFinished());
                    }
                );
                boolean auditStarted = project.getSecondLevelRateMembers().stream().anyMatch(m -> userRates.get(m.getId()) != null);
                if (!auditStarted) {
                    if (membersDone) {
                        rates.stream().filter(r -> BooleanUtils.toBoolean(r.getAvg())).findAny()
                            .ifPresent( avgRage ->
                            {
                                rates.stream().filter(r -> !BooleanUtils.toBoolean(r.getAvg()) && r.getManagement() != null)
                                    .mapToInt(r -> r.getManagement())
                                    .average().ifPresent(d -> avgRage.setManagement(Math.toIntExact(Math.round(d))));

                                rates.stream().filter(r -> !BooleanUtils.toBoolean(r.getAvg()) && r.getCreation() != null)
                                    .mapToInt(r -> r.getCreation())
                                    .average().ifPresent(d -> avgRage.setCreation(Math.toIntExact(Math.round(d))));

                                rates.stream().filter(r -> !BooleanUtils.toBoolean(r.getAvg()) && r.getProduction() != null)
                                    .mapToInt(r -> r.getProduction())
                                    .average().ifPresent(d -> avgRage.setProduction(Math.toIntExact(Math.round(d))));

                                rates.stream().filter(r -> !BooleanUtils.toBoolean(r.getAvg()) && r.getDirect() != null)
                                    .mapToInt(r -> r.getDirect())
                                    .average().ifPresent(d -> avgRage.setDirect(Math.toIntExact(Math.round(d))));

                                rates.stream().filter(r -> !BooleanUtils.toBoolean(r.getAvg()) && r.getPhotography() != null)
                                    .mapToInt(r -> r.getPhotography())
                                    .average().ifPresent(d -> avgRage.setPhotography(Math.toIntExact(Math.round(d))));

                                rates.stream().filter(r -> !BooleanUtils.toBoolean(r.getAvg()) && r.getPostprocess() != null)
                                    .mapToInt(r -> r.getPostprocess())
                                    .average().ifPresent(d -> avgRage.setPostprocess(Math.toIntExact(Math.round(d))));

                                DomainObjectUtils.syncRateScoreAndDesc(avgRage);
                                rateRepository.saveAndFlush(avgRage);
                            });

                        sendRateNotificationToMembers(project.getSecondLevelRateMembers(), project, true);
                    }
                } else {
                    boolean auditDone = project.getSecondLevelRateMembers().stream().allMatch(m -> {
                        ProjectRate r = userRates.get(m.getId());
                        return r == null || BooleanUtils.toBoolean(r.getFinished());
                    });
                    if (auditDone) {
                        project.setAllRatesDone(true);
                        // start performance approvals
                        project.setRateClosed(true);
                        project.setBonusStatus(ProjectBonusStatus.IN_PROGRESS);
                        projectRepository.saveAndFlush(project);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void sendRateNotificationToMembers(Set<Employee> members, Project project, Boolean isAudit) {
        if (members != null && project != null) {
            members.forEach(m -> sendRateNotificationToMember(m, project, isAudit));
        }
    }

    private void sendRateNotificationToMember(Employee member, Project project, Boolean isAudit) {
        if (member != null && project != null) {
            log.info("Send project rate notification for project " + project.getName() + " to employee " + member.getName() +" - " + member.getId());
            ProjectRate rate = new ProjectRate();
            rate.setFinished(false);
            rate.setOwner(member);
            rate.setCreateTime(System.currentTimeMillis());
            rate.setProject(project);
            rate.setAudit(isAudit);

            try {
                Map<String, Boolean> privileges = rateService.getAllProjectRatePrivilegesOfEmployee(member.getId());
                if (BooleanUtils.toBoolean(privileges.get("项目管理质量"))) {
                    rate.setShouldRateManagement(true);
                }
                if (BooleanUtils.toBoolean(privileges.get("创意质量"))) {
                    rate.setShouldRateCreation(true);
                }
                if (BooleanUtils.toBoolean(privileges.get("制片质量"))) {
                    rate.setShouldRateProduction(true);
                }
                if (BooleanUtils.toBoolean(privileges.get("导演质量"))) {
                    rate.setShouldRateDirect(true);
                }
                if (BooleanUtils.toBoolean(privileges.get("摄像质量"))) {
                    rate.setShouldRatePhotography(true);
                }
                if (BooleanUtils.toBoolean(privileges.get("后期设计质量"))) {
                    rate.setShouldRatePostprocess(true);
                }
                if (BooleanUtils.toBoolean(privileges.get("终审"))) {
                    rate.setShouldFinalRate(true);
                }
            } catch (IOException e) {
                log.warn("Error to read rate privileges", e);
            }

            rateRepository.save(rate);
        }
    }
}
