package com.yijia.yy.web.rest;

import com.yijia.yy.yyOaApp;

import com.yijia.yy.domain.JobTitle;
import com.yijia.yy.repository.JobTitleRepository;
import com.yijia.yy.service.JobTitleService;
import com.yijia.yy.service.dto.JobTitleDTO;
import com.yijia.yy.service.mapper.JobTitleMapper;

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
 * Test class for the JobTitleResource REST controller.
 *
 * @see JobTitleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = yyOaApp.class)
public class JobTitleResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private JobTitleRepository jobTitleRepository;

    @Inject
    private JobTitleMapper jobTitleMapper;

    @Inject
    private JobTitleService jobTitleService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restJobTitleMockMvc;

    private JobTitle jobTitle;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        JobTitleResource jobTitleResource = new JobTitleResource();
        ReflectionTestUtils.setField(jobTitleResource, "jobTitleService", jobTitleService);
        this.restJobTitleMockMvc = MockMvcBuilders.standaloneSetup(jobTitleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobTitle createEntity(EntityManager em) {
        JobTitle jobTitle = new JobTitle()
                .name(DEFAULT_NAME);
        return jobTitle;
    }

    @Before
    public void initTest() {
        jobTitle = createEntity(em);
    }

    @Test
    @Transactional
    public void createJobTitle() throws Exception {
        int databaseSizeBeforeCreate = jobTitleRepository.findAll().size();

        // Create the JobTitle
        JobTitleDTO jobTitleDTO = jobTitleMapper.jobTitleToJobTitleDTO(jobTitle);

        restJobTitleMockMvc.perform(post("/api/job-titles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobTitleDTO)))
                .andExpect(status().isCreated());

        // Validate the JobTitle in the database
        List<JobTitle> jobTitles = jobTitleRepository.findAll();
        assertThat(jobTitles).hasSize(databaseSizeBeforeCreate + 1);
        JobTitle testJobTitle = jobTitles.get(jobTitles.size() - 1);
        assertThat(testJobTitle.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllJobTitles() throws Exception {
        // Initialize the database
        jobTitleRepository.saveAndFlush(jobTitle);

        // Get all the jobTitles
        restJobTitleMockMvc.perform(get("/api/job-titles?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(jobTitle.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getJobTitle() throws Exception {
        // Initialize the database
        jobTitleRepository.saveAndFlush(jobTitle);

        // Get the jobTitle
        restJobTitleMockMvc.perform(get("/api/job-titles/{id}", jobTitle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(jobTitle.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingJobTitle() throws Exception {
        // Get the jobTitle
        restJobTitleMockMvc.perform(get("/api/job-titles/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobTitle() throws Exception {
        // Initialize the database
        jobTitleRepository.saveAndFlush(jobTitle);
        int databaseSizeBeforeUpdate = jobTitleRepository.findAll().size();

        // Update the jobTitle
        JobTitle updatedJobTitle = jobTitleRepository.findOne(jobTitle.getId());
        updatedJobTitle
                .name(UPDATED_NAME);
        JobTitleDTO jobTitleDTO = jobTitleMapper.jobTitleToJobTitleDTO(updatedJobTitle);

        restJobTitleMockMvc.perform(put("/api/job-titles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(jobTitleDTO)))
                .andExpect(status().isOk());

        // Validate the JobTitle in the database
        List<JobTitle> jobTitles = jobTitleRepository.findAll();
        assertThat(jobTitles).hasSize(databaseSizeBeforeUpdate);
        JobTitle testJobTitle = jobTitles.get(jobTitles.size() - 1);
        assertThat(testJobTitle.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteJobTitle() throws Exception {
        // Initialize the database
        jobTitleRepository.saveAndFlush(jobTitle);
        int databaseSizeBeforeDelete = jobTitleRepository.findAll().size();

        // Get the jobTitle
        restJobTitleMockMvc.perform(delete("/api/job-titles/{id}", jobTitle.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<JobTitle> jobTitles = jobTitleRepository.findAll();
        assertThat(jobTitles).hasSize(databaseSizeBeforeDelete - 1);
    }
}
