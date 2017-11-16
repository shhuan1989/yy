package com.yijia.yy.web.rest;

import com.yijia.yy.YiyingOaApp;

import com.yijia.yy.domain.OvertimeWork;
import com.yijia.yy.repository.OvertimeWorkRepository;
import com.yijia.yy.service.OvertimeWorkService;
import com.yijia.yy.service.dto.OvertimeWorkDTO;
import com.yijia.yy.service.mapper.OvertimeWorkMapper;

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
 * Test class for the OvertimeWorkResource REST controller.
 *
 * @see OvertimeWorkResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YiyingOaApp.class)
public class OvertimeWorkResourceIntTest {


    private static final Long DEFAULT_START_TIME = 1L;
    private static final Long UPDATED_START_TIME = 2L;

    private static final Long DEFAULT_END_TIME = 1L;
    private static final Long UPDATED_END_TIME = 2L;

    private static final Integer DEFAULT_HOURS = 1;
    private static final Integer UPDATED_HOURS = 2;
    private static final String DEFAULT_INFO = "AAAAA";
    private static final String UPDATED_INFO = "BBBBB";

    private static final Long DEFAULT_CREATE_TIME = 1L;
    private static final Long UPDATED_CREATE_TIME = 2L;

    @Inject
    private OvertimeWorkRepository overtimeWorkRepository;

    @Inject
    private OvertimeWorkMapper overtimeWorkMapper;

    @Inject
    private OvertimeWorkService overtimeWorkService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restOvertimeWorkMockMvc;

    private OvertimeWork overtimeWork;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OvertimeWorkResource overtimeWorkResource = new OvertimeWorkResource();
        ReflectionTestUtils.setField(overtimeWorkResource, "overtimeWorkService", overtimeWorkService);
        this.restOvertimeWorkMockMvc = MockMvcBuilders.standaloneSetup(overtimeWorkResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OvertimeWork createEntity(EntityManager em) {
        OvertimeWork overtimeWork = new OvertimeWork();
        return overtimeWork;
    }

    @Before
    public void initTest() {
        overtimeWork = createEntity(em);
    }

    @Test
    @Transactional
    public void createOvertimeWork() throws Exception {
        int databaseSizeBeforeCreate = overtimeWorkRepository.findAll().size();

        // Create the OvertimeWork
        OvertimeWorkDTO overtimeWorkDTO = overtimeWorkMapper.overtimeWorkToOvertimeWorkDTO(overtimeWork);

        restOvertimeWorkMockMvc.perform(post("/api/overtime-works")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(overtimeWorkDTO)))
                .andExpect(status().isCreated());

        // Validate the OvertimeWork in the database
        List<OvertimeWork> overtimeWorks = overtimeWorkRepository.findAll();
        assertThat(overtimeWorks).hasSize(databaseSizeBeforeCreate + 1);
        OvertimeWork testOvertimeWork = overtimeWorks.get(overtimeWorks.size() - 1);
        assertThat(testOvertimeWork.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testOvertimeWork.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testOvertimeWork.getHours()).isEqualTo(DEFAULT_HOURS);
        assertThat(testOvertimeWork.getInfo()).isEqualTo(DEFAULT_INFO);
        assertThat(testOvertimeWork.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
    }

    @Test
    @Transactional
    public void checkStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = overtimeWorkRepository.findAll().size();
        // set the field null
        overtimeWork.setStartTime(null);

        // Create the OvertimeWork, which fails.
        OvertimeWorkDTO overtimeWorkDTO = overtimeWorkMapper.overtimeWorkToOvertimeWorkDTO(overtimeWork);

        restOvertimeWorkMockMvc.perform(post("/api/overtime-works")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(overtimeWorkDTO)))
                .andExpect(status().isBadRequest());

        List<OvertimeWork> overtimeWorks = overtimeWorkRepository.findAll();
        assertThat(overtimeWorks).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = overtimeWorkRepository.findAll().size();
        // set the field null
        overtimeWork.setEndTime(null);

        // Create the OvertimeWork, which fails.
        OvertimeWorkDTO overtimeWorkDTO = overtimeWorkMapper.overtimeWorkToOvertimeWorkDTO(overtimeWork);

        restOvertimeWorkMockMvc.perform(post("/api/overtime-works")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(overtimeWorkDTO)))
                .andExpect(status().isBadRequest());

        List<OvertimeWork> overtimeWorks = overtimeWorkRepository.findAll();
        assertThat(overtimeWorks).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOvertimeWorks() throws Exception {
        // Initialize the database
        overtimeWorkRepository.saveAndFlush(overtimeWork);

        // Get all the overtimeWorks
        restOvertimeWorkMockMvc.perform(get("/api/overtime-works?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(overtimeWork.getId().intValue())))
                .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.intValue())))
                .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.intValue())))
                .andExpect(jsonPath("$.[*].hours").value(hasItem(DEFAULT_HOURS)))
                .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO.toString())))
                .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.intValue())));
    }

    @Test
    @Transactional
    public void getOvertimeWork() throws Exception {
        // Initialize the database
        overtimeWorkRepository.saveAndFlush(overtimeWork);

        // Get the overtimeWork
        restOvertimeWorkMockMvc.perform(get("/api/overtime-works/{id}", overtimeWork.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(overtimeWork.getId().intValue()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.intValue()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.intValue()))
            .andExpect(jsonPath("$.hours").value(DEFAULT_HOURS))
            .andExpect(jsonPath("$.info").value(DEFAULT_INFO.toString()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingOvertimeWork() throws Exception {
        // Get the overtimeWork
        restOvertimeWorkMockMvc.perform(get("/api/overtime-works/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOvertimeWork() throws Exception {
        // Initialize the database
        overtimeWorkRepository.saveAndFlush(overtimeWork);
        int databaseSizeBeforeUpdate = overtimeWorkRepository.findAll().size();

        // Update the overtimeWork
        OvertimeWork updatedOvertimeWork = overtimeWorkRepository.findOne(overtimeWork.getId());
        OvertimeWorkDTO overtimeWorkDTO = overtimeWorkMapper.overtimeWorkToOvertimeWorkDTO(updatedOvertimeWork);

        restOvertimeWorkMockMvc.perform(put("/api/overtime-works")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(overtimeWorkDTO)))
                .andExpect(status().isOk());

        // Validate the OvertimeWork in the database
        List<OvertimeWork> overtimeWorks = overtimeWorkRepository.findAll();
        assertThat(overtimeWorks).hasSize(databaseSizeBeforeUpdate);
        OvertimeWork testOvertimeWork = overtimeWorks.get(overtimeWorks.size() - 1);
        assertThat(testOvertimeWork.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testOvertimeWork.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testOvertimeWork.getHours()).isEqualTo(UPDATED_HOURS);
        assertThat(testOvertimeWork.getInfo()).isEqualTo(UPDATED_INFO);
        assertThat(testOvertimeWork.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    public void deleteOvertimeWork() throws Exception {
        // Initialize the database
        overtimeWorkRepository.saveAndFlush(overtimeWork);
        int databaseSizeBeforeDelete = overtimeWorkRepository.findAll().size();

        // Get the overtimeWork
        restOvertimeWorkMockMvc.perform(delete("/api/overtime-works/{id}", overtimeWork.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<OvertimeWork> overtimeWorks = overtimeWorkRepository.findAll();
        assertThat(overtimeWorks).hasSize(databaseSizeBeforeDelete - 1);
    }
}
