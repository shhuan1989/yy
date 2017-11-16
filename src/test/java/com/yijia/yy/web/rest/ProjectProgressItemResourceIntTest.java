package com.yijia.yy.web.rest;

import com.yijia.yy.yyOaApp;

import com.yijia.yy.domain.ProjectProgressItem;
import com.yijia.yy.repository.ProjectProgressItemRepository;
import com.yijia.yy.service.ProjectProgressItemService;
import com.yijia.yy.service.dto.ProjectProgressItemDTO;
import com.yijia.yy.service.mapper.ProjectProgressItemMapper;

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
 * Test class for the ProjectProgressItemResource REST controller.
 *
 * @see ProjectProgressItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = yyOaApp.class)
public class ProjectProgressItemResourceIntTest {


    private static final Long DEFAULT_ETA = 1L;
    private static final Long UPDATED_ETA = 2L;

    private static final Long DEFAULT_FINISH_TIME = 1L;
    private static final Long UPDATED_FINISH_TIME = 2L;

    @Inject
    private ProjectProgressItemRepository projectProgressItemRepository;

    @Inject
    private ProjectProgressItemMapper projectProgressItemMapper;

    @Inject
    private ProjectProgressItemService projectProgressItemService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restProjectProgressItemMockMvc;

    private ProjectProgressItem projectProgressItem;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProjectProgressItemResource projectProgressItemResource = new ProjectProgressItemResource();
        ReflectionTestUtils.setField(projectProgressItemResource, "projectProgressItemService", projectProgressItemService);
        this.restProjectProgressItemMockMvc = MockMvcBuilders.standaloneSetup(projectProgressItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjectProgressItem createEntity(EntityManager em) {
        ProjectProgressItem projectProgressItem = new ProjectProgressItem()
                .eta(DEFAULT_ETA)
                .finishTime(DEFAULT_FINISH_TIME);
        return projectProgressItem;
    }

    @Before
    public void initTest() {
        projectProgressItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjectProgressItem() throws Exception {
        int databaseSizeBeforeCreate = projectProgressItemRepository.findAll().size();

        // Create the ProjectProgressItem
        ProjectProgressItemDTO projectProgressItemDTO = projectProgressItemMapper.projectProgressItemToProjectProgressItemDTO(projectProgressItem);

        restProjectProgressItemMockMvc.perform(post("/api/project-progress-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projectProgressItemDTO)))
                .andExpect(status().isCreated());

        // Validate the ProjectProgressItem in the database
        List<ProjectProgressItem> projectProgressItems = projectProgressItemRepository.findAll();
        assertThat(projectProgressItems).hasSize(databaseSizeBeforeCreate + 1);
        ProjectProgressItem testProjectProgressItem = projectProgressItems.get(projectProgressItems.size() - 1);
        assertThat(testProjectProgressItem.getEta()).isEqualTo(DEFAULT_ETA);
        assertThat(testProjectProgressItem.getFinishTime()).isEqualTo(DEFAULT_FINISH_TIME);
    }

    @Test
    @Transactional
    public void getAllProjectProgressItems() throws Exception {
        // Initialize the database
        projectProgressItemRepository.saveAndFlush(projectProgressItem);

        // Get all the projectProgressItems
        restProjectProgressItemMockMvc.perform(get("/api/project-progress-items?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(projectProgressItem.getId().intValue())))
                .andExpect(jsonPath("$.[*].eta").value(hasItem(DEFAULT_ETA.intValue())))
                .andExpect(jsonPath("$.[*].finishTime").value(hasItem(DEFAULT_FINISH_TIME.intValue())));
    }

    @Test
    @Transactional
    public void getProjectProgressItem() throws Exception {
        // Initialize the database
        projectProgressItemRepository.saveAndFlush(projectProgressItem);

        // Get the projectProgressItem
        restProjectProgressItemMockMvc.perform(get("/api/project-progress-items/{id}", projectProgressItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projectProgressItem.getId().intValue()))
            .andExpect(jsonPath("$.eta").value(DEFAULT_ETA.intValue()))
            .andExpect(jsonPath("$.finishTime").value(DEFAULT_FINISH_TIME.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingProjectProgressItem() throws Exception {
        // Get the projectProgressItem
        restProjectProgressItemMockMvc.perform(get("/api/project-progress-items/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjectProgressItem() throws Exception {
        // Initialize the database
        projectProgressItemRepository.saveAndFlush(projectProgressItem);
        int databaseSizeBeforeUpdate = projectProgressItemRepository.findAll().size();

        // Update the projectProgressItem
        ProjectProgressItem updatedProjectProgressItem = projectProgressItemRepository.findOne(projectProgressItem.getId());
        updatedProjectProgressItem
                .eta(UPDATED_ETA)
                .finishTime(UPDATED_FINISH_TIME);
        ProjectProgressItemDTO projectProgressItemDTO = projectProgressItemMapper.projectProgressItemToProjectProgressItemDTO(updatedProjectProgressItem);

        restProjectProgressItemMockMvc.perform(put("/api/project-progress-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projectProgressItemDTO)))
                .andExpect(status().isOk());

        // Validate the ProjectProgressItem in the database
        List<ProjectProgressItem> projectProgressItems = projectProgressItemRepository.findAll();
        assertThat(projectProgressItems).hasSize(databaseSizeBeforeUpdate);
        ProjectProgressItem testProjectProgressItem = projectProgressItems.get(projectProgressItems.size() - 1);
        assertThat(testProjectProgressItem.getEta()).isEqualTo(UPDATED_ETA);
        assertThat(testProjectProgressItem.getFinishTime()).isEqualTo(UPDATED_FINISH_TIME);
    }

    @Test
    @Transactional
    public void deleteProjectProgressItem() throws Exception {
        // Initialize the database
        projectProgressItemRepository.saveAndFlush(projectProgressItem);
        int databaseSizeBeforeDelete = projectProgressItemRepository.findAll().size();

        // Get the projectProgressItem
        restProjectProgressItemMockMvc.perform(delete("/api/project-progress-items/{id}", projectProgressItem.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ProjectProgressItem> projectProgressItems = projectProgressItemRepository.findAll();
        assertThat(projectProgressItems).hasSize(databaseSizeBeforeDelete - 1);
    }
}
