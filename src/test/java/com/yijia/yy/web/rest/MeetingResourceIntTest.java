package com.yijia.yy.web.rest;

import com.yijia.yy.yyOaApp;

import com.yijia.yy.domain.Meeting;
import com.yijia.yy.repository.MeetingRepository;
import com.yijia.yy.service.MeetingService;
import com.yijia.yy.service.dto.MeetingDTO;
import com.yijia.yy.service.mapper.MeetingMapper;

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
 * Test class for the MeetingResource REST controller.
 *
 * @see MeetingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = yyOaApp.class)
public class MeetingResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final Long DEFAULT_START_TIME = 1L;
    private static final Long UPDATED_START_TIME = 2L;

    private static final Long DEFAULT_END_TIME = 1L;
    private static final Long UPDATED_END_TIME = 2L;
    private static final byte[] DEFAULT_INFO = "AAAAA".getBytes();
    private static final byte[] UPDATED_INFO = "BBBBB".getBytes();

    @Inject
    private MeetingRepository meetingRepository;

    @Inject
    private MeetingMapper meetingMapper;

    @Inject
    private MeetingService meetingService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restMeetingMockMvc;

    private Meeting meeting;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MeetingResource meetingResource = new MeetingResource();
        ReflectionTestUtils.setField(meetingResource, "meetingService", meetingService);
        this.restMeetingMockMvc = MockMvcBuilders.standaloneSetup(meetingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Meeting createEntity(EntityManager em) {
        Meeting meeting = new Meeting()
                .name(DEFAULT_NAME)
                .startTime(DEFAULT_START_TIME)
                .endTime(DEFAULT_END_TIME)
                .info(DEFAULT_INFO);
        return meeting;
    }

    @Before
    public void initTest() {
        meeting = createEntity(em);
    }

    @Test
    @Transactional
    public void createMeeting() throws Exception {
        int databaseSizeBeforeCreate = meetingRepository.findAll().size();

        // Create the Meeting
        MeetingDTO meetingDTO = meetingMapper.meetingToMeetingDTO(meeting);

        restMeetingMockMvc.perform(post("/api/meetings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(meetingDTO)))
                .andExpect(status().isCreated());

        // Validate the Meeting in the database
        List<Meeting> meetings = meetingRepository.findAll();
        assertThat(meetings).hasSize(databaseSizeBeforeCreate + 1);
        Meeting testMeeting = meetings.get(meetings.size() - 1);
        assertThat(testMeeting.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMeeting.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testMeeting.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testMeeting.getInfo()).isEqualTo(DEFAULT_INFO);
    }

    @Test
    @Transactional
    public void checkStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = meetingRepository.findAll().size();
        // set the field null
        meeting.setStartTime(null);

        // Create the Meeting, which fails.
        MeetingDTO meetingDTO = meetingMapper.meetingToMeetingDTO(meeting);

        restMeetingMockMvc.perform(post("/api/meetings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(meetingDTO)))
                .andExpect(status().isBadRequest());

        List<Meeting> meetings = meetingRepository.findAll();
        assertThat(meetings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = meetingRepository.findAll().size();
        // set the field null
        meeting.setEndTime(null);

        // Create the Meeting, which fails.
        MeetingDTO meetingDTO = meetingMapper.meetingToMeetingDTO(meeting);

        restMeetingMockMvc.perform(post("/api/meetings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(meetingDTO)))
                .andExpect(status().isBadRequest());

        List<Meeting> meetings = meetingRepository.findAll();
        assertThat(meetings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMeetings() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        // Get all the meetings
        restMeetingMockMvc.perform(get("/api/meetings?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(meeting.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.intValue())))
                .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.intValue())))
                .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO.toString())));
    }

    @Test
    @Transactional
    public void getMeeting() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);

        // Get the meeting
        restMeetingMockMvc.perform(get("/api/meetings/{id}", meeting.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(meeting.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.intValue()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.intValue()))
            .andExpect(jsonPath("$.info").value(DEFAULT_INFO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMeeting() throws Exception {
        // Get the meeting
        restMeetingMockMvc.perform(get("/api/meetings/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMeeting() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);
        int databaseSizeBeforeUpdate = meetingRepository.findAll().size();

        // Update the meeting
        Meeting updatedMeeting = meetingRepository.findOne(meeting.getId());
        updatedMeeting
                .name(UPDATED_NAME)
                .startTime(UPDATED_START_TIME)
                .endTime(UPDATED_END_TIME)
                .info(UPDATED_INFO);
        MeetingDTO meetingDTO = meetingMapper.meetingToMeetingDTO(updatedMeeting);

        restMeetingMockMvc.perform(put("/api/meetings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(meetingDTO)))
                .andExpect(status().isOk());

        // Validate the Meeting in the database
        List<Meeting> meetings = meetingRepository.findAll();
        assertThat(meetings).hasSize(databaseSizeBeforeUpdate);
        Meeting testMeeting = meetings.get(meetings.size() - 1);
        assertThat(testMeeting.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMeeting.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testMeeting.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testMeeting.getInfo()).isEqualTo(UPDATED_INFO);
    }

    @Test
    @Transactional
    public void deleteMeeting() throws Exception {
        // Initialize the database
        meetingRepository.saveAndFlush(meeting);
        int databaseSizeBeforeDelete = meetingRepository.findAll().size();

        // Get the meeting
        restMeetingMockMvc.perform(delete("/api/meetings/{id}", meeting.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Meeting> meetings = meetingRepository.findAll();
        assertThat(meetings).hasSize(databaseSizeBeforeDelete - 1);
    }
}
