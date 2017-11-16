package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.*;
import com.yijia.yy.service.dto.JobTitleDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity JobTitle and its DTO JobTitleDTO.
 */
@Mapper(componentModel = "spring", uses = {})
@DecoratedWith(JobTitleMapperDecorator.class)
public interface JobTitleMapper {

    @Mapping(target = "subordinateIds", ignore = true)
    @Mapping(target = "leaderId", source = "leader.id")
    JobTitleDTO jobTitleToJobTitleDTO(JobTitle jobTitle);

    List<JobTitleDTO> jobTitlesToJobTitleDTOs(List<JobTitle> jobTitles);

    @Mapping(target = "subordinates", ignore = true)
    @Mapping(target = "leader.id", source = "leaderId")
    JobTitle jobTitleDTOToJobTitle(JobTitleDTO jobTitleDTO);

    List<JobTitle> jobTitleDTOsToJobTitles(List<JobTitleDTO> jobTitleDTOs);
}
