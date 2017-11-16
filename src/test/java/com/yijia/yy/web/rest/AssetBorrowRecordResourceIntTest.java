package com.yijia.yy.web.rest;

import com.yijia.yy.yyOaApp;

import com.yijia.yy.domain.AssetBorrowRecord;
import com.yijia.yy.repository.AssetBorrowRecordRepository;
import com.yijia.yy.service.AssetBorrowRecordService;
import com.yijia.yy.service.dto.AssetBorrowRecordDTO;
import com.yijia.yy.service.mapper.AssetBorrowRecordMapper;

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
 * Test class for the AssetBorrowRecordResource REST controller.
 *
 * @see AssetBorrowRecordResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = yyOaApp.class)
public class AssetBorrowRecordResourceIntTest {


    private static final Integer DEFAULT_AMOUNT = 1;
    private static final Integer UPDATED_AMOUNT = 2;

    private static final Long DEFAULT_BORROW_TIME = 1L;
    private static final Long UPDATED_BORROW_TIME = 2L;

    private static final Long DEFAULT_ESTIMATE_RETURN_TIME = 1L;
    private static final Long UPDATED_ESTIMATE_RETURN_TIME = 2L;

    private static final Long DEFAULT_ACTUAL_RETURN_TIME = 1L;
    private static final Long UPDATED_ACTUAL_RETURN_TIME = 2L;

    @Inject
    private AssetBorrowRecordRepository assetBorrowRecordRepository;

    @Inject
    private AssetBorrowRecordMapper assetBorrowRecordMapper;

    @Inject
    private AssetBorrowRecordService assetBorrowRecordService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAssetBorrowRecordMockMvc;

    private AssetBorrowRecord assetBorrowRecord;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AssetBorrowRecordResource assetBorrowRecordResource = new AssetBorrowRecordResource();
        ReflectionTestUtils.setField(assetBorrowRecordResource, "assetBorrowRecordService", assetBorrowRecordService);
        this.restAssetBorrowRecordMockMvc = MockMvcBuilders.standaloneSetup(assetBorrowRecordResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetBorrowRecord createEntity(EntityManager em) {
        AssetBorrowRecord assetBorrowRecord = new AssetBorrowRecord()
                .amount(DEFAULT_AMOUNT);
        return assetBorrowRecord;
    }

    @Before
    public void initTest() {
        assetBorrowRecord = createEntity(em);
    }

    @Test
    @Transactional
    public void createAssetBorrowRecord() throws Exception {
        int databaseSizeBeforeCreate = assetBorrowRecordRepository.findAll().size();

        // Create the AssetBorrowRecord
        AssetBorrowRecordDTO assetBorrowRecordDTO = assetBorrowRecordMapper.assetBorrowRecordToAssetBorrowRecordDTO(assetBorrowRecord);

        restAssetBorrowRecordMockMvc.perform(post("/api/asset-borrow-records")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(assetBorrowRecordDTO)))
                .andExpect(status().isCreated());

        // Validate the AssetBorrowRecord in the database
        List<AssetBorrowRecord> assetBorrowRecords = assetBorrowRecordRepository.findAll();
        assertThat(assetBorrowRecords).hasSize(databaseSizeBeforeCreate + 1);
        AssetBorrowRecord testAssetBorrowRecord = assetBorrowRecords.get(assetBorrowRecords.size() - 1);
        assertThat(testAssetBorrowRecord.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetBorrowRecordRepository.findAll().size();
        // set the field null
        assetBorrowRecord.setAmount(null);

        // Create the AssetBorrowRecord, which fails.
        AssetBorrowRecordDTO assetBorrowRecordDTO = assetBorrowRecordMapper.assetBorrowRecordToAssetBorrowRecordDTO(assetBorrowRecord);

        restAssetBorrowRecordMockMvc.perform(post("/api/asset-borrow-records")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(assetBorrowRecordDTO)))
                .andExpect(status().isBadRequest());

        List<AssetBorrowRecord> assetBorrowRecords = assetBorrowRecordRepository.findAll();
        assertThat(assetBorrowRecords).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBorrowTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetBorrowRecordRepository.findAll().size();
        // set the field null
        // Create the AssetBorrowRecord, which fails.
        AssetBorrowRecordDTO assetBorrowRecordDTO = assetBorrowRecordMapper.assetBorrowRecordToAssetBorrowRecordDTO(assetBorrowRecord);

        restAssetBorrowRecordMockMvc.perform(post("/api/asset-borrow-records")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(assetBorrowRecordDTO)))
                .andExpect(status().isBadRequest());

        List<AssetBorrowRecord> assetBorrowRecords = assetBorrowRecordRepository.findAll();
        assertThat(assetBorrowRecords).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAssetBorrowRecords() throws Exception {
        // Initialize the database
        assetBorrowRecordRepository.saveAndFlush(assetBorrowRecord);

        // Get all the assetBorrowRecords
        restAssetBorrowRecordMockMvc.perform(get("/api/asset-borrow-records?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(assetBorrowRecord.getId().intValue())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
                .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_BORROW_TIME.intValue())))
                .andExpect(jsonPath("$.[*].estimateReturnTime").value(hasItem(DEFAULT_ESTIMATE_RETURN_TIME.intValue())))
                .andExpect(jsonPath("$.[*].actualReturnTime").value(hasItem(DEFAULT_ACTUAL_RETURN_TIME.intValue())));
    }

    @Test
    @Transactional
    public void getAssetBorrowRecord() throws Exception {
        // Initialize the database
        assetBorrowRecordRepository.saveAndFlush(assetBorrowRecord);

        // Get the assetBorrowRecord
        restAssetBorrowRecordMockMvc.perform(get("/api/asset-borrow-records/{id}", assetBorrowRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(assetBorrowRecord.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_BORROW_TIME.intValue()))
            .andExpect(jsonPath("$.estimateReturnTime").value(DEFAULT_ESTIMATE_RETURN_TIME.intValue()))
            .andExpect(jsonPath("$.actualReturnTime").value(DEFAULT_ACTUAL_RETURN_TIME.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAssetBorrowRecord() throws Exception {
        // Get the assetBorrowRecord
        restAssetBorrowRecordMockMvc.perform(get("/api/asset-borrow-records/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAssetBorrowRecord() throws Exception {
        // Initialize the database
        assetBorrowRecordRepository.saveAndFlush(assetBorrowRecord);
        int databaseSizeBeforeUpdate = assetBorrowRecordRepository.findAll().size();

        // Update the assetBorrowRecord
        AssetBorrowRecord updatedAssetBorrowRecord = assetBorrowRecordRepository.findOne(assetBorrowRecord.getId());
        updatedAssetBorrowRecord
                .amount(UPDATED_AMOUNT);
        AssetBorrowRecordDTO assetBorrowRecordDTO = assetBorrowRecordMapper.assetBorrowRecordToAssetBorrowRecordDTO(updatedAssetBorrowRecord);

        restAssetBorrowRecordMockMvc.perform(put("/api/asset-borrow-records")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(assetBorrowRecordDTO)))
                .andExpect(status().isOk());

        // Validate the AssetBorrowRecord in the database
        List<AssetBorrowRecord> assetBorrowRecords = assetBorrowRecordRepository.findAll();
        assertThat(assetBorrowRecords).hasSize(databaseSizeBeforeUpdate);
        AssetBorrowRecord testAssetBorrowRecord = assetBorrowRecords.get(assetBorrowRecords.size() - 1);
        assertThat(testAssetBorrowRecord.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void deleteAssetBorrowRecord() throws Exception {
        // Initialize the database
        assetBorrowRecordRepository.saveAndFlush(assetBorrowRecord);
        int databaseSizeBeforeDelete = assetBorrowRecordRepository.findAll().size();

        // Get the assetBorrowRecord
        restAssetBorrowRecordMockMvc.perform(delete("/api/asset-borrow-records/{id}", assetBorrowRecord.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<AssetBorrowRecord> assetBorrowRecords = assetBorrowRecordRepository.findAll();
        assertThat(assetBorrowRecords).hasSize(databaseSizeBeforeDelete - 1);
    }
}
