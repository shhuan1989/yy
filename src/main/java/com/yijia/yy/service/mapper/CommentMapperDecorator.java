package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.Comment;
import com.yijia.yy.domain.FileInfo;
import com.yijia.yy.service.dto.CommentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.stream.Collectors;

public class CommentMapperDecorator implements CommentMapper{
    @Autowired
    @Qualifier("delegate")
    private CommentMapper delegate;

    @Override
    public CommentDTO commentToCommentDTO(Comment comment) {
        if (comment == null) {
            return null;
        }
        CommentDTO dto = delegate.commentToCommentDTO(comment);

        return dto;
    }

    @Override
    public List<CommentDTO> commentsToCommentDTOs(List<Comment> comments) {
        if (comments == null) {
            return null;
        }
        return delegate.commentsToCommentDTOs(comments);
    }

    @Override
    public Comment commentDTOToComment(CommentDTO commentDTO) {
        if (commentDTO == null) {
            return null;
        }

        Comment comment = delegate.commentDTOToComment(commentDTO);
        if (commentDTO.getFileInfo().getId() == null) {
            comment.setFileInfo(null);
        }

        if (commentDTO.getPictureInfo().getId() == null) {
            comment.setPictureInfo(null);
        }

        return comment;
    }

    @Override
    public List<Comment> commentDTOsToComments(List<CommentDTO> commentDTOs) {
        if (commentDTOs != null) {
            return null;
        }
        return commentDTOs.stream()
            .map(c -> commentDTOToComment(c))
            .collect(Collectors.toList());
    }
}
