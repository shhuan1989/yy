package com.yijia.yy.service;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.dto.NoticeDTO;
import org.springframework.data.domain.Sort;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Notice.
 */
public interface NoticeService {

    /**
     * Save a notice.
     *
     * @param noticeDTO the entity to save
     * @return the persisted entity
     */
    NoticeDTO save(NoticeDTO noticeDTO);

    /**
     *  Get all the notices.
     *
     *  @param sort the order
     *  @return the list of entities
     */
    List<NoticeDTO> findAll(Sort sort);

    /**
     *  Get all the notices.
     *
     *  @param predicate the predicate
     *  @param sort the order
     *  @return the list of entities
     */
    List<NoticeDTO> findAll(Predicate predicate, Sort sort);

    /**
     *  Get the "id" notice.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    NoticeDTO findOne(Long id);

    /**
     *  Delete the "id" notice.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
