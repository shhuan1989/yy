package com.yijia.yy.web.rest;

import com.yijia.yy.yyOaApp;

import com.yijia.yy.domain.ContractInvoice;
import com.yijia.yy.repository.ContractInvoiceRepository;
import com.yijia.yy.service.ContractInvoiceService;
import com.yijia.yy.service.dto.ContractInvoiceDTO;
import com.yijia.yy.service.mapper.ContractInvoiceMapper;

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
 * Test class for the ContractInvoiceResource REST controller.
 *
 * @see ContractInvoiceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = yyOaApp.class)
public class ContractInvoiceResourceIntTest {


    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final Long DEFAULT_CREATE_TIME = 1L;
    private static final Long UPDATED_CREATE_TIME = 2L;

    @Inject
    private ContractInvoiceRepository contractInvoiceRepository;

    @Inject
    private ContractInvoiceMapper contractInvoiceMapper;

    @Inject
    private ContractInvoiceService contractInvoiceService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restContractInvoiceMockMvc;

    private ContractInvoice contractInvoice;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ContractInvoiceResource contractInvoiceResource = new ContractInvoiceResource();
        ReflectionTestUtils.setField(contractInvoiceResource, "contractInvoiceService", contractInvoiceService);
        this.restContractInvoiceMockMvc = MockMvcBuilders.standaloneSetup(contractInvoiceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContractInvoice createEntity(EntityManager em) {
        ContractInvoice contractInvoice = new ContractInvoice()
                .amount(DEFAULT_AMOUNT)
                .createTime(DEFAULT_CREATE_TIME);
        return contractInvoice;
    }

    @Before
    public void initTest() {
        contractInvoice = createEntity(em);
    }

    @Test
    @Transactional
    public void createContractInvoice() throws Exception {
        int databaseSizeBeforeCreate = contractInvoiceRepository.findAll().size();

        // Create the ContractInvoice
        ContractInvoiceDTO contractInvoiceDTO = contractInvoiceMapper.contractInvoiceToContractInvoiceDTO(contractInvoice);

        restContractInvoiceMockMvc.perform(post("/api/contract-invoices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(contractInvoiceDTO)))
                .andExpect(status().isCreated());

        // Validate the ContractInvoice in the database
        List<ContractInvoice> contractInvoices = contractInvoiceRepository.findAll();
        assertThat(contractInvoices).hasSize(databaseSizeBeforeCreate + 1);
        ContractInvoice testContractInvoice = contractInvoices.get(contractInvoices.size() - 1);
        assertThat(testContractInvoice.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testContractInvoice.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractInvoiceRepository.findAll().size();
        // set the field null
        contractInvoice.setAmount(null);

        // Create the ContractInvoice, which fails.
        ContractInvoiceDTO contractInvoiceDTO = contractInvoiceMapper.contractInvoiceToContractInvoiceDTO(contractInvoice);

        restContractInvoiceMockMvc.perform(post("/api/contract-invoices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(contractInvoiceDTO)))
                .andExpect(status().isBadRequest());

        List<ContractInvoice> contractInvoices = contractInvoiceRepository.findAll();
        assertThat(contractInvoices).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractInvoiceRepository.findAll().size();
        // set the field null
        contractInvoice.setCreateTime(null);

        // Create the ContractInvoice, which fails.
        ContractInvoiceDTO contractInvoiceDTO = contractInvoiceMapper.contractInvoiceToContractInvoiceDTO(contractInvoice);

        restContractInvoiceMockMvc.perform(post("/api/contract-invoices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(contractInvoiceDTO)))
                .andExpect(status().isBadRequest());

        List<ContractInvoice> contractInvoices = contractInvoiceRepository.findAll();
        assertThat(contractInvoices).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllContractInvoices() throws Exception {
        // Initialize the database
        contractInvoiceRepository.saveAndFlush(contractInvoice);

        // Get all the contractInvoices
        restContractInvoiceMockMvc.perform(get("/api/contract-invoices?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(contractInvoice.getId().intValue())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
                .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.intValue())));
    }

    @Test
    @Transactional
    public void getContractInvoice() throws Exception {
        // Initialize the database
        contractInvoiceRepository.saveAndFlush(contractInvoice);

        // Get the contractInvoice
        restContractInvoiceMockMvc.perform(get("/api/contract-invoices/{id}", contractInvoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contractInvoice.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingContractInvoice() throws Exception {
        // Get the contractInvoice
        restContractInvoiceMockMvc.perform(get("/api/contract-invoices/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContractInvoice() throws Exception {
        // Initialize the database
        contractInvoiceRepository.saveAndFlush(contractInvoice);
        int databaseSizeBeforeUpdate = contractInvoiceRepository.findAll().size();

        // Update the contractInvoice
        ContractInvoice updatedContractInvoice = contractInvoiceRepository.findOne(contractInvoice.getId());
        updatedContractInvoice
                .amount(UPDATED_AMOUNT)
                .createTime(UPDATED_CREATE_TIME);
        ContractInvoiceDTO contractInvoiceDTO = contractInvoiceMapper.contractInvoiceToContractInvoiceDTO(updatedContractInvoice);

        restContractInvoiceMockMvc.perform(put("/api/contract-invoices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(contractInvoiceDTO)))
                .andExpect(status().isOk());

        // Validate the ContractInvoice in the database
        List<ContractInvoice> contractInvoices = contractInvoiceRepository.findAll();
        assertThat(contractInvoices).hasSize(databaseSizeBeforeUpdate);
        ContractInvoice testContractInvoice = contractInvoices.get(contractInvoices.size() - 1);
        assertThat(testContractInvoice.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testContractInvoice.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    public void deleteContractInvoice() throws Exception {
        // Initialize the database
        contractInvoiceRepository.saveAndFlush(contractInvoice);
        int databaseSizeBeforeDelete = contractInvoiceRepository.findAll().size();

        // Get the contractInvoice
        restContractInvoiceMockMvc.perform(delete("/api/contract-invoices/{id}", contractInvoice.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ContractInvoice> contractInvoices = contractInvoiceRepository.findAll();
        assertThat(contractInvoices).hasSize(databaseSizeBeforeDelete - 1);
    }
}
