package com.yijia.yy.service;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.service.dto.MeetingDTO;
import org.springframework.data.domain.Sort;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Meeting.
 */
public interface MeetingService {

    /**
     * Save a meeting.
     *
     * @param meetingDTO the entity to save
     * @return the persisted entity
     */
    MeetingDTO save(MeetingDTO meetingDTO);

    /**
     *  Get all the meetings.
     *
     *  @return the list of entities
     */
    List<MeetingDTO> findAll(Sort sort);

    List<MeetingDTO> findAll(Predicate predicate, Sort sort);

    /**
     *  Get the "id" meeting.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    MeetingDTO findOne(Long id);

    /**
     *  Delete the "id" meeting.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
