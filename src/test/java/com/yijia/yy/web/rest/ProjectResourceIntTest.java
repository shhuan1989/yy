package com.yijia.yy.web.rest;

import com.yijia.yy.YiyingOaApp;
import com.yijia.yy.domain.Dictionary;
import com.yijia.yy.domain.Project;
import com.yijia.yy.domain.enumeration.BooleanEnum;
import com.yijia.yy.domain.enumeration.HasEnum;
import com.yijia.yy.repository.ProjectRepository;
import com.yijia.yy.service.ProjectService;
import com.yijia.yy.service.dto.ProjectDTO;
import com.yijia.yy.service.mapper.ProjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProjectResource REST controller.
 *
 * @see ProjectResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YiyingOaApp.class)
public class ProjectResourceIntTest {

    private static final String DEFAULT_NO = "AAAAA";
    private static final String UPDATED_NO = "BBBBB";
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final HasEnum DEFAULT_HAS_CONTRACT = HasEnum.NO;
    private static final HasEnum UPDATED_HAS_CONTRACT = HasEnum.YES;

    private static final BooleanEnum DEFAULT_IS_NEW_CLIENT = BooleanEnum.YES;
    private static final BooleanEnum UPDATED_IS_NEW_CLIENT = BooleanEnum.NO;

    private static final String DEFAULT_BUDGET = "1w";
    private static final String UPDATED_BUDGET = "2w";
    private static final String DEFAULT_NEGOTIATOR = "AAAAA";
    private static final String UPDATED_NEGOTIATOR = "BBBBB";
    private static final String DEFAULT_TEL_NEGOTIATOR = "AAAAA";
    private static final String UPDATED_TEL_NEGOTIATOR = "BBBBB";
    private static final String DEFAULT_QQ_NEGOTIATOR = "AAAAA";
    private static final String UPDATED_QQ_NEGOTIATOR = "BBBBB";
    private static final String DEFAULT_WECHART_NEGOTIATOR = "AAAAA";
    private static final String UPDATED_WECHART_NEGOTIATOR = "BBBBB";
    private static final String DEFAULT_MAIL_NEGOTIATOR = "AAAAA";
    private static final String UPDATED_MAIL_NEGOTIATOR = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private ProjectRepository projectRepository;

    @Inject
    private ProjectMapper projectMapper;

    @Inject
    private ProjectService projectService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restProjectMockMvc;

    private Project project;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProjectResource projectResource = new ProjectResource();
        ReflectionTestUtils.setField(projectResource, "projectService", projectService);
        this.restProjectMockMvc = MockMvcBuilders.standaloneSetup(projectResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Project createEntity(EntityManager em) {
        Project project = new Project()
                .no(DEFAULT_NO)
                .name(DEFAULT_NAME)
                .hasContract(DEFAULT_HAS_CONTRACT)
                .isNewClient(DEFAULT_IS_NEW_CLIENT)
                .budget(DEFAULT_BUDGET)
                .negotiator(DEFAULT_NEGOTIATOR)
                .telNegotiator(DEFAULT_TEL_NEGOTIATOR)
                .qqNegotiator(DEFAULT_QQ_NEGOTIATOR)
                .wechartNegotiator(DEFAULT_WECHART_NEGOTIATOR)
                .mailNegotiator(DEFAULT_MAIL_NEGOTIATOR)
                .description(DEFAULT_DESCRIPTION);
        return project;
    }

    @Before
    public void initTest() {
        project = createEntity(em);
    }

    @Test
    @Transactional
    public void createProject() throws Exception {
        int databaseSizeBeforeCreate = projectRepository.findAll().size();

        // Create the Project
        ProjectDTO projectDTO = projectMapper.projectToProjectDTO(project);

        restProjectMockMvc.perform(post("/api/projects")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projectDTO)))
                .andExpect(status().isCreated());

        // Validate the Project in the database
        List<Project> projects = projectRepository.findAll();
        assertThat(projects).hasSize(databaseSizeBeforeCreate + 1);
        Project testProject = projects.get(projects.size() - 1);
        assertThat(testProject.getNo()).isEqualTo(DEFAULT_NO);
        assertThat(testProject.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProject.getHasContract()).isEqualTo(DEFAULT_HAS_CONTRACT);
        assertThat(testProject.getIsNewClient()).isEqualTo(DEFAULT_IS_NEW_CLIENT);
        assertThat(testProject.getBudget()).isEqualTo(DEFAULT_BUDGET);
        assertThat(testProject.getNegotiator()).isEqualTo(DEFAULT_NEGOTIATOR);
        assertThat(testProject.getTelNegotiator()).isEqualTo(DEFAULT_TEL_NEGOTIATOR);
        assertThat(testProject.getQqNegotiator()).isEqualTo(DEFAULT_QQ_NEGOTIATOR);
        assertThat(testProject.getWechartNegotiator()).isEqualTo(DEFAULT_WECHART_NEGOTIATOR);
        assertThat(testProject.getMailNegotiator()).isEqualTo(DEFAULT_MAIL_NEGOTIATOR);
        assertThat(testProject.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProjects() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projects
        restProjectMockMvc.perform(get("/api/projects?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(project.getId().intValue())))
                .andExpect(jsonPath("$.[*].no").value(hasItem(DEFAULT_NO.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].hasContract").value(hasItem(DEFAULT_HAS_CONTRACT)))
                .andExpect(jsonPath("$.[*].isNewClient").value(hasItem(DEFAULT_IS_NEW_CLIENT)))
                .andExpect(jsonPath("$.[*].budget").value(hasItem(DEFAULT_BUDGET)))
                .andExpect(jsonPath("$.[*].negotiator").value(hasItem(DEFAULT_NEGOTIATOR.toString())))
                .andExpect(jsonPath("$.[*].telNegotiator").value(hasItem(DEFAULT_TEL_NEGOTIATOR.toString())))
                .andExpect(jsonPath("$.[*].qqNegotiator").value(hasItem(DEFAULT_QQ_NEGOTIATOR.toString())))
                .andExpect(jsonPath("$.[*].wechartNegotiator").value(hasItem(DEFAULT_WECHART_NEGOTIATOR.toString())))
                .andExpect(jsonPath("$.[*].mailNegotiator").value(hasItem(DEFAULT_MAIL_NEGOTIATOR.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get the project
        restProjectMockMvc.perform(get("/api/projects/{id}", project.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(project.getId().intValue()))
            .andExpect(jsonPath("$.no").value(DEFAULT_NO.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.hasContract").value(DEFAULT_HAS_CONTRACT))
            .andExpect(jsonPath("$.isNewClient").value(DEFAULT_IS_NEW_CLIENT))
            .andExpect(jsonPath("$.budget").value(DEFAULT_BUDGET))
            .andExpect(jsonPath("$.negotiator").value(DEFAULT_NEGOTIATOR.toString()))
            .andExpect(jsonPath("$.telNegotiator").value(DEFAULT_TEL_NEGOTIATOR.toString()))
            .andExpect(jsonPath("$.qqNegotiator").value(DEFAULT_QQ_NEGOTIATOR.toString()))
            .andExpect(jsonPath("$.wechartNegotiator").value(DEFAULT_WECHART_NEGOTIATOR.toString()))
            .andExpect(jsonPath("$.mailNegotiator").value(DEFAULT_MAIL_NEGOTIATOR.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProject() throws Exception {
        // Get the project
        restProjectMockMvc.perform(get("/api/projects/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();

        // Update the project
        Project updatedProject = projectRepository.findOne(project.getId());
        updatedProject
                .no(UPDATED_NO)
                .name(UPDATED_NAME)
                .hasContract(UPDATED_HAS_CONTRACT)
                .isNewClient(UPDATED_IS_NEW_CLIENT)
                .budget(UPDATED_BUDGET)
                .negotiator(UPDATED_NEGOTIATOR)
                .telNegotiator(UPDATED_TEL_NEGOTIATOR)
                .qqNegotiator(UPDATED_QQ_NEGOTIATOR)
                .wechartNegotiator(UPDATED_WECHART_NEGOTIATOR)
                .mailNegotiator(UPDATED_MAIL_NEGOTIATOR)
                .description(UPDATED_DESCRIPTION);
        ProjectDTO projectDTO = projectMapper.projectToProjectDTO(updatedProject);

        restProjectMockMvc.perform(put("/api/projects")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projectDTO)))
                .andExpect(status().isOk());

        // Validate the Project in the database
        List<Project> projects = projectRepository.findAll();
        assertThat(projects).hasSize(databaseSizeBeforeUpdate);
        Project testProject = projects.get(projects.size() - 1);
        assertThat(testProject.getNo()).isEqualTo(UPDATED_NO);
        assertThat(testProject.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProject.getHasContract()).isEqualTo(UPDATED_HAS_CONTRACT);
        assertThat(testProject.getIsNewClient()).isEqualTo(UPDATED_IS_NEW_CLIENT);
        assertThat(testProject.getBudget()).isEqualTo(UPDATED_BUDGET);
        assertThat(testProject.getNegotiator()).isEqualTo(UPDATED_NEGOTIATOR);
        assertThat(testProject.getTelNegotiator()).isEqualTo(UPDATED_TEL_NEGOTIATOR);
        assertThat(testProject.getQqNegotiator()).isEqualTo(UPDATED_QQ_NEGOTIATOR);
        assertThat(testProject.getWechartNegotiator()).isEqualTo(UPDATED_WECHART_NEGOTIATOR);
        assertThat(testProject.getMailNegotiator()).isEqualTo(UPDATED_MAIL_NEGOTIATOR);
        assertThat(testProject.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);
        int databaseSizeBeforeDelete = projectRepository.findAll().size();

        // Get the project
        restProjectMockMvc.perform(delete("/api/projects/{id}", project.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Project> projects = projectRepository.findAll();
        assertThat(projects).hasSize(databaseSizeBeforeDelete - 1);
    }
}
