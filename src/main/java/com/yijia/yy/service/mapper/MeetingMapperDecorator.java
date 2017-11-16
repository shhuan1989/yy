package com.yijia.yy.service.mapper;


import com.yijia.yy.domain.Employee;
import com.yijia.yy.domain.Meeting;
import com.yijia.yy.domain.converter.MeetingStatusConverter;
import com.yijia.yy.service.dto.DictionaryDTO;
import com.yijia.yy.service.dto.MeetingDTO;
import com.yijia.yy.service.util.DomainObjectUtils;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class MeetingMapperDecorator implements MeetingMapper {

    @Inject
    @Qualifier("delegate")
    private MeetingMapper delegate;

    @Override
    public MeetingDTO meetingToMeetingDTO(Meeting meeting) {
        if (meeting == null) {
            return null;
        }

        MeetingDTO dto = delegate.meetingToMeetingDTO(meeting);

        dto.setStatus(
            new DictionaryDTO()
                .withId(Long.valueOf(meeting.getStatus().ordinal()))
                .withName(meeting.getStatus().toString())
        );

        if (meeting.getMembers() != null) {
            dto.setMemberIds(
                meeting.getMembers().stream()
                .map(m -> m.getId())
                .collect(Collectors.toSet())
            );
            dto.setMemberNames(
                meeting.getMembers().stream()
                .map(m -> m.getName())
                .collect(Collectors.toSet())
            );
        }

        if (meeting.getInfo() != null) {
            dto.setInfo(new String(meeting.getInfo(), StandardCharsets.UTF_8));
        }

        return dto;
    }

    @Override
    public List<MeetingDTO> meetingsToMeetingDTOs(List<Meeting> meetings) {
        if (meetings == null) {
            return null;
        }

        return meetings.stream()
            .map(m -> meetingToMeetingDTO(m))
            .collect(Collectors.toList());
    }

    @Override
    public Meeting meetingDTOToMeeting(MeetingDTO meetingDTO) {
        if (meetingDTO == null) {
            return null;
        }

        Meeting meeting = delegate.meetingDTOToMeeting(meetingDTO);
        if (DomainObjectUtils.dictionaryDtoIsNotNull(meetingDTO.getStatus())) {
            meeting.setStatus(
                new MeetingStatusConverter().convertToEntityAttribute(
                    Math.toIntExact(meetingDTO.getStatus().getId())
                )
            );
        }

        if (meetingDTO.getMemberIds() != null) {
            meeting.setMembers(
                meetingDTO.getMemberIds().stream()
                .map(m -> new Employee().id(m))
                .collect(Collectors.toSet())
            );
        }

        if (meetingDTO.getInfo() != null) {
            meeting.setInfo(meetingDTO.getInfo().getBytes(StandardCharsets.UTF_8));
        }
        return meeting;
    }

    @Override
    public List<Meeting> meetingDTOsToMeetings(List<MeetingDTO> meetingDTOs) {
        if (meetingDTOs == null) {
            return null;
        }

        return meetingDTOs.stream()
            .map(m -> meetingDTOToMeeting(m))
            .collect(Collectors.toList());
    }
}
