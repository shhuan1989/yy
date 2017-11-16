package com.yijia.yy.web.rest;

import com.yijia.yy.YiyingOaApp;

import com.yijia.yy.domain.Vacation;
import com.yijia.yy.repository.VacationRepository;
import com.yijia.yy.service.VacationService;
import com.yijia.yy.service.dto.VacationDTO;
import com.yijia.yy.service.mapper.VacationMapper;

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
 * Test class for the VacationResource REST controller.
 *
 * @see VacationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YiyingOaApp.class)
public class VacationResourceIntTest {


    private static final Long DEFAULT_START_TIME = 1L;
    private static final Long UPDATED_START_TIME = 2L;

    private static final Long DEFAULT_END_TIME = 1L;
    private static final Long UPDATED_END_TIME = 2L;

    private static final Integer DEFAULT_DAYS = 1;
    private static final Integer UPDATED_DAYS = 2;
    private static final String DEFAULT_INFO = "AAAAA";
    private static final String UPDATED_INFO = "BBBBB";

    private static final Long DEFAULT_CREATE_TIME = 1L;
    private static final Long UPDATED_CREATE_TIME = 2L;

    @Inject
    private VacationRepository vacationRepository;

    @Inject
    private VacationMapper vacationMapper;

    @Inject
    private VacationService vacationService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restVacationMockMvc;

    private Vacation vacation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VacationResource vacationResource = new VacationResource();
        ReflectionTestUtils.setField(vacationResource, "vacationService", vacationService);
        this.restVacationMockMvc = MockMvcBuilders.standaloneSetup(vacationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vacation createEntity(EntityManager em) {
        Vacation vacation = new Vacation();
        return vacation;
    }

    @Before
    public void initTest() {
        vacation = createEntity(em);
    }

    @Test
    @Transactional
    public void createVacation() throws Exception {
        int databaseSizeBeforeCreate = vacationRepository.findAll().size();

        // Create the Vacation
        VacationDTO vacationDTO = vacationMapper.vacationToVacationDTO(vacation);

        restVacationMockMvc.perform(post("/api/vacations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(vacationDTO)))
                .andExpect(status().isCreated());

        // Validate the Vacation in the database
        List<Vacation> vacations = vacationRepository.findAll();
        assertThat(vacations).hasSize(databaseSizeBeforeCreate + 1);
        Vacation testVacation = vacations.get(vacations.size() - 1);
        assertThat(testVacation.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testVacation.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testVacation.getDays()).isEqualTo(DEFAULT_DAYS);
        assertThat(testVacation.getInfo()).isEqualTo(DEFAULT_INFO);
        assertThat(testVacation.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
    }

    @Test
    @Transactional
    public void checkStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = vacationRepository.findAll().size();
        // set the field null
        vacation.setStartTime(null);

        // Create the Vacation, which fails.
        VacationDTO vacationDTO = vacationMapper.vacationToVacationDTO(vacation);

        restVacationMockMvc.perform(post("/api/vacations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(vacationDTO)))
                .andExpect(status().isBadRequest());

        List<Vacation> vacations = vacationRepository.findAll();
        assertThat(vacations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = vacationRepository.findAll().size();
        // set the field null
        vacation.setEndTime(null);

        // Create the Vacation, which fails.
        VacationDTO vacationDTO = vacationMapper.vacationToVacationDTO(vacation);

        restVacationMockMvc.perform(post("/api/vacations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(vacationDTO)))
                .andExpect(status().isBadRequest());

        List<Vacation> vacations = vacationRepository.findAll();
        assertThat(vacations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDaysIsRequired() throws Exception {
        int databaseSizeBeforeTest = vacationRepository.findAll().size();
        // set the field null
        vacation.setDays(null);

        // Create the Vacation, which fails.
        VacationDTO vacationDTO = vacationMapper.vacationToVacationDTO(vacation);

        restVacationMockMvc.perform(post("/api/vacations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(vacationDTO)))
                .andExpect(status().isBadRequest());

        List<Vacation> vacations = vacationRepository.findAll();
        assertThat(vacations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVacations() throws Exception {
        // Initialize the database
        vacationRepository.saveAndFlush(vacation);

        // Get all the vacations
        restVacationMockMvc.perform(get("/api/vacations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(vacation.getId().intValue())))
                .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.intValue())))
                .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.intValue())))
                .andExpect(jsonPath("$.[*].days").value(hasItem(DEFAULT_DAYS)))
                .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO.toString())))
                .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.intValue())));
    }

    @Test
    @Transactional
    public void getVacation() throws Exception {
        // Initialize the database
        vacationRepository.saveAndFlush(vacation);

        // Get the vacation
        restVacationMockMvc.perform(get("/api/vacations/{id}", vacation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(vacation.getId().intValue()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.intValue()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.intValue()))
            .andExpect(jsonPath("$.days").value(DEFAULT_DAYS))
            .andExpect(jsonPath("$.info").value(DEFAULT_INFO.toString()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingVacation() throws Exception {
        // Get the vacation
        restVacationMockMvc.perform(get("/api/vacations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVacation() throws Exception {
        // Initialize the database
        vacationRepository.saveAndFlush(vacation);
        int databaseSizeBeforeUpdate = vacationRepository.findAll().size();

        // Update the vacation
        Vacation updatedVacation = vacationRepository.findOne(vacation.getId());
        VacationDTO vacationDTO = vacationMapper.vacationToVacationDTO(updatedVacation);

        restVacationMockMvc.perform(put("/api/vacations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(vacationDTO)))
                .andExpect(status().isOk());

        // Validate the Vacation in the database
        List<Vacation> vacations = vacationRepository.findAll();
        assertThat(vacations).hasSize(databaseSizeBeforeUpdate);
        Vacation testVacation = vacations.get(vacations.size() - 1);
        assertThat(testVacation.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testVacation.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testVacation.getDays()).isEqualTo(UPDATED_DAYS);
        assertThat(testVacation.getInfo()).isEqualTo(UPDATED_INFO);
        assertThat(testVacation.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    public void deleteVacation() throws Exception {
        // Initialize the database
        vacationRepository.saveAndFlush(vacation);
        int databaseSizeBeforeDelete = vacationRepository.findAll().size();

        // Get the vacation
        restVacationMockMvc.perform(delete("/api/vacations/{id}", vacation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Vacation> vacations = vacationRepository.findAll();
        assertThat(vacations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
