package com.yijia.yy.web.rest;

import com.yijia.yy.YiyingOaApp;

import com.yijia.yy.domain.ProjectCost;
import com.yijia.yy.repository.ProjectCostRepository;
import com.yijia.yy.service.ProjectCostService;
import com.yijia.yy.service.dto.ProjectCostDTO;
import com.yijia.yy.service.mapper.ProjectCostMapper;

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
 * Test class for the ProjectCostResource REST controller.
 *
 * @see ProjectCostResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YiyingOaApp.class)
public class ProjectCostResourceIntTest {


    private static final Long DEFAULT_CREATE_TIME = 1L;
    private static final Long UPDATED_CREATE_TIME = 2L;

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;
    private static final String DEFAULT_INFO = "AAAAA";
    private static final String UPDATED_INFO = "BBBBB";

    @Inject
    private ProjectCostRepository projectCostRepository;

    @Inject
    private ProjectCostMapper projectCostMapper;

    @Inject
    private ProjectCostService projectCostService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restProjectCostMockMvc;

    private ProjectCost projectCost;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProjectCostResource projectCostResource = new ProjectCostResource();
        ReflectionTestUtils.setField(projectCostResource, "projectCostService", projectCostService);
        this.restProjectCostMockMvc = MockMvcBuilders.standaloneSetup(projectCostResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjectCost createEntity(EntityManager em) {
        ProjectCost projectCost = new ProjectCost()
                .createTime(DEFAULT_CREATE_TIME)
                .amount(DEFAULT_AMOUNT)
                .info(DEFAULT_INFO);
        return projectCost;
    }

    @Before
    public void initTest() {
        projectCost = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjectCost() throws Exception {
        int databaseSizeBeforeCreate = projectCostRepository.findAll().size();

        // Create the ProjectCost
        ProjectCostDTO projectCostDTO = projectCostMapper.projectCostToProjectCostDTO(projectCost);

        restProjectCostMockMvc.perform(post("/api/project-costs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projectCostDTO)))
                .andExpect(status().isCreated());

        // Validate the ProjectCost in the database
        List<ProjectCost> projectCosts = projectCostRepository.findAll();
        assertThat(projectCosts).hasSize(databaseSizeBeforeCreate + 1);
        ProjectCost testProjectCost = projectCosts.get(projectCosts.size() - 1);
        assertThat(testProjectCost.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testProjectCost.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testProjectCost.getInfo()).isEqualTo(DEFAULT_INFO);
    }

    @Test
    @Transactional
    public void getAllProjectCosts() throws Exception {
        // Initialize the database
        projectCostRepository.saveAndFlush(projectCost);

        // Get all the projectCosts
        restProjectCostMockMvc.perform(get("/api/project-costs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(projectCost.getId().intValue())))
                .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.intValue())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
                .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO.toString())));
    }

    @Test
    @Transactional
    public void getProjectCost() throws Exception {
        // Initialize the database
        projectCostRepository.saveAndFlush(projectCost);

        // Get the projectCost
        restProjectCostMockMvc.perform(get("/api/project-costs/{id}", projectCost.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projectCost.getId().intValue()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.info").value(DEFAULT_INFO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProjectCost() throws Exception {
        // Get the projectCost
        restProjectCostMockMvc.perform(get("/api/project-costs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjectCost() throws Exception {
        // Initialize the database
        projectCostRepository.saveAndFlush(projectCost);
        int databaseSizeBeforeUpdate = projectCostRepository.findAll().size();

        // Update the projectCost
        ProjectCost updatedProjectCost = projectCostRepository.findOne(projectCost.getId());
        updatedProjectCost
                .createTime(UPDATED_CREATE_TIME)
                .amount(UPDATED_AMOUNT)
                .info(UPDATED_INFO);
        ProjectCostDTO projectCostDTO = projectCostMapper.projectCostToProjectCostDTO(updatedProjectCost);

        restProjectCostMockMvc.perform(put("/api/project-costs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projectCostDTO)))
                .andExpect(status().isOk());

        // Validate the ProjectCost in the database
        List<ProjectCost> projectCosts = projectCostRepository.findAll();
        assertThat(projectCosts).hasSize(databaseSizeBeforeUpdate);
        ProjectCost testProjectCost = projectCosts.get(projectCosts.size() - 1);
        assertThat(testProjectCost.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testProjectCost.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testProjectCost.getInfo()).isEqualTo(UPDATED_INFO);
    }

    @Test
    @Transactional
    public void deleteProjectCost() throws Exception {
        // Initialize the database
        projectCostRepository.saveAndFlush(projectCost);
        int databaseSizeBeforeDelete = projectCostRepository.findAll().size();

        // Get the projectCost
        restProjectCostMockMvc.perform(delete("/api/project-costs/{id}", projectCost.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ProjectCost> projectCosts = projectCostRepository.findAll();
        assertThat(projectCosts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
