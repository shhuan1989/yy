package com.yijia.yy.service.impl;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.domain.enumeration.MeetingStatus;
import com.yijia.yy.service.MeetingService;
import com.yijia.yy.domain.Meeting;
import com.yijia.yy.repository.MeetingRepository;
import com.yijia.yy.service.UserService;
import com.yijia.yy.service.dto.MeetingDTO;
import com.yijia.yy.service.mapper.MeetingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing Meeting.
 */
@Service
@Transactional
public class MeetingServiceImpl implements MeetingService{

    private final Logger log = LoggerFactory.getLogger(MeetingServiceImpl.class);

    @Inject
    private MeetingRepository meetingRepository;

    @Inject
    private MeetingMapper meetingMapper;

    @Inject
    private UserService userService;

    /**
     * Save a meeting.
     *
     * @param meetingDTO the entity to save
     * @return the persisted entity
     */
    public MeetingDTO save(MeetingDTO meetingDTO) {
        log.debug("Request to save Meeting : {}", meetingDTO);
        Meeting meeting = meetingMapper.meetingDTOToMeeting(meetingDTO);
        if (meeting.getId() == null) {
            meeting.setCreator(userService.currentLoginEmployee());
            meeting.setStatus(MeetingStatus.NOT_START);
        }
        meeting = meetingRepository.save(meeting);
        MeetingDTO result = meetingMapper.meetingToMeetingDTO(meeting);
        return result;
    }

    /**
     *  Get all the meetings.
     *
     *  @return the list of entities
     */
    @Transactional
    public List<MeetingDTO> findAll(Sort sort) {
        log.debug("Request to get all Meetings");

        List<Meeting> meetings = meetingRepository.findAll(sort);
        updateMeetings(meetings);

        List<MeetingDTO> result = meetings.stream()
            .map(meetingMapper::meetingToMeetingDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    @Transactional
    public List<MeetingDTO> findAll(Predicate predicate, Sort sort) {
        log.debug("Request to get all Meetings");
        List<Meeting> meetings = StreamSupport.stream(meetingRepository.findAll(predicate, sort).spliterator(), false)
            .collect(Collectors.toList());
        updateMeetings(meetings);
        List<MeetingDTO> result = meetings.stream()
            .map(meetingMapper::meetingToMeetingDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }


    private void updateMeetings(List<Meeting> meetings) {
        List<Meeting> updatableMeetings = meetings.stream()
            .filter(m -> m.getStatus() == MeetingStatus.NOT_START || m.getStatus() == MeetingStatus.IN_PROGRESS)
            .collect(Collectors.toList());

        Long ct = System.currentTimeMillis();
        updatableMeetings.forEach(m -> {
            if (ct > m.getEndTime()) {
                m.setStatus(MeetingStatus.FINISHED);
            } else if (ct > m.getStartTime()) {
                m.setStatus(MeetingStatus.IN_PROGRESS);
            }
        });
        meetingRepository.save(updatableMeetings);
    }

    /**
     *  Get one meeting by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public MeetingDTO findOne(Long id) {
        log.debug("Request to get Meeting : {}", id);
        Meeting meeting = meetingRepository.findOne(id);
        MeetingDTO meetingDTO = meetingMapper.meetingToMeetingDTO(meeting);
        return meetingDTO;
    }

    /**
     *  Delete the  meeting by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Meeting : {}", id);
        meetingRepository.delete(id);
    }
}
