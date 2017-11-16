package com.yijia.yy.service.mapper;

import com.yijia.yy.domain.Notice;
import com.yijia.yy.service.dto.NoticeDTO;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by palad on 2017/2/24.
 */
public class NoticeMapperDecorator implements NoticeMapper {

    @Inject
    @Qualifier("delegate")
    private NoticeMapper delegate;

    @Override
    public NoticeDTO noticeToNoticeDTO(Notice notice) {
        if (notice == null) {
            return null;
        }

        NoticeDTO dto = delegate.noticeToNoticeDTO(notice);
        if (notice.getContent() != null) {
            dto.setContent(new String(notice.getContent(), StandardCharsets.UTF_8));
        }

        return dto;
    }

    @Override
    public List<NoticeDTO> noticesToNoticeDTOs(List<Notice> notices) {
        if (notices == null) {
            return null;
        }

        return notices.stream()
            .map(o -> noticeToNoticeDTO(o))
            .collect(Collectors.toList());
    }

    @Override
    public Notice noticeDTOToNotice(NoticeDTO noticeDTO) {
        if (noticeDTO == null) {
            return null;
        }

        Notice notice = delegate.noticeDTOToNotice(noticeDTO);
        if (noticeDTO.getContent() != null) {
            notice.setContent(noticeDTO.getContent().getBytes(StandardCharsets.UTF_8));
        }

        return notice;
    }

    @Override
    public List<Notice> noticeDTOsToNotices(List<NoticeDTO> noticeDTOs) {
        if (noticeDTOs == null) {
            return null;
        }

        return noticeDTOs.stream()
            .map(o -> noticeDTOToNotice(o))
            .collect(Collectors.toList());
    }
}
