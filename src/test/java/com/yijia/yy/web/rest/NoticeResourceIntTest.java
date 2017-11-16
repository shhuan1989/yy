package com.yijia.yy.web.rest;

import com.yijia.yy.YiyingOaApp;

import com.yijia.yy.domain.Notice;
import com.yijia.yy.repository.NoticeRepository;
import com.yijia.yy.service.NoticeService;
import com.yijia.yy.service.dto.NoticeDTO;
import com.yijia.yy.service.mapper.NoticeMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the NoticeResource REST controller.
 *
 * @see NoticeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YiyingOaApp.class)
public class NoticeResourceIntTest {

    private static final String DEFAULT_SUBJECT = "AAAAA";
    private static final String UPDATED_SUBJECT = "BBBBB";

    private static final Long DEFAULT_EXPIRE_TIME = 1L;
    private static final Long UPDATED_EXPIRE_TIME = 2L;
    private static final byte[] DEFAULT_CONTENT = "AAAAA".getBytes();
    private static final byte[] UPDATED_CONTENT = "BBBBB".getBytes();

    private static final Long DEFAULT_CREATE_TIME = 1L;
    private static final Long UPDATED_CREATE_TIME = 2L;

    @Inject
    private NoticeRepository noticeRepository;

    @Inject
    private NoticeMapper noticeMapper;

    @Inject
    private NoticeService noticeService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restNoticeMockMvc;

    private Notice notice;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NoticeResource noticeResource = new NoticeResource();
        ReflectionTestUtils.setField(noticeResource, "noticeService", noticeService);
        this.restNoticeMockMvc = MockMvcBuilders.standaloneSetup(noticeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notice createEntity(EntityManager em) {
        Notice notice = new Notice()
                .subject(DEFAULT_SUBJECT)
                .expireTime(DEFAULT_EXPIRE_TIME)
                .content(DEFAULT_CONTENT)
                .createTime(DEFAULT_CREATE_TIME);
        return notice;
    }

    @Before
    public void initTest() {
        notice = createEntity(em);
    }

    @Test
    @Transactional
    public void createNotice() throws Exception {
        int databaseSizeBeforeCreate = noticeRepository.findAll().size();

        // Create the Notice
        NoticeDTO noticeDTO = noticeMapper.noticeToNoticeDTO(notice);

        restNoticeMockMvc.perform(post("/api/notices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(noticeDTO)))
                .andExpect(status().isCreated());

        // Validate the Notice in the database
        List<Notice> notices = noticeRepository.findAll();
        assertThat(notices).hasSize(databaseSizeBeforeCreate + 1);
        Notice testNotice = notices.get(notices.size() - 1);
        assertThat(testNotice.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testNotice.getExpireTime()).isEqualTo(DEFAULT_EXPIRE_TIME);
        assertThat(testNotice.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testNotice.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
    }

    @Test
    @Transactional
    public void getAllNotices() throws Exception {
        // Initialize the database
        noticeRepository.saveAndFlush(notice);

        // Get all the notices
        restNoticeMockMvc.perform(get("/api/notices?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(notice.getId().intValue())))
                .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT.toString())))
                .andExpect(jsonPath("$.[*].expireTime").value(hasItem(DEFAULT_EXPIRE_TIME.intValue())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
                .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.intValue())));
    }

    @Test
    @Transactional
    public void getNotice() throws Exception {
        // Initialize the database
        noticeRepository.saveAndFlush(notice);

        // Get the notice
        restNoticeMockMvc.perform(get("/api/notices/{id}", notice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(notice.getId().intValue()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT.toString()))
            .andExpect(jsonPath("$.expireTime").value(DEFAULT_EXPIRE_TIME.intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingNotice() throws Exception {
        // Get the notice
        restNoticeMockMvc.perform(get("/api/notices/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNotice() throws Exception {
        // Initialize the database
        noticeRepository.saveAndFlush(notice);
        int databaseSizeBeforeUpdate = noticeRepository.findAll().size();

        // Update the notice
        Notice updatedNotice = noticeRepository.findOne(notice.getId());
        updatedNotice
                .subject(UPDATED_SUBJECT)
                .expireTime(UPDATED_EXPIRE_TIME)
                .content(UPDATED_CONTENT)
                .createTime(UPDATED_CREATE_TIME);
        NoticeDTO noticeDTO = noticeMapper.noticeToNoticeDTO(updatedNotice);

        restNoticeMockMvc.perform(put("/api/notices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(noticeDTO)))
                .andExpect(status().isOk());

        // Validate the Notice in the database
        List<Notice> notices = noticeRepository.findAll();
        assertThat(notices).hasSize(databaseSizeBeforeUpdate);
        Notice testNotice = notices.get(notices.size() - 1);
        assertThat(testNotice.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testNotice.getExpireTime()).isEqualTo(UPDATED_EXPIRE_TIME);
        assertThat(testNotice.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testNotice.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    public void deleteNotice() throws Exception {
        // Initialize the database
        noticeRepository.saveAndFlush(notice);
        int databaseSizeBeforeDelete = noticeRepository.findAll().size();

        // Get the notice
        restNoticeMockMvc.perform(delete("/api/notices/{id}", notice.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Notice> notices = noticeRepository.findAll();
        assertThat(notices).hasSize(databaseSizeBeforeDelete - 1);
    }
}
