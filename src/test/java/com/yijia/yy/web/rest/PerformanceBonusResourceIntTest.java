package com.yijia.yy.web.rest;

import com.yijia.yy.YiyingOaApp;

import com.yijia.yy.domain.PerformanceBonus;
import com.yijia.yy.repository.PerformanceBonusRepository;
import com.yijia.yy.service.PerformanceBonusService;
import com.yijia.yy.service.dto.PerformanceBonusDTO;
import com.yijia.yy.service.mapper.PerformanceBonusMapper;

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
 * Test class for the PerformanceBonusResource REST controller.
 *
 * @see PerformanceBonusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YiyingOaApp.class)
public class PerformanceBonusResourceIntTest {


    private static final Float DEFAULT_AMOUNT = 1F;
    private static final Float UPDATED_AMOUNT = 2F;

    private static final Boolean DEFAULT_ISSUED = false;
    private static final Boolean UPDATED_ISSUED = true;

    private static final Long DEFAULT_CREATE_TIME = 1L;
    private static final Long UPDATED_CREATE_TIME = 2L;

    private static final Long DEFAULT_ISSUE_TIME = 1L;
    private static final Long UPDATED_ISSUE_TIME = 2L;

    private static final Long DEFAULT_LAST_MODIFIED_TIME = 1L;
    private static final Long UPDATED_LAST_MODIFIED_TIME = 2L;

    @Inject
    private PerformanceBonusRepository performanceBonusRepository;

    @Inject
    private PerformanceBonusMapper performanceBonusMapper;

    @Inject
    private PerformanceBonusService performanceBonusService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPerformanceBonusMockMvc;

    private PerformanceBonus performanceBonus;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PerformanceBonusResource performanceBonusResource = new PerformanceBonusResource();
        ReflectionTestUtils.setField(performanceBonusResource, "performanceBonusService", performanceBonusService);
        this.restPerformanceBonusMockMvc = MockMvcBuilders.standaloneSetup(performanceBonusResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PerformanceBonus createEntity(EntityManager em) {
        PerformanceBonus performanceBonus = new PerformanceBonus()
                .amount(DEFAULT_AMOUNT)
                .createTime(DEFAULT_CREATE_TIME)
                .lastModifiedTime(DEFAULT_LAST_MODIFIED_TIME);
        return performanceBonus;
    }

    @Before
    public void initTest() {
        performanceBonus = createEntity(em);
    }

    @Test
    @Transactional
    public void createPerformanceBonus() throws Exception {
        int databaseSizeBeforeCreate = performanceBonusRepository.findAll().size();

        // Create the PerformanceBonus
        PerformanceBonusDTO performanceBonusDTO = performanceBonusMapper.performanceBonusToPerformanceBonusDTO(performanceBonus);

        restPerformanceBonusMockMvc.perform(post("/api/performance-bonuses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(performanceBonusDTO)))
                .andExpect(status().isCreated());

        // Validate the PerformanceBonus in the database
        List<PerformanceBonus> performanceBonuses = performanceBonusRepository.findAll();
        assertThat(performanceBonuses).hasSize(databaseSizeBeforeCreate + 1);
        PerformanceBonus testPerformanceBonus = performanceBonuses.get(performanceBonuses.size() - 1);
        assertThat(testPerformanceBonus.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testPerformanceBonus.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testPerformanceBonus.getLastModifiedTime()).isEqualTo(DEFAULT_LAST_MODIFIED_TIME);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = performanceBonusRepository.findAll().size();
        // set the field null
        performanceBonus.setAmount(null);

        // Create the PerformanceBonus, which fails.
        PerformanceBonusDTO performanceBonusDTO = performanceBonusMapper.performanceBonusToPerformanceBonusDTO(performanceBonus);

        restPerformanceBonusMockMvc.perform(post("/api/performance-bonuses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(performanceBonusDTO)))
                .andExpect(status().isBadRequest());

        List<PerformanceBonus> performanceBonuses = performanceBonusRepository.findAll();
        assertThat(performanceBonuses).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = performanceBonusRepository.findAll().size();
        // set the field null
        performanceBonus.setCreateTime(null);

        // Create the PerformanceBonus, which fails.
        PerformanceBonusDTO performanceBonusDTO = performanceBonusMapper.performanceBonusToPerformanceBonusDTO(performanceBonus);

        restPerformanceBonusMockMvc.perform(post("/api/performance-bonuses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(performanceBonusDTO)))
                .andExpect(status().isBadRequest());

        List<PerformanceBonus> performanceBonuses = performanceBonusRepository.findAll();
        assertThat(performanceBonuses).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPerformanceBonuses() throws Exception {
        // Initialize the database
        performanceBonusRepository.saveAndFlush(performanceBonus);

        // Get all the performanceBonuses
        restPerformanceBonusMockMvc.perform(get("/api/performance-bonuses?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(performanceBonus.getId().intValue())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
                .andExpect(jsonPath("$.[*].issued").value(hasItem(DEFAULT_ISSUED.booleanValue())))
                .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.intValue())))
                .andExpect(jsonPath("$.[*].issueTime").value(hasItem(DEFAULT_ISSUE_TIME.intValue())))
                .andExpect(jsonPath("$.[*].lastModifiedTime").value(hasItem(DEFAULT_LAST_MODIFIED_TIME.intValue())));
    }

    @Test
    @Transactional
    public void getPerformanceBonus() throws Exception {
        // Initialize the database
        performanceBonusRepository.saveAndFlush(performanceBonus);

        // Get the performanceBonus
        restPerformanceBonusMockMvc.perform(get("/api/performance-bonuses/{id}", performanceBonus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(performanceBonus.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.issued").value(DEFAULT_ISSUED.booleanValue()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.intValue()))
            .andExpect(jsonPath("$.issueTime").value(DEFAULT_ISSUE_TIME.intValue()))
            .andExpect(jsonPath("$.lastModifiedTime").value(DEFAULT_LAST_MODIFIED_TIME.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPerformanceBonus() throws Exception {
        // Get the performanceBonus
        restPerformanceBonusMockMvc.perform(get("/api/performance-bonuses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePerformanceBonus() throws Exception {
        // Initialize the database
        performanceBonusRepository.saveAndFlush(performanceBonus);
        int databaseSizeBeforeUpdate = performanceBonusRepository.findAll().size();

        // Update the performanceBonus
        PerformanceBonus updatedPerformanceBonus = performanceBonusRepository.findOne(performanceBonus.getId());
        updatedPerformanceBonus
                .amount(UPDATED_AMOUNT)
                .createTime(UPDATED_CREATE_TIME)
                .lastModifiedTime(UPDATED_LAST_MODIFIED_TIME);
        PerformanceBonusDTO performanceBonusDTO = performanceBonusMapper.performanceBonusToPerformanceBonusDTO(updatedPerformanceBonus);

        restPerformanceBonusMockMvc.perform(put("/api/performance-bonuses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(performanceBonusDTO)))
                .andExpect(status().isOk());

        // Validate the PerformanceBonus in the database
        List<PerformanceBonus> performanceBonuses = performanceBonusRepository.findAll();
        assertThat(performanceBonuses).hasSize(databaseSizeBeforeUpdate);
        PerformanceBonus testPerformanceBonus = performanceBonuses.get(performanceBonuses.size() - 1);
        assertThat(testPerformanceBonus.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPerformanceBonus.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testPerformanceBonus.getLastModifiedTime()).isEqualTo(UPDATED_LAST_MODIFIED_TIME);
    }

    @Test
    @Transactional
    public void deletePerformanceBonus() throws Exception {
        // Initialize the database
        performanceBonusRepository.saveAndFlush(performanceBonus);
        int databaseSizeBeforeDelete = performanceBonusRepository.findAll().size();

        // Get the performanceBonus
        restPerformanceBonusMockMvc.perform(delete("/api/performance-bonuses/{id}", performanceBonus.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PerformanceBonus> performanceBonuses = performanceBonusRepository.findAll();
        assertThat(performanceBonuses).hasSize(databaseSizeBeforeDelete - 1);
    }
}
