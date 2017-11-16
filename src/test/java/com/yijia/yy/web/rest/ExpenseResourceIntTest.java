package com.yijia.yy.web.rest;

import com.yijia.yy.yyOaApp;

import com.yijia.yy.domain.Expense;
import com.yijia.yy.repository.ExpenseRepository;
import com.yijia.yy.service.ExpenseService;
import com.yijia.yy.service.dto.ExpenseDTO;
import com.yijia.yy.service.mapper.ExpenseMapper;

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
 * Test class for the ExpenseResource REST controller.
 *
 * @see ExpenseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = yyOaApp.class)
public class ExpenseResourceIntTest {


    private static final Long DEFAULT_PAY_TIME = 1L;
    private static final Long UPDATED_PAY_TIME = 2L;

    @Inject
    private ExpenseRepository expenseRepository;

    @Inject
    private ExpenseMapper expenseMapper;

    @Inject
    private ExpenseService expenseService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restExpenseMockMvc;

    private Expense expense;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ExpenseResource expenseResource = new ExpenseResource();
        ReflectionTestUtils.setField(expenseResource, "expenseService", expenseService);
        this.restExpenseMockMvc = MockMvcBuilders.standaloneSetup(expenseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Expense createEntity(EntityManager em) {
        Expense expense = new Expense()
                .payTime(DEFAULT_PAY_TIME);
        return expense;
    }

    @Before
    public void initTest() {
        expense = createEntity(em);
    }

    @Test
    @Transactional
    public void createExpense() throws Exception {
        int databaseSizeBeforeCreate = expenseRepository.findAll().size();

        // Create the Expense
        ExpenseDTO expenseDTO = expenseMapper.expenseToExpenseDTO(expense);

        restExpenseMockMvc.perform(post("/api/expenses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(expenseDTO)))
                .andExpect(status().isCreated());

        // Validate the Expense in the database
        List<Expense> expenses = expenseRepository.findAll();
        assertThat(expenses).hasSize(databaseSizeBeforeCreate + 1);
        Expense testExpense = expenses.get(expenses.size() - 1);
        assertThat(testExpense.getPayTime()).isEqualTo(DEFAULT_PAY_TIME);
    }

    @Test
    @Transactional
    public void getAllExpenses() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenses
        restExpenseMockMvc.perform(get("/api/expenses?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(expense.getId().intValue())))
                .andExpect(jsonPath("$.[*].payTime").value(hasItem(DEFAULT_PAY_TIME.intValue())));
    }

    @Test
    @Transactional
    public void getExpense() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get the expense
        restExpenseMockMvc.perform(get("/api/expenses/{id}", expense.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(expense.getId().intValue()))
            .andExpect(jsonPath("$.payTime").value(DEFAULT_PAY_TIME.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingExpense() throws Exception {
        // Get the expense
        restExpenseMockMvc.perform(get("/api/expenses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExpense() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);
        int databaseSizeBeforeUpdate = expenseRepository.findAll().size();

        // Update the expense
        Expense updatedExpense = expenseRepository.findOne(expense.getId());
        updatedExpense
                .payTime(UPDATED_PAY_TIME);
        ExpenseDTO expenseDTO = expenseMapper.expenseToExpenseDTO(updatedExpense);

        restExpenseMockMvc.perform(put("/api/expenses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(expenseDTO)))
                .andExpect(status().isOk());

        // Validate the Expense in the database
        List<Expense> expenses = expenseRepository.findAll();
        assertThat(expenses).hasSize(databaseSizeBeforeUpdate);
        Expense testExpense = expenses.get(expenses.size() - 1);
        assertThat(testExpense.getPayTime()).isEqualTo(UPDATED_PAY_TIME);
    }

    @Test
    @Transactional
    public void deleteExpense() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);
        int databaseSizeBeforeDelete = expenseRepository.findAll().size();

        // Get the expense
        restExpenseMockMvc.perform(delete("/api/expenses/{id}", expense.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Expense> expenses = expenseRepository.findAll();
        assertThat(expenses).hasSize(databaseSizeBeforeDelete - 1);
    }
}
