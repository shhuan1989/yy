package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.*;
import com.yijia.yy.service.dto.NoticeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Notice and its DTO NoticeDTO.
 */
@Mapper(componentModel = "spring", uses = {DeptMapper.class, ProjectMapper.class, EmployeeMapper.class, NoticeChatMapper.class})
@DecoratedWith(NoticeMapperDecorator.class)
public interface NoticeMapper {

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "project.name", target = "projectName")
    @Mapping(source = "creator.id", target = "creatorId")
    @Mapping(target = "content", ignore = true)
    NoticeDTO noticeToNoticeDTO(Notice notice);

    List<NoticeDTO> noticesToNoticeDTOs(List<Notice> notices);

    @Mapping(source = "projectId", target = "project.id")
    @Mapping(source = "creatorId", target = "creator.id")
    @Mapping(target = "content", ignore = true)
    Notice noticeDTOToNotice(NoticeDTO noticeDTO);

    List<Notice> noticeDTOsToNotices(List<NoticeDTO> noticeDTOs);

    default Project projectFromId(Long id) {
        if (id == null) {
            return null;
        }
        Project project = new Project();
        project.setId(id);
        return project;
    }

    default Employee employeeFromId(Long id) {
        if (id == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(id);
        return employee;
    }

    default Dept deptFromId(Long id) {
        if (id == null) {
            return null;
        }
        Dept dept = new Dept();
        dept.setId(id);
        return dept;
    }
}
