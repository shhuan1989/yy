package com.yijia.yy.web.rest;

import com.yijia.yy.yyOaApp;

import com.yijia.yy.domain.ProjectPayment;
import com.yijia.yy.repository.ProjectPaymentRepository;
import com.yijia.yy.service.ProjectPaymentService;
import com.yijia.yy.service.dto.ProjectPaymentDTO;
import com.yijia.yy.service.mapper.ProjectPaymentMapper;

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
 * Test class for the ProjectPaymentResource REST controller.
 *
 * @see ProjectPaymentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = yyOaApp.class)
public class ProjectPaymentResourceIntTest {


    private static final Long DEFAULT_APPOINTED_TIME = 1L;
    private static final Long UPDATED_APPOINTED_TIME = 2L;

    private static final Float DEFAULT_AMOUNT = 0F;
    private static final Float UPDATED_AMOUNT = 1F;

    private static final Long DEFAULT_PAY_TIME = 1L;
    private static final Long UPDATED_PAY_TIME = 2L;

    @Inject
    private ProjectPaymentRepository projectPaymentRepository;

    @Inject
    private ProjectPaymentMapper projectPaymentMapper;

    @Inject
    private ProjectPaymentService projectPaymentService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restProjectPaymentMockMvc;

    private ProjectPayment projectPayment;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProjectPaymentResource projectPaymentResource = new ProjectPaymentResource();
        ReflectionTestUtils.setField(projectPaymentResource, "projectPaymentService", projectPaymentService);
        this.restProjectPaymentMockMvc = MockMvcBuilders.standaloneSetup(projectPaymentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjectPayment createEntity(EntityManager em) {
        ProjectPayment projectPayment = new ProjectPayment()
                .appointedTime(DEFAULT_APPOINTED_TIME)
                .amount(DEFAULT_AMOUNT)
                .payTime(DEFAULT_PAY_TIME);
        return projectPayment;
    }

    @Before
    public void initTest() {
        projectPayment = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjectPayment() throws Exception {
        int databaseSizeBeforeCreate = projectPaymentRepository.findAll().size();

        // Create the ProjectPayment
        ProjectPaymentDTO projectPaymentDTO = projectPaymentMapper.projectPaymentToProjectPaymentDTO(projectPayment);

        restProjectPaymentMockMvc.perform(post("/api/project-payments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projectPaymentDTO)))
                .andExpect(status().isCreated());

        // Validate the ProjectPayment in the database
        List<ProjectPayment> projectPayments = projectPaymentRepository.findAll();
        assertThat(projectPayments).hasSize(databaseSizeBeforeCreate + 1);
        ProjectPayment testProjectPayment = projectPayments.get(projectPayments.size() - 1);
        assertThat(testProjectPayment.getAppointedTime()).isEqualTo(DEFAULT_APPOINTED_TIME);
        assertThat(testProjectPayment.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testProjectPayment.getPayTime()).isEqualTo(DEFAULT_PAY_TIME);
    }

    @Test
    @Transactional
    public void checkAppointedTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectPaymentRepository.findAll().size();
        // set the field null
        projectPayment.setAppointedTime(null);

        // Create the ProjectPayment, which fails.
        ProjectPaymentDTO projectPaymentDTO = projectPaymentMapper.projectPaymentToProjectPaymentDTO(projectPayment);

        restProjectPaymentMockMvc.perform(post("/api/project-payments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projectPaymentDTO)))
                .andExpect(status().isBadRequest());

        List<ProjectPayment> projectPayments = projectPaymentRepository.findAll();
        assertThat(projectPayments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectPaymentRepository.findAll().size();
        // set the field null
        projectPayment.setAmount(null);

        // Create the ProjectPayment, which fails.
        ProjectPaymentDTO projectPaymentDTO = projectPaymentMapper.projectPaymentToProjectPaymentDTO(projectPayment);

        restProjectPaymentMockMvc.perform(post("/api/project-payments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projectPaymentDTO)))
                .andExpect(status().isBadRequest());

        List<ProjectPayment> projectPayments = projectPaymentRepository.findAll();
        assertThat(projectPayments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProjectPayments() throws Exception {
        // Initialize the database
        projectPaymentRepository.saveAndFlush(projectPayment);

        // Get all the projectPayments
        restProjectPaymentMockMvc.perform(get("/api/project-payments?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(projectPayment.getId().intValue())))
                .andExpect(jsonPath("$.[*].appointedTime").value(hasItem(DEFAULT_APPOINTED_TIME.intValue())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
                .andExpect(jsonPath("$.[*].payTime").value(hasItem(DEFAULT_PAY_TIME.intValue())));
    }

    @Test
    @Transactional
    public void getProjectPayment() throws Exception {
        // Initialize the database
        projectPaymentRepository.saveAndFlush(projectPayment);

        // Get the projectPayment
        restProjectPaymentMockMvc.perform(get("/api/project-payments/{id}", projectPayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projectPayment.getId().intValue()))
            .andExpect(jsonPath("$.appointedTime").value(DEFAULT_APPOINTED_TIME.intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.payTime").value(DEFAULT_PAY_TIME.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingProjectPayment() throws Exception {
        // Get the projectPayment
        restProjectPaymentMockMvc.perform(get("/api/project-payments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjectPayment() throws Exception {
        // Initialize the database
        projectPaymentRepository.saveAndFlush(projectPayment);
        int databaseSizeBeforeUpdate = projectPaymentRepository.findAll().size();

        // Update the projectPayment
        ProjectPayment updatedProjectPayment = projectPaymentRepository.findOne(projectPayment.getId());
        updatedProjectPayment
                .appointedTime(UPDATED_APPOINTED_TIME)
                .amount(UPDATED_AMOUNT)
                .payTime(UPDATED_PAY_TIME);
        ProjectPaymentDTO projectPaymentDTO = projectPaymentMapper.projectPaymentToProjectPaymentDTO(updatedProjectPayment);

        restProjectPaymentMockMvc.perform(put("/api/project-payments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(projectPaymentDTO)))
                .andExpect(status().isOk());

        // Validate the ProjectPayment in the database
        List<ProjectPayment> projectPayments = projectPaymentRepository.findAll();
        assertThat(projectPayments).hasSize(databaseSizeBeforeUpdate);
        ProjectPayment testProjectPayment = projectPayments.get(projectPayments.size() - 1);
        assertThat(testProjectPayment.getAppointedTime()).isEqualTo(UPDATED_APPOINTED_TIME);
        assertThat(testProjectPayment.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testProjectPayment.getPayTime()).isEqualTo(UPDATED_PAY_TIME);
    }

    @Test
    @Transactional
    public void deleteProjectPayment() throws Exception {
        // Initialize the database
        projectPaymentRepository.saveAndFlush(projectPayment);
        int databaseSizeBeforeDelete = projectPaymentRepository.findAll().size();

        // Get the projectPayment
        restProjectPaymentMockMvc.perform(delete("/api/project-payments/{id}", projectPayment.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ProjectPayment> projectPayments = projectPaymentRepository.findAll();
        assertThat(projectPayments).hasSize(databaseSizeBeforeDelete - 1);
    }
}
