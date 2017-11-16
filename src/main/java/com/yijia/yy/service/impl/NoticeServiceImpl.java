package com.yijia.yy.service.impl;

import com.querydsl.core.types.Predicate;
import com.yijia.yy.repository.DeptRepository;
import com.yijia.yy.service.NoticeService;
import com.yijia.yy.domain.Notice;
import com.yijia.yy.repository.NoticeRepository;
import com.yijia.yy.service.UserService;
import com.yijia.yy.service.dto.NoticeDTO;
import com.yijia.yy.service.mapper.NoticeMapper;
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
 * Service Implementation for managing Notice.
 */
@Service
@Transactional
public class NoticeServiceImpl implements NoticeService{

    private final Logger log = LoggerFactory.getLogger(NoticeServiceImpl.class);

    @Inject
    private NoticeRepository noticeRepository;

    @Inject
    private NoticeMapper noticeMapper;

    @Inject
    private UserService userService;

    @Inject
    private DeptRepository deptRepository;

    /**
     * Save a notice.
     *
     * @param noticeDTO the entity to save
     * @return the persisted entity
     */
    public NoticeDTO save(NoticeDTO noticeDTO) {
        log.debug("Request to save Notice : {}", noticeDTO);
        Notice notice = noticeMapper.noticeDTOToNotice(noticeDTO);
        if (notice.getId() == null) {
            notice.setCreateTime(System.currentTimeMillis());
            notice.setCreator(userService.currentLoginEmployee());
        }

        if (notice.getProject() != null && notice.getProject().getId() == null) {
            notice.setProject(null);
        }

        Boolean allDepts = notice.getDepts().stream().filter(d -> d.getId() == -1L).findAny().isPresent();
        if (allDepts) {
            notice.setDepts(
                deptRepository.findAll()
                .stream()
                .collect(Collectors.toSet())
            );
        }

        if (notice.getComments() != null) {
            notice.getComments().forEach(c -> {
                if (c.getFileInfo() != null && c.getFileInfo().getId() == null) {
                    c.setFileInfo(null);
                }
                if (c.getPictureInfo() != null && c.getPictureInfo().getId() == null) {
                    c.setPictureInfo(null);
                }
                if (c.getCreator() == null || c.getCreator().getId() == null) {
                    c.setCreator(userService.currentLoginEmployee());
                    c.setCreateTime(System.currentTimeMillis());
                }
            });
        }

        notice = noticeRepository.save(notice);
        NoticeDTO result = noticeMapper.noticeToNoticeDTO(notice);
        return result;
    }

    /**
     *  Get all the notices.
     *
     *  @param sort the order
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<NoticeDTO> findAll(Sort sort) {
        log.debug("Request to get all Notices");
        List<NoticeDTO> result = noticeRepository.findAll(sort).stream()
            .map(noticeMapper::noticeToNoticeDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get all the notices.
     *
     *  @param predicate the predicate
     *  @param sort the order
     *  @return the list of entities
     */
    public List<NoticeDTO> findAll(Predicate predicate, Sort sort) {
        if (predicate == null) {
           return findAll(sort);
        }

        return StreamSupport.stream(noticeRepository.findAll(predicate, sort).spliterator(), false)
            .map(noticeMapper::noticeToNoticeDTO)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get one notice by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public NoticeDTO findOne(Long id) {
        log.debug("Request to get Notice : {}", id);
        Notice notice = noticeRepository.findOneWithEagerRelationships(id);
        NoticeDTO noticeDTO = noticeMapper.noticeToNoticeDTO(notice);
        return noticeDTO;
    }

    /**
     *  Delete the  notice by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Notice : {}", id);
        noticeRepository.delete(id);
    }
}
