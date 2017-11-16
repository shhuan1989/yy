package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.*;
import com.yijia.yy.service.dto.CommentDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Comment and its DTO CommentDTO.
 */
@Mapper(componentModel = "spring", uses = {EmployeeMapper.class, PictureInfoMapper.class, FileInfoMapper.class})
@DecoratedWith(CommentMapperDecorator.class)
public interface CommentMapper {

    @Mapping(source = "task.id", target = "taskId")
    CommentDTO commentToCommentDTO(Comment comment);

    List<CommentDTO> commentsToCommentDTOs(List<Comment> comments);

    @Mapping(source = "taskId", target = "task")
    Comment commentDTOToComment(CommentDTO commentDTO);

    List<Comment> commentDTOsToComments(List<CommentDTO> commentDTOs);

    default Task taskFromId(Long id) {
        if (id == null) {
            return null;
        }
        return new Task().id(id);
    }
}
