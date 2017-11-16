package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.*;
import com.yijia.yy.service.dto.NoticeChatDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity NoticeChat and its DTO NoticeChatDTO.
 */
@Mapper(componentModel = "spring", uses = {EmployeeMapper.class, FileInfoMapper.class, PictureInfoMapper.class})
public interface NoticeChatMapper {

    @Mapping(source = "notice.id", target = "noticeId")
    NoticeChatDTO noticeChatToNoticeChatDTO(NoticeChat noticeChat);

    List<NoticeChatDTO> noticeChatsToNoticeChatDTOs(List<NoticeChat> noticeChats);

    @Mapping(source = "noticeId", target = "notice")
    NoticeChat noticeChatDTOToNoticeChat(NoticeChatDTO noticeChatDTO);

    List<NoticeChat> noticeChatDTOsToNoticeChats(List<NoticeChatDTO> noticeChatDTOs);

    default Employee employeeFromId(Long id) {
        if (id == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(id);
        return employee;
    }

    default Notice noticeFromId(Long id) {
        if (id == null) {
            return null;
        }
        Notice notice = new Notice();
        notice.setId(id);
        return notice;
    }
}
