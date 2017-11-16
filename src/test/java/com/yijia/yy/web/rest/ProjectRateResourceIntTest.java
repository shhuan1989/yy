package com.yijia.yy.web.rest;

import com.yijia.yy.yyOaApp;

import com.yijia.yy.domain.ProjectRate;
import com.yijia.yy.domain.Employee;
import com.yijia.yy.domain.Project;
import com.yijia.yy.repository.ProjectRateRepository;
import com.yijia.yy.service.ProjectRateService;
import com.yijia.yy.service.dto.ProjectRateDTO;
import com.yijia.yy.service.mapper.ProjectRateMapper;

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
 * Test class for the ProjectRateResource REST controller.
 *
 * @see ProjectRateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = yyOaApp.class)
public class ProjectRateResourceIntTest {


    private static final Long DEFAULT_CREATE_TIME = 1L;
    private static final Long UPDATED_CREATE_TIME = 2L;

    @Inject
    private ProjectRateRepository projectRateRepository;

    @Inject
    private ProjectRateMapper projectRateMapper;

    @Inject
    private ProjectRateService projectRateService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restProjectRateMockMvc;

    private ProjectRate projectRate;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProjectRateResource projectRateResource = new ProjectRateResource();
        ReflectionTestUtils.setField(projectRateResource, "projectRateService", projectRateService);
        this.restProjectRateMockMvc = MockMvcBuilders.standaloneSetup(projectRateResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjectRate createEntity(EntityManager em) {
        ProjectRate projectRate = new ProjectRate()
                .createTime(DEFAULT_CREATE_TIME);
        // Add required entity
        Employee creator = EmployeeResourceIntTest.createEntity(em);
        em.persist(creator);
        em.flush();
        projectRate.setOwner(creator);
        // Add required entity
        Project project = ProjectResourceIntTest.createEntity(em);
        em.persist(project);
        em.flush();
        projectRate.setProject(project);
        return projectRate;
    }

    @Before
    public void initTest() {
        projectRate = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjectRate() throws Exception {
        int databaseSizeBeforeCreate = projectRateRepository.findAll().size();

        // Create the ProjectRate
        ProjectRateDTO projectRateDTO = projectRateMapper.projectRateToProjectRateDTO(projectRate);

        restProjectRateMockMvc.perform(post("/api/project-rates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projectRateDTO)))
                .andExpect(status().isCreated());

        // Validate the ProjectRate in the database
        List<ProjectRate> projectRates = projectRateRepository.findAll();
        assertThat(projectRates).hasSize(databaseSizeBeforeCreate + 1);
        ProjectRate testProjectRate = projectRates.get(projectRates.size() - 1);
        assertThat(testProjectRate.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
    }

    @Test
    @Transactional
    public void checkCreateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectRateRepository.findAll().size();
        // set the field null
        projectRate.setCreateTime(null);

        // Create the ProjectRate, which fails.
        ProjectRateDTO projectRateDTO = projectRateMapper.projectRateToProjectRateDTO(projectRate);

        restProjectRateMockMvc.perform(post("/api/project-rates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projectRateDTO)))
                .andExpect(status().isBadRequest());

        List<ProjectRate> projectRates = projectRateRepository.findAll();
        assertThat(projectRates).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProjectRates() throws Exception {
        // Initialize the database
        projectRateRepository.saveAndFlush(projectRate);

        // Get all the projectRates
        restProjectRateMockMvc.perform(get("/api/project-rates?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(projectRate.getId().intValue())))
                .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.intValue())));
    }

    @Test
    @Transactional
    public void getProjectRate() throws Exception {
        // Initialize the database
        projectRateRepository.saveAndFlush(projectRate);

        // Get the projectRate
        restProjectRateMockMvc.perform(get("/api/project-rates/{id}", projectRate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projectRate.getId().intValue()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingProjectRate() throws Exception {
        // Get the projectRate
        restProjectRateMockMvc.perform(get("/api/project-rates/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjectRate() throws Exception {
        // Initialize the database
        projectRateRepository.saveAndFlush(projectRate);
        int databaseSizeBeforeUpdate = projectRateRepository.findAll().size();

        // Update the projectRate
        ProjectRate updatedProjectRate = projectRateRepository.findOne(projectRate.getId());
        updatedProjectRate
                .createTime(UPDATED_CREATE_TIME);
        ProjectRateDTO projectRateDTO = projectRateMapper.projectRateToProjectRateDTO(updatedProjectRate);

        restProjectRateMockMvc.perform(put("/api/project-rates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projectRateDTO)))
                .andExpect(status().isOk());

        // Validate the ProjectRate in the database
        List<ProjectRate> projectRates = projectRateRepository.findAll();
        assertThat(projectRates).hasSize(databaseSizeBeforeUpdate);
        ProjectRate testProjectRate = projectRates.get(projectRates.size() - 1);
        assertThat(testProjectRate.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    public void deleteProjectRate() throws Exception {
        // Initialize the database
        projectRateRepository.saveAndFlush(projectRate);
        int databaseSizeBeforeDelete = projectRateRepository.findAll().size();

        // Get the projectRate
        restProjectRateMockMvc.perform(delete("/api/project-rates/{id}", projectRate.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ProjectRate> projectRates = projectRateRepository.findAll();
        assertThat(projectRates).hasSize(databaseSizeBeforeDelete - 1);
    }
}
