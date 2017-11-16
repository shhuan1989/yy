package com.yijia.yy.service.impl;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.domain.FileInfo;
import com.yijia.yy.domain.PictureInfo;
import com.yijia.yy.domain.User;
import com.yijia.yy.repository.FileInfoRepository;
import com.yijia.yy.repository.PictureInfoRepository;
import com.yijia.yy.repository.UserRepository;
import com.yijia.yy.security.SecurityUtils;
import com.yijia.yy.service.CommentService;
import com.yijia.yy.domain.Comment;
import com.yijia.yy.repository.CommentRepository;
import com.yijia.yy.service.UserService;
import com.yijia.yy.service.dto.CommentDTO;
import com.yijia.yy.service.mapper.CommentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing Comment.
 */
@Service
@Transactional
public class CommentServiceImpl implements CommentService{

    private final Logger log = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Inject
    private CommentRepository commentRepository;

    @Inject
    private CommentMapper commentMapper;

    @Inject
    private UserService userService;

    @Inject
    private FileInfoRepository fileInfoRepository;

    @Inject
    private PictureInfoRepository pictureInfoRepository;

    /**
     * Save a comment.
     *
     * @param commentDTO the entity to save
     * @return the persisted entity
     */
    public CommentDTO save(CommentDTO commentDTO) {
        log.debug("Request to save Comment : {}", commentDTO);
        Comment comment = commentMapper.commentDTOToComment(commentDTO);
        if (comment.getId() == null) {
            comment.setCreateTime(System.currentTimeMillis());
            Optional<User> user = userService.currentLoginUser();
            if (user.isPresent()) {
                comment.setCreator(user.get().getEmployee());
            }
        }
        comment = commentRepository.save(comment);
        if (comment.getPictureInfo() != null && comment.getPictureInfo().getId() != null) {
            comment.setPictureInfo(pictureInfoRepository.findOne(comment.getPictureInfo().getId()));
        }
        if (comment.getFileInfo() != null && comment.getFileInfo().getId() != null) {
            comment.setFileInfo(fileInfoRepository.findOne(comment.getFileInfo().getId()));
        }
        return commentMapper.commentToCommentDTO(comment);
    }

    /**
     *  Get all the comments.
     *
     *  @param sort
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<CommentDTO> findAll(Sort sort) {
        log.debug("Request to get all Comments");
        List<CommentDTO> result = commentRepository.findAll(sort).stream()
            .map(commentMapper::commentToCommentDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get all the comments by predicate.
     *
     *  @param predicate
     *  @param sort
     *  @return the list of entities
     */
    @Transactional
    @Override
    public List<CommentDTO> findAll(Predicate predicate, Sort sort) {
        log.debug("Request to get all Comments");
        List<CommentDTO> result = StreamSupport.stream(commentRepository.findAll(predicate, sort).spliterator(), false)
            .map(commentMapper::commentToCommentDTO)
            .collect(Collectors.toCollection(LinkedList::new));
        return result;
    }

    /**
     *  Get one comment by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public CommentDTO findOne(Long id) {
        log.debug("Request to get Comment : {}", id);
        Comment comment = commentRepository.findOne(id);
        CommentDTO commentDTO = commentMapper.commentToCommentDTO(comment);
        return commentDTO;
    }

    /**
     *  Delete the  comment by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Comment : {}", id);
        commentRepository.delete(id);
    }
}
