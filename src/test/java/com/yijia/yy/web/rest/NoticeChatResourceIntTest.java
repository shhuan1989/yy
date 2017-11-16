package com.yijia.yy.web.rest;

import com.yijia.yy.yyOaApp;

import com.yijia.yy.domain.NoticeChat;
import com.yijia.yy.repository.NoticeChatRepository;
import com.yijia.yy.service.NoticeChatService;
import com.yijia.yy.service.dto.NoticeChatDTO;
import com.yijia.yy.service.mapper.NoticeChatMapper;

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
 * Test class for the NoticeChatResource REST controller.
 *
 * @see NoticeChatResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = yyOaApp.class)
public class NoticeChatResourceIntTest {

    private static final String DEFAULT_TEXT = "AAAAA";
    private static final String UPDATED_TEXT = "BBBBB";

    private static final Long DEFAULT_CREATE_TIME = 1L;
    private static final Long UPDATED_CREATE_TIME = 2L;

    @Inject
    private NoticeChatRepository noticeChatRepository;

    @Inject
    private NoticeChatMapper noticeChatMapper;

    @Inject
    private NoticeChatService noticeChatService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restNoticeChatMockMvc;

    private NoticeChat noticeChat;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NoticeChatResource noticeChatResource = new NoticeChatResource();
        ReflectionTestUtils.setField(noticeChatResource, "noticeChatService", noticeChatService);
        this.restNoticeChatMockMvc = MockMvcBuilders.standaloneSetup(noticeChatResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NoticeChat createEntity(EntityManager em) {
        NoticeChat noticeChat = new NoticeChat()
                .text(DEFAULT_TEXT)
                .createTime(DEFAULT_CREATE_TIME);
        return noticeChat;
    }

    @Before
    public void initTest() {
        noticeChat = createEntity(em);
    }

    @Test
    @Transactional
    public void createNoticeChat() throws Exception {
        int databaseSizeBeforeCreate = noticeChatRepository.findAll().size();

        // Create the NoticeChat
        NoticeChatDTO noticeChatDTO = noticeChatMapper.noticeChatToNoticeChatDTO(noticeChat);

        restNoticeChatMockMvc.perform(post("/api/notice-chats")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(noticeChatDTO)))
                .andExpect(status().isCreated());

        // Validate the NoticeChat in the database
        List<NoticeChat> noticeChats = noticeChatRepository.findAll();
        assertThat(noticeChats).hasSize(databaseSizeBeforeCreate + 1);
        NoticeChat testNoticeChat = noticeChats.get(noticeChats.size() - 1);
        assertThat(testNoticeChat.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testNoticeChat.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
    }

    @Test
    @Transactional
    public void getAllNoticeChats() throws Exception {
        // Initialize the database
        noticeChatRepository.saveAndFlush(noticeChat);

        // Get all the noticeChats
        restNoticeChatMockMvc.perform(get("/api/notice-chats?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(noticeChat.getId().intValue())))
                .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())))
                .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.intValue())));
    }

    @Test
    @Transactional
    public void getNoticeChat() throws Exception {
        // Initialize the database
        noticeChatRepository.saveAndFlush(noticeChat);

        // Get the noticeChat
        restNoticeChatMockMvc.perform(get("/api/notice-chats/{id}", noticeChat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(noticeChat.getId().intValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingNoticeChat() throws Exception {
        // Get the noticeChat
        restNoticeChatMockMvc.perform(get("/api/notice-chats/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNoticeChat() throws Exception {
        // Initialize the database
        noticeChatRepository.saveAndFlush(noticeChat);
        int databaseSizeBeforeUpdate = noticeChatRepository.findAll().size();

        // Update the noticeChat
        NoticeChat updatedNoticeChat = noticeChatRepository.findOne(noticeChat.getId());
        updatedNoticeChat
                .text(UPDATED_TEXT)
                .createTime(UPDATED_CREATE_TIME);
        NoticeChatDTO noticeChatDTO = noticeChatMapper.noticeChatToNoticeChatDTO(updatedNoticeChat);

        restNoticeChatMockMvc.perform(put("/api/notice-chats")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(noticeChatDTO)))
                .andExpect(status().isOk());

        // Validate the NoticeChat in the database
        List<NoticeChat> noticeChats = noticeChatRepository.findAll();
        assertThat(noticeChats).hasSize(databaseSizeBeforeUpdate);
        NoticeChat testNoticeChat = noticeChats.get(noticeChats.size() - 1);
        assertThat(testNoticeChat.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testNoticeChat.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    public void deleteNoticeChat() throws Exception {
        // Initialize the database
        noticeChatRepository.saveAndFlush(noticeChat);
        int databaseSizeBeforeDelete = noticeChatRepository.findAll().size();

        // Get the noticeChat
        restNoticeChatMockMvc.perform(delete("/api/notice-chats/{id}", noticeChat.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<NoticeChat> noticeChats = noticeChatRepository.findAll();
        assertThat(noticeChats).hasSize(databaseSizeBeforeDelete - 1);
    }
}
