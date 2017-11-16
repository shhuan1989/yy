package com.yijia.yy.service.impl;

import com.yijia.yy.repository.FileInfoRepository;
import com.yijia.yy.repository.PictureInfoRepository;
import com.yijia.yy.service.NoticeChatService;
import com.yijia.yy.domain.NoticeChat;
import com.yijia.yy.repository.NoticeChatRepository;
import com.yijia.yy.service.UserService;
import com.yijia.yy.service.dto.NoticeChatDTO;
import com.yijia.yy.service.mapper.NoticeChatMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing NoticeChat.
 */
@Service
@Transactional
public class NoticeChatServiceImpl implements NoticeChatService{

    private final Logger log = LoggerFactory.getLogger(NoticeChatServiceImpl.class);

    @Inject
    private NoticeChatRepository noticeChatRepository;

    @Inject
    private NoticeChatMapper noticeChatMapper;

    @Inject
    private UserService userService;

    @Inject
    private FileInfoRepository fileInfoRepository;

    @Inject
    private PictureInfoRepository pictureInfoRepository;

    /**
     * Save a noticeChat.
     *
     * @param noticeChatDTO the entity to save
     * @return the persisted entity
     */
    public NoticeChatDTO save(NoticeChatDTO noticeChatDTO) {
        log.debug("Request to save NoticeChat : {}", noticeChatDTO);
        NoticeChat noticeChat = noticeChatMapper.noticeChatDTOToNoticeChat(noticeChatDTO);

        if (noticeChat.getCreator() == null || noticeChat.getCreator().getId() == null) {
            noticeChat.setCreator(userService.currentLoginEmployee());
            noticeChat.setCreateTime(System.currentTimeMillis());
        }
        if (noticeChat.getPictureInfo() != null && noticeChat.getPictureInfo().getId() == null) {
            noticeChat.setPictureInfo(null);
        }
        if (noticeChat.getFileInfo() != null && noticeChat.getFileInfo().getId() == null) {
            noticeChat.setFileInfo(null);
        }

        noticeChat = noticeChatRepository.save(noticeChat);
        if (noticeChat.getPictureInfo() != null && noticeChat.getPictureInfo().getId() != null) {
            noticeChat.setPictureInfo(pictureInfoRepository.findOne(noticeChat.getPictureInfo().getId()));
        }
        if (noticeChat.getFileInfo() != null && noticeChat.getFileInfo().getId() != null) {
            noticeChat.setFileInfo(fileInfoRepository.findOne(noticeChat.getFileInfo().getId()));
        }
        NoticeChatDTO result = noticeChatMapper.noticeChatToNoticeChatDTO(noticeChat);
        return result;
    }

    /**
     *  Get all the noticeChats.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<NoticeChatDTO> findAll() {
        log.debug("Request to get all NoticeChats");
        List<NoticeChatDTO> result = noticeChatRepository.findAll().stream()
            .map(noticeChatMapper::noticeChatToNoticeChatDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }


    /**
     *  get all the noticeChats where PictureInfo is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<NoticeChatDTO> findAllWherePictureInfoIsNull() {
        log.debug("Request to get all noticeChats where PictureInfo is null");
        return StreamSupport
            .stream(noticeChatRepository.findAll().spliterator(), false)
            .filter(noticeChat -> noticeChat.getPictureInfo() == null)
            .map(noticeChatMapper::noticeChatToNoticeChatDTO)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     *  get all the noticeChats where FileInfo is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<NoticeChatDTO> findAllWhereFileInfoIsNull() {
        log.debug("Request to get all noticeChats where FileInfo is null");
        return StreamSupport
            .stream(noticeChatRepository.findAll().spliterator(), false)
            .filter(noticeChat -> noticeChat.getFileInfo() == null)
            .map(noticeChatMapper::noticeChatToNoticeChatDTO)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one noticeChat by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public NoticeChatDTO findOne(Long id) {
        log.debug("Request to get NoticeChat : {}", id);
        NoticeChat noticeChat = noticeChatRepository.findOne(id);
        NoticeChatDTO noticeChatDTO = noticeChatMapper.noticeChatToNoticeChatDTO(noticeChat);
        return noticeChatDTO;
    }

    /**
     *  Delete the  noticeChat by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete NoticeChat : {}", id);
        noticeChatRepository.delete(id);
    }
}
