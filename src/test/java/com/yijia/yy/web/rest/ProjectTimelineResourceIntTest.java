package com.yijia.yy.web.rest;

import com.yijia.yy.YiyingOaApp;

import com.yijia.yy.domain.ProjectTimeline;
import com.yijia.yy.domain.Project;
import com.yijia.yy.repository.ProjectTimelineRepository;
import com.yijia.yy.service.ProjectTimelineService;
import com.yijia.yy.service.dto.ProjectTimelineDTO;
import com.yijia.yy.service.mapper.ProjectTimelineMapper;

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
 * Test class for the ProjectTimelineResource REST controller.
 *
 * @see ProjectTimelineResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YiyingOaApp.class)
public class ProjectTimelineResourceIntTest {


    private static final Long DEFAULT_CREATE_TIME = 1L;
    private static final Long UPDATED_CREATE_TIME = 2L;
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_INFO = "AAAAA";
    private static final String UPDATED_INFO = "BBBBB";

    @Inject
    private ProjectTimelineRepository projectTimelineRepository;

    @Inject
    private ProjectTimelineMapper projectTimelineMapper;

    @Inject
    private ProjectTimelineService projectTimelineService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restProjectTimelineMockMvc;

    private ProjectTimeline projectTimeline;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProjectTimelineResource projectTimelineResource = new ProjectTimelineResource();
        ReflectionTestUtils.setField(projectTimelineResource, "projectTimelineService", projectTimelineService);
        this.restProjectTimelineMockMvc = MockMvcBuilders.standaloneSetup(projectTimelineResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjectTimeline createEntity(EntityManager em) {
        ProjectTimeline projectTimeline = new ProjectTimeline()
                .createTime(DEFAULT_CREATE_TIME)
                .name(DEFAULT_NAME)
                .info(DEFAULT_INFO);
        // Add required entity
        Project project = ProjectResourceIntTest.createEntity(em);
        em.persist(project);
        em.flush();
        projectTimeline.setProject(project);
        return projectTimeline;
    }

    @Before
    public void initTest() {
        projectTimeline = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjectTimeline() throws Exception {
        int databaseSizeBeforeCreate = projectTimelineRepository.findAll().size();

        // Create the ProjectTimeline
        ProjectTimelineDTO projectTimelineDTO = projectTimelineMapper.projectTimelineToProjectTimelineDTO(projectTimeline);

        restProjectTimelineMockMvc.perform(post("/api/project-timelines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projectTimelineDTO)))
                .andExpect(status().isCreated());

        // Validate the ProjectTimeline in the database
        List<ProjectTimeline> projectTimelines = projectTimelineRepository.findAll();
        assertThat(projectTimelines).hasSize(databaseSizeBeforeCreate + 1);
        ProjectTimeline testProjectTimeline = projectTimelines.get(projectTimelines.size() - 1);
        assertThat(testProjectTimeline.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testProjectTimeline.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProjectTimeline.getInfo()).isEqualTo(DEFAULT_INFO);
    }

    @Test
    @Transactional
    public void checkCreateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectTimelineRepository.findAll().size();
        // set the field null
        projectTimeline.setCreateTime(null);

        // Create the ProjectTimeline, which fails.
        ProjectTimelineDTO projectTimelineDTO = projectTimelineMapper.projectTimelineToProjectTimelineDTO(projectTimeline);

        restProjectTimelineMockMvc.perform(post("/api/project-timelines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projectTimelineDTO)))
                .andExpect(status().isBadRequest());

        List<ProjectTimeline> projectTimelines = projectTimelineRepository.findAll();
        assertThat(projectTimelines).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProjectTimelines() throws Exception {
        // Initialize the database
        projectTimelineRepository.saveAndFlush(projectTimeline);

        // Get all the projectTimelines
        restProjectTimelineMockMvc.perform(get("/api/project-timelines?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(projectTimeline.getId().intValue())))
                .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO.toString())));
    }

    @Test
    @Transactional
    public void getProjectTimeline() throws Exception {
        // Initialize the database
        projectTimelineRepository.saveAndFlush(projectTimeline);

        // Get the projectTimeline
        restProjectTimelineMockMvc.perform(get("/api/project-timelines/{id}", projectTimeline.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projectTimeline.getId().intValue()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.info").value(DEFAULT_INFO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProjectTimeline() throws Exception {
        // Get the projectTimeline
        restProjectTimelineMockMvc.perform(get("/api/project-timelines/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjectTimeline() throws Exception {
        // Initialize the database
        projectTimelineRepository.saveAndFlush(projectTimeline);
        int databaseSizeBeforeUpdate = projectTimelineRepository.findAll().size();

        // Update the projectTimeline
        ProjectTimeline updatedProjectTimeline = projectTimelineRepository.findOne(projectTimeline.getId());
        updatedProjectTimeline
                .createTime(UPDATED_CREATE_TIME)
                .name(UPDATED_NAME)
                .info(UPDATED_INFO);
        ProjectTimelineDTO projectTimelineDTO = projectTimelineMapper.projectTimelineToProjectTimelineDTO(updatedProjectTimeline);

        restProjectTimelineMockMvc.perform(put("/api/project-timelines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projectTimelineDTO)))
                .andExpect(status().isOk());

        // Validate the ProjectTimeline in the database
        List<ProjectTimeline> projectTimelines = projectTimelineRepository.findAll();
        assertThat(projectTimelines).hasSize(databaseSizeBeforeUpdate);
        ProjectTimeline testProjectTimeline = projectTimelines.get(projectTimelines.size() - 1);
        assertThat(testProjectTimeline.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testProjectTimeline.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProjectTimeline.getInfo()).isEqualTo(UPDATED_INFO);
    }

    @Test
    @Transactional
    public void deleteProjectTimeline() throws Exception {
        // Initialize the database
        projectTimelineRepository.saveAndFlush(projectTimeline);
        int databaseSizeBeforeDelete = projectTimelineRepository.findAll().size();

        // Get the projectTimeline
        restProjectTimelineMockMvc.perform(delete("/api/project-timelines/{id}", projectTimeline.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ProjectTimeline> projectTimelines = projectTimelineRepository.findAll();
        assertThat(projectTimelines).hasSize(databaseSizeBeforeDelete - 1);
    }
}
