package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.Contract;
import com.yijia.yy.domain.Project;
import com.yijia.yy.domain.converter.*;
import com.yijia.yy.domain.enumeration.ProjectStatus;
import com.yijia.yy.service.dto.DictionaryDTO;
import com.yijia.yy.service.dto.ProjectDTO;
import com.yijia.yy.service.util.DomainObjectUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectMapperDecorator implements ProjectMapper {

    @Inject
    @Qualifier("delegate")
    private ProjectMapper delegate;

    @Override
    public ProjectDTO projectToProjectDTO(Project project) {
        if (project == null) {
            return null;
        }

        if (project.getContract() != null) {
            project.getContract().setProject(null);
        }

        ProjectDTO dto = delegate.projectToProjectDTO(project);
        if (project.getIsNewClient() != null) {
            dto.setIsNewClient(
                new DictionaryDTO().
                    withId(Long.valueOf(project.getIsNewClient().ordinal()))
                    .withName(project.getIsNewClient().toString())
            );
        }

        if (project.getStatus() != null) {
            dto.setStatus(
                new DictionaryDTO()
                .withId(Long.valueOf(project.getStatus().ordinal()))
                .withName(project.getStatus().toString())
            );
        }

        if (project.getHasContract() != null) {
            dto.setHasContract(
                new DictionaryDTO()
                    .withId(Long.valueOf(project.getHasContract().ordinal()))
                    .withName(project.getHasContract().toString())
            );
        }

        if (project.getRateStatus() != null) {
            dto.setRateStatus(
                new DictionaryDTO()
                .withId(Long.valueOf(project.getRateStatus().ordinal()))
                .withName(project.getRateStatus().toString())
            );
        }

        if (project.getBonusStatus() != null) {
            dto.setBonusStatus(
                new DictionaryDTO()
                .withId(Long.valueOf(project.getBonusStatus().ordinal()))
                .withName(project.getBonusStatus().toString())
            );
        }

        Integer participantsCount = 0;
        if (project.getDirector() != null) {
            participantsCount += 1;
        }
        if (project.getAes() != null) {
            participantsCount += project.getAes().size();
        }
        if (project.getMembers() != null) {
            participantsCount += project.getMembers().size();
        }
        dto.setParticipantsCount(participantsCount);

        if (project.getBonusApprovals() != null) {
            dto.setBonusIssued(
                project.getBonusApprovals().stream().anyMatch(b -> BooleanUtils.toBoolean(b.getIssued()))
            );
        }

        return dto;
    }

    @Override
    public List<ProjectDTO> projectsToProjectDTOs(List<Project> projects) {
        if (projects == null) {
            return null;
        }
        return projects.stream().map(p -> projectToProjectDTO(p))
            .collect(Collectors.toList());
    }

    @Override
    public Project projectDTOToProject(ProjectDTO projectDTO) {
        if (projectDTO == null) {
            return null;
        }
        Project project = delegate.projectDTOToProject(projectDTO);
        if (DomainObjectUtils.dictionaryDtoIsNotNull(projectDTO.getIsNewClient())) {
            project.setIsNewClient(
                new BooleanEnumConverter().convertToEntityAttribute(
                    Math.toIntExact(projectDTO.getIsNewClient().getId())
                )
            );
        }
        if (DomainObjectUtils.dictionaryDtoIsNotNull(projectDTO.getStatus())) {
            project.setStatus(
                new ProjectStatusConverter().convertToEntityAttribute(
                    Math.toIntExact(projectDTO.getStatus().getId())
                )
            );
        }
        if (DomainObjectUtils.dictionaryDtoIsNotNull(projectDTO.getHasContract())) {
            project.setHasContract(
                new HasEnumConverter().convertToEntityAttribute(
                    Math.toIntExact(projectDTO.getHasContract().getId())
                )
            );
        }

        if (DomainObjectUtils.dictionaryDtoIsNotNull(projectDTO.getRateStatus())) {
            project.setRateStatus(
                new ProjectRateStatusConverter().convertToEntityAttribute(
                    Math.toIntExact(projectDTO.getRateStatus().getId())
                )
            );
        }

        if (DomainObjectUtils.dictionaryDtoIsNotNull(projectDTO.getBonusStatus())) {
            project.setBonusStatus(
                new ProjectBonusStatusConverter().convertToEntityAttribute(
                    Math.toIntExact(projectDTO.getBonusStatus().getId())
                )
            );
        }

        if (projectDTO.getContract() == null || projectDTO.getContract().getId() == null) {
            projectDTO.setContract(null);
        }
        if (projectDTO.getClient() == null || projectDTO.getClient().getId() == null) {
            project.setClient(null);
        }
        if (projectDTO.getDirector() == null || projectDTO.getDirector().getId() == null) {
            project.setDirector(null);
        }
        if (projectDTO.getLastModifier() == null || projectDTO.getLastModifier().getId() == null) {
            project.setLastModifier(null);
        }

        return project;
    }

    @Override
    public List<Project> projectDTOsToProjects(List<ProjectDTO> projectDTOs) {
        if (projectDTOs == null) {
            return null;
        }
        return projectDTOs.stream().map(p -> projectDTOToProject(p))
            .collect(Collectors.toList());
    }
}
