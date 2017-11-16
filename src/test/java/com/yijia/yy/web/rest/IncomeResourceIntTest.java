package com.yijia.yy.web.rest;

import com.yijia.yy.YiyingOaApp;

import com.yijia.yy.domain.Income;
import com.yijia.yy.repository.IncomeRepository;
import com.yijia.yy.service.IncomeService;
import com.yijia.yy.service.dto.IncomeDTO;
import com.yijia.yy.service.mapper.IncomeMapper;

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
 * Test class for the IncomeResource REST controller.
 *
 * @see IncomeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YiyingOaApp.class)
public class IncomeResourceIntTest {


    private static final Long DEFAULT_INCOME_TIME = 1L;
    private static final Long UPDATED_INCOME_TIME = 2L;

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;
    private static final String DEFAULT_INCOME_DESC = "AAAAA";
    private static final String UPDATED_INCOME_DESC = "BBBBB";
    private static final String DEFAULT_MEMO = "AAAAA";
    private static final String UPDATED_MEMO = "BBBBB";

    @Inject
    private IncomeRepository incomeRepository;

    @Inject
    private IncomeMapper incomeMapper;

    @Inject
    private IncomeService incomeService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restIncomeMockMvc;

    private Income income;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        IncomeResource incomeResource = new IncomeResource();
        ReflectionTestUtils.setField(incomeResource, "incomeService", incomeService);
        this.restIncomeMockMvc = MockMvcBuilders.standaloneSetup(incomeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Income createEntity(EntityManager em) {
        Income income = new Income()
                .incomeTime(DEFAULT_INCOME_TIME)
                .amount(DEFAULT_AMOUNT)
                .incomeDesc(DEFAULT_INCOME_DESC)
                .memo(DEFAULT_MEMO);
        return income;
    }

    @Before
    public void initTest() {
        income = createEntity(em);
    }

    @Test
    @Transactional
    public void createIncome() throws Exception {
        int databaseSizeBeforeCreate = incomeRepository.findAll().size();

        // Create the Income
        IncomeDTO incomeDTO = incomeMapper.incomeToIncomeDTO(income);

        restIncomeMockMvc.perform(post("/api/incomes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(incomeDTO)))
                .andExpect(status().isCreated());

        // Validate the Income in the database
        List<Income> incomes = incomeRepository.findAll();
        assertThat(incomes).hasSize(databaseSizeBeforeCreate + 1);
        Income testIncome = incomes.get(incomes.size() - 1);
        assertThat(testIncome.getIncomeTime()).isEqualTo(DEFAULT_INCOME_TIME);
        assertThat(testIncome.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testIncome.getIncomeDesc()).isEqualTo(DEFAULT_INCOME_DESC);
        assertThat(testIncome.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void getAllIncomes() throws Exception {
        // Initialize the database
        incomeRepository.saveAndFlush(income);

        // Get all the incomes
        restIncomeMockMvc.perform(get("/api/incomes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(income.getId().intValue())))
                .andExpect(jsonPath("$.[*].incomeTime").value(hasItem(DEFAULT_INCOME_TIME.intValue())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
                .andExpect(jsonPath("$.[*].incomeDesc").value(hasItem(DEFAULT_INCOME_DESC.toString())))
                .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())));
    }

    @Test
    @Transactional
    public void getIncome() throws Exception {
        // Initialize the database
        incomeRepository.saveAndFlush(income);

        // Get the income
        restIncomeMockMvc.perform(get("/api/incomes/{id}", income.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(income.getId().intValue()))
            .andExpect(jsonPath("$.incomeTime").value(DEFAULT_INCOME_TIME.intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.incomeDesc").value(DEFAULT_INCOME_DESC.toString()))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingIncome() throws Exception {
        // Get the income
        restIncomeMockMvc.perform(get("/api/incomes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIncome() throws Exception {
        // Initialize the database
        incomeRepository.saveAndFlush(income);
        int databaseSizeBeforeUpdate = incomeRepository.findAll().size();

        // Update the income
        Income updatedIncome = incomeRepository.findOne(income.getId());
        updatedIncome
                .incomeTime(UPDATED_INCOME_TIME)
                .amount(UPDATED_AMOUNT)
                .incomeDesc(UPDATED_INCOME_DESC)
                .memo(UPDATED_MEMO);
        IncomeDTO incomeDTO = incomeMapper.incomeToIncomeDTO(updatedIncome);

        restIncomeMockMvc.perform(put("/api/incomes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(incomeDTO)))
                .andExpect(status().isOk());

        // Validate the Income in the database
        List<Income> incomes = incomeRepository.findAll();
        assertThat(incomes).hasSize(databaseSizeBeforeUpdate);
        Income testIncome = incomes.get(incomes.size() - 1);
        assertThat(testIncome.getIncomeTime()).isEqualTo(UPDATED_INCOME_TIME);
        assertThat(testIncome.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testIncome.getIncomeDesc()).isEqualTo(UPDATED_INCOME_DESC);
        assertThat(testIncome.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void deleteIncome() throws Exception {
        // Initialize the database
        incomeRepository.saveAndFlush(income);
        int databaseSizeBeforeDelete = incomeRepository.findAll().size();

        // Get the income
        restIncomeMockMvc.perform(delete("/api/incomes/{id}", income.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Income> incomes = incomeRepository.findAll();
        assertThat(incomes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
