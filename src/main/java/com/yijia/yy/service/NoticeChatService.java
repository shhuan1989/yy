package com.yijia.yy.service;

import com.yijia.yy.service.dto.NoticeChatDTO;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing NoticeChat.
 */
public interface NoticeChatService {

    /**
     * Save a noticeChat.
     *
     * @param noticeChatDTO the entity to save
     * @return the persisted entity
     */
    NoticeChatDTO save(NoticeChatDTO noticeChatDTO);

    /**
     *  Get all the noticeChats.
     *  
     *  @return the list of entities
     */
    List<NoticeChatDTO> findAll();
    /**
     *  Get all the NoticeChatDTO where PictureInfo is null.
     *
     *  @return the list of entities
     */
    List<NoticeChatDTO> findAllWherePictureInfoIsNull();
    /**
     *  Get all the NoticeChatDTO where FileInfo is null.
     *
     *  @return the list of entities
     */
    List<NoticeChatDTO> findAllWhereFileInfoIsNull();

    /**
     *  Get the "id" noticeChat.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    NoticeChatDTO findOne(Long id);

    /**
     *  Delete the "id" noticeChat.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
