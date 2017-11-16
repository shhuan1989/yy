package com.yijia.yy.web.rest;

import com.yijia.yy.YiyingOaApp;

import com.yijia.yy.domain.ProjectProgressTable;
import com.yijia.yy.repository.ProjectProgressTableRepository;
import com.yijia.yy.service.ProjectProgressTableService;
import com.yijia.yy.service.dto.ProjectProgressTableDTO;
import com.yijia.yy.service.mapper.ProjectProgressTableMapper;

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
 * Test class for the ProjectProgressTableResource REST controller.
 *
 * @see ProjectProgressTableResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YiyingOaApp.class)
public class ProjectProgressTableResourceIntTest {


    private static final Long DEFAULT_CREATE_TIME = 1L;
    private static final Long UPDATED_CREATE_TIME = 2L;
    private static final String DEFAULT_MEMO = "AAAAA";
    private static final String UPDATED_MEMO = "BBBBB";

    @Inject
    private ProjectProgressTableRepository projectProgressTableRepository;

    @Inject
    private ProjectProgressTableMapper projectProgressTableMapper;

    @Inject
    private ProjectProgressTableService projectProgressTableService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restProjectProgressTableMockMvc;

    private ProjectProgressTable projectProgressTable;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProjectProgressTableResource projectProgressTableResource = new ProjectProgressTableResource();
        ReflectionTestUtils.setField(projectProgressTableResource, "projectProgressTableService", projectProgressTableService);
        this.restProjectProgressTableMockMvc = MockMvcBuilders.standaloneSetup(projectProgressTableResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjectProgressTable createEntity(EntityManager em) {
        ProjectProgressTable projectProgressTable = new ProjectProgressTable()
                .createTime(DEFAULT_CREATE_TIME)
                .memo(DEFAULT_MEMO);
        return projectProgressTable;
    }

    @Before
    public void initTest() {
        projectProgressTable = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjectProgressTable() throws Exception {
        int databaseSizeBeforeCreate = projectProgressTableRepository.findAll().size();

        // Create the ProjectProgressTable
        ProjectProgressTableDTO projectProgressTableDTO = projectProgressTableMapper.projectProgressTableToProjectProgressTableDTO(projectProgressTable);

        restProjectProgressTableMockMvc.perform(post("/api/project-progress-tables")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projectProgressTableDTO)))
                .andExpect(status().isCreated());

        // Validate the ProjectProgressTable in the database
        List<ProjectProgressTable> projectProgressTables = projectProgressTableRepository.findAll();
        assertThat(projectProgressTables).hasSize(databaseSizeBeforeCreate + 1);
        ProjectProgressTable testProjectProgressTable = projectProgressTables.get(projectProgressTables.size() - 1);
        assertThat(testProjectProgressTable.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testProjectProgressTable.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void getAllProjectProgressTables() throws Exception {
        // Initialize the database
        projectProgressTableRepository.saveAndFlush(projectProgressTable);

        // Get all the projectProgressTables
        restProjectProgressTableMockMvc.perform(get("/api/project-progress-tables?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(projectProgressTable.getId().intValue())))
                .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.intValue())))
                .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())));
    }

    @Test
    @Transactional
    public void getProjectProgressTable() throws Exception {
        // Initialize the database
        projectProgressTableRepository.saveAndFlush(projectProgressTable);

        // Get the projectProgressTable
        restProjectProgressTableMockMvc.perform(get("/api/project-progress-tables/{id}", projectProgressTable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projectProgressTable.getId().intValue()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.intValue()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProjectProgressTable() throws Exception {
        // Get the projectProgressTable
        restProjectProgressTableMockMvc.perform(get("/api/project-progress-tables/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjectProgressTable() throws Exception {
        // Initialize the database
        projectProgressTableRepository.saveAndFlush(projectProgressTable);
        int databaseSizeBeforeUpdate = projectProgressTableRepository.findAll().size();

        // Update the projectProgressTable
        ProjectProgressTable updatedProjectProgressTable = projectProgressTableRepository.findOne(projectProgressTable.getId());
        updatedProjectProgressTable
                .createTime(UPDATED_CREATE_TIME)
                .memo(UPDATED_MEMO);
        ProjectProgressTableDTO projectProgressTableDTO = projectProgressTableMapper.projectProgressTableToProjectProgressTableDTO(updatedProjectProgressTable);

        restProjectProgressTableMockMvc.perform(put("/api/project-progress-tables")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projectProgressTableDTO)))
                .andExpect(status().isOk());

        // Validate the ProjectProgressTable in the database
        List<ProjectProgressTable> projectProgressTables = projectProgressTableRepository.findAll();
        assertThat(projectProgressTables).hasSize(databaseSizeBeforeUpdate);
        ProjectProgressTable testProjectProgressTable = projectProgressTables.get(projectProgressTables.size() - 1);
        assertThat(testProjectProgressTable.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testProjectProgressTable.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void deleteProjectProgressTable() throws Exception {
        // Initialize the database
        projectProgressTableRepository.saveAndFlush(projectProgressTable);
        int databaseSizeBeforeDelete = projectProgressTableRepository.findAll().size();

        // Get the projectProgressTable
        restProjectProgressTableMockMvc.perform(delete("/api/project-progress-tables/{id}", projectProgressTable.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ProjectProgressTable> projectProgressTables = projectProgressTableRepository.findAll();
        assertThat(projectProgressTables).hasSize(databaseSizeBeforeDelete - 1);
    }
}
