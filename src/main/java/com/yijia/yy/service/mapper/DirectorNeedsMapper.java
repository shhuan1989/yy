package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.*;
import com.yijia.yy.service.dto.DirectorNeedsDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity DirectorNeeds and its DTO DirectorNeedsDTO.
 */
@Mapper(componentModel = "spring", uses = {DirectorNeedsItemMapper.class, DirectorNeedsCommentMapper.class})
public interface DirectorNeedsMapper {

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "creator.id", target = "creatorId")
    @Mapping(source = "creator.name", target = "creatorName")
    @Mapping(source = "project.name", target = "projectName")
    @Mapping(source = "project.idNumber", target = "projectIdNumber")
    DirectorNeedsDTO directorNeedsToDirectorNeedsDTO(DirectorNeeds directorNeeds);

    List<DirectorNeedsDTO> directorNeedsToDirectorNeedsDTOs(List<DirectorNeeds> directorNeeds);

    @Mapping(source = "projectId", target = "project")
    @Mapping(source = "creatorId", target = "creator.id")
    @Mapping(source = "creatorName", target = "creator.name")
    @Mapping(source = "projectName", target = "project.name")
    @Mapping(source = "projectIdNumber", target = "project.idNumber")
    DirectorNeeds directorNeedsDTOToDirectorNeeds(DirectorNeedsDTO directorNeedsDTO);

    List<DirectorNeeds> directorNeedsDTOsToDirectorNeeds(List<DirectorNeedsDTO> directorNeedsDTOs);

    default Project projectFromId(Long id) {
        if (id == null) {
            return null;
        }
        Project project = new Project();
        project.setId(id);
        return project;
    }
}
