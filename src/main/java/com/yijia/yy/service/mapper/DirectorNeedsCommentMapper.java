package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.DirectorNeedsComment;
import com.yijia.yy.domain.Employee;
import com.yijia.yy.service.dto.DirectorNeedsCommentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity DirectorNeedsComment and its DTO DirectorNeedsCommentDTO.
 */
@Mapper(componentModel = "spring", uses = {EmployeeMapper.class})
public interface DirectorNeedsCommentMapper {

    @Mapping(source = "directorNeeds.id", target = "directorNeedsId")
    DirectorNeedsCommentDTO directorNeedsCommentToDirectorNeedsCommentDTO(DirectorNeedsComment directorNeedsComment);

    List<DirectorNeedsCommentDTO> directorNeedsCommentsToDirectorNeedsCommentDTOs(List<DirectorNeedsComment> directorNeedsComments);

    @Mapping(source = "directorNeedsId", target = "directorNeeds.id")
    DirectorNeedsComment directorNeedsCommentDTOToDirectorNeedsComment(DirectorNeedsCommentDTO directorNeedsCommentDTO);

    List<DirectorNeedsComment> directorNeedsCommentDTOsToDirectorNeedsComments(List<DirectorNeedsCommentDTO> directorNeedsCommentDTOs);

    default Employee creatorFromId(Long id) {
        if (id == null) {
            return null;
        }
        Employee creator = new Employee();
        creator.setId(id);
        return creator;
    }
}
