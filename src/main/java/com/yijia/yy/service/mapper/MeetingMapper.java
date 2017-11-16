package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.Employee;
import com.yijia.yy.domain.Meeting;
import com.yijia.yy.domain.Project;
import com.yijia.yy.domain.Room;
import com.yijia.yy.service.dto.MeetingDTO;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity Meeting and its DTO MeetingDTO.
 */
@Mapper(componentModel = "spring", uses = {EmployeeMapper.class, DictionaryMapper.class})
@DecoratedWith(MeetingMapperDecorator.class)
public interface MeetingMapper {

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "memberIds", ignore = true)
    @Mapping(target = "memberNames", ignore = true)
    @Mapping(target = "info", ignore = true)
    MeetingDTO meetingToMeetingDTO(Meeting meeting);

    List<MeetingDTO> meetingsToMeetingDTOs(List<Meeting> meetings);

    @Mapping(source = "projectId", target = "project")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "info", ignore = true)
    Meeting meetingDTOToMeeting(MeetingDTO meetingDTO);

    List<Meeting> meetingDTOsToMeetings(List<MeetingDTO> meetingDTOs);

    default Employee employeeFromId(Long id) {
        if (id == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(id);
        return employee;
    }

    default Room roomFromId(Long id) {
        if (id == null) {
            return null;
        }
        Room room = new Room();
        room.setId(id);
        return room;
    }

    default Project projectFromId(Long id) {
        if (id == null) {
            return null;
        }
        Project project = new Project();
        project.setId(id);
        return project;
    }
}
