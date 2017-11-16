package com.yijia.yy.web.rest;

import com.yijia.yy.YiyingOaApp;

import com.yijia.yy.domain.ExpenseItem;
import com.yijia.yy.repository.ExpenseItemRepository;
import com.yijia.yy.service.ExpenseItemService;
import com.yijia.yy.service.dto.ExpenseItemDTO;
import com.yijia.yy.service.mapper.ExpenseItemMapper;

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
 * Test class for the ExpenseItemResource REST controller.
 *
 * @see ExpenseItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YiyingOaApp.class)
public class ExpenseItemResourceIntTest {

    private static final String DEFAULT_PURPOSE = "AAAAA";
    private static final String UPDATED_PURPOSE = "BBBBB";

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    @Inject
    private ExpenseItemRepository expenseItemRepository;

    @Inject
    private ExpenseItemMapper expenseItemMapper;

    @Inject
    private ExpenseItemService expenseItemService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restExpenseItemMockMvc;

    private ExpenseItem expenseItem;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ExpenseItemResource expenseItemResource = new ExpenseItemResource();
        ReflectionTestUtils.setField(expenseItemResource, "expenseItemService", expenseItemService);
        this.restExpenseItemMockMvc = MockMvcBuilders.standaloneSetup(expenseItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExpenseItem createEntity(EntityManager em) {
        ExpenseItem expenseItem = new ExpenseItem()
                .purpose(DEFAULT_PURPOSE)
                .amount(DEFAULT_AMOUNT);
        return expenseItem;
    }

    @Before
    public void initTest() {
        expenseItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createExpenseItem() throws Exception {
        int databaseSizeBeforeCreate = expenseItemRepository.findAll().size();

        // Create the ExpenseItem
        ExpenseItemDTO expenseItemDTO = expenseItemMapper.expenseItemToExpenseItemDTO(expenseItem);

        restExpenseItemMockMvc.perform(post("/api/expense-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(expenseItemDTO)))
                .andExpect(status().isCreated());

        // Validate the ExpenseItem in the database
        List<ExpenseItem> expenseItems = expenseItemRepository.findAll();
        assertThat(expenseItems).hasSize(databaseSizeBeforeCreate + 1);
        ExpenseItem testExpenseItem = expenseItems.get(expenseItems.size() - 1);
        assertThat(testExpenseItem.getPurpose()).isEqualTo(DEFAULT_PURPOSE);
        assertThat(testExpenseItem.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllExpenseItems() throws Exception {
        // Initialize the database
        expenseItemRepository.saveAndFlush(expenseItem);

        // Get all the expenseItems
        restExpenseItemMockMvc.perform(get("/api/expense-items?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(expenseItem.getId().intValue())))
                .andExpect(jsonPath("$.[*].purpose").value(hasItem(DEFAULT_PURPOSE.toString())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())));
    }

    @Test
    @Transactional
    public void getExpenseItem() throws Exception {
        // Initialize the database
        expenseItemRepository.saveAndFlush(expenseItem);

        // Get the expenseItem
        restExpenseItemMockMvc.perform(get("/api/expense-items/{id}", expenseItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(expenseItem.getId().intValue()))
            .andExpect(jsonPath("$.purpose").value(DEFAULT_PURPOSE.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingExpenseItem() throws Exception {
        // Get the expenseItem
        restExpenseItemMockMvc.perform(get("/api/expense-items/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExpenseItem() throws Exception {
        // Initialize the database
        expenseItemRepository.saveAndFlush(expenseItem);
        int databaseSizeBeforeUpdate = expenseItemRepository.findAll().size();

        // Update the expenseItem
        ExpenseItem updatedExpenseItem = expenseItemRepository.findOne(expenseItem.getId());
        updatedExpenseItem
                .purpose(UPDATED_PURPOSE)
                .amount(UPDATED_AMOUNT);
        ExpenseItemDTO expenseItemDTO = expenseItemMapper.expenseItemToExpenseItemDTO(updatedExpenseItem);

        restExpenseItemMockMvc.perform(put("/api/expense-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(expenseItemDTO)))
                .andExpect(status().isOk());

        // Validate the ExpenseItem in the database
        List<ExpenseItem> expenseItems = expenseItemRepository.findAll();
        assertThat(expenseItems).hasSize(databaseSizeBeforeUpdate);
        ExpenseItem testExpenseItem = expenseItems.get(expenseItems.size() - 1);
        assertThat(testExpenseItem.getPurpose()).isEqualTo(UPDATED_PURPOSE);
        assertThat(testExpenseItem.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void deleteExpenseItem() throws Exception {
        // Initialize the database
        expenseItemRepository.saveAndFlush(expenseItem);
        int databaseSizeBeforeDelete = expenseItemRepository.findAll().size();

        // Get the expenseItem
        restExpenseItemMockMvc.perform(delete("/api/expense-items/{id}", expenseItem.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ExpenseItem> expenseItems = expenseItemRepository.findAll();
        assertThat(expenseItems).hasSize(databaseSizeBeforeDelete - 1);
    }
}
