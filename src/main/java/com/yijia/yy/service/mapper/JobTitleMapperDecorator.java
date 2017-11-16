package com.yijia.yy.service.mapper;


import com.yijia.yy.domain.JobTitle;
import com.yijia.yy.service.dto.JobTitleDTO;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class JobTitleMapperDecorator implements JobTitleMapper{

    @Inject
    @Qualifier("delegate")
    private JobTitleMapper delegate;

    @Override
    public JobTitleDTO jobTitleToJobTitleDTO(JobTitle jobTitle) {
        if (jobTitle == null) {
            return null;
        }
        JobTitleDTO dto = delegate.jobTitleToJobTitleDTO(jobTitle);
        if (jobTitle.getSubordinates() != null) {
            dto.setSubordinateIds(
                jobTitle.getSubordinates().stream().map(t -> t.getId()).collect(Collectors.toSet())
            );
        } else {
            dto.setSubordinateIds(null);
        }
        return dto;
    }

    @Override
    public List<JobTitleDTO> jobTitlesToJobTitleDTOs(List<JobTitle> jobTitles) {
        if (jobTitles == null) {
            return null;
        }
        return jobTitles.stream()
            .map(t -> jobTitleToJobTitleDTO(t))
            .collect(Collectors.toList());
    }

    @Override
    public JobTitle jobTitleDTOToJobTitle(JobTitleDTO jobTitleDTO) {
        if (jobTitleDTO == null) {
            return null;
        }
        JobTitle jobTitle = delegate.jobTitleDTOToJobTitle(jobTitleDTO);
        if (jobTitleDTO.getSubordinateIds() != null) {
            jobTitle.setSubordinates(
                jobTitleDTO.getSubordinateIds().stream()
                .map(id -> new JobTitle().withId(id))
                .collect(Collectors.toSet())
            );
        }
        return jobTitle;
    }

    @Override
    public List<JobTitle> jobTitleDTOsToJobTitles(List<JobTitleDTO> jobTitleDTOs) {
        if (jobTitleDTOs == null) {
            return null;
        }
        return jobTitleDTOs.stream()
            .map(t -> jobTitleDTOToJobTitle(t))
            .collect(Collectors.toList());
    }
}
