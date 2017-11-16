package com.yijia.yy.web.rest;

import com.yijia.yy.YiyingOaApp;

import com.yijia.yy.domain.Approval;
import com.yijia.yy.domain.enumeration.ApprovalStatus;
import com.yijia.yy.repository.ApprovalRepository;
import com.yijia.yy.service.ApprovalService;
import com.yijia.yy.service.dto.ApprovalDTO;
import com.yijia.yy.service.mapper.ApprovalMapper;

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
 * Test class for the ApprovalResource REST controller.
 *
 * @see ApprovalResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YiyingOaApp.class)
public class ApprovalResourceIntTest {

    private static final Long DEFAULT_CREATE_TIME = 1L;
    private static final Long UPDATED_CREATE_TIME = 2L;

    private static final Long DEFAULT_LAST_MODIFIED_TIME = 1L;
    private static final Long UPDATED_LAST_MODIFIED_TIME = 2L;

    private static final ApprovalStatus DEFAULT_STATUS = ApprovalStatus.APPROVED;
    private static final ApprovalStatus UPDATED_STATUS = ApprovalStatus.IN_PROGRESS;

    @Inject
    private ApprovalRepository approvalRepository;

    @Inject
    private ApprovalMapper approvalMapper;

    @Inject
    private ApprovalService approvalService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restApprovalMockMvc;

    private Approval approval;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ApprovalResource approvalResource = new ApprovalResource();
        ReflectionTestUtils.setField(approvalResource, "approvalService", approvalService);
        this.restApprovalMockMvc = MockMvcBuilders.standaloneSetup(approvalResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Approval createEntity(EntityManager em) {
        Approval approval = new Approval()
                .createTime(DEFAULT_CREATE_TIME)
                .lastModifiedTime(DEFAULT_LAST_MODIFIED_TIME)
                .status(DEFAULT_STATUS);
        return approval;
    }

    @Before
    public void initTest() {
        approval = createEntity(em);
    }

    @Test
    @Transactional
    public void createApproval() throws Exception {
        int databaseSizeBeforeCreate = approvalRepository.findAll().size();

        // Create the Approval
        ApprovalDTO approvalDTO = approvalMapper.approvalToApprovalDTO(approval);

        restApprovalMockMvc.perform(post("/api/approvals")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(approvalDTO)))
                .andExpect(status().isCreated());

        // Validate the Approval in the database
        List<Approval> approvals = approvalRepository.findAll();
        assertThat(approvals).hasSize(databaseSizeBeforeCreate + 1);
        Approval testApproval = approvals.get(approvals.size() - 1);
        assertThat(testApproval.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testApproval.getLastModifiedTime()).isEqualTo(DEFAULT_LAST_MODIFIED_TIME);
        assertThat(testApproval.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void checkCreateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = approvalRepository.findAll().size();
        // set the field null
        approval.setCreateTime(null);

        // Create the Approval, which fails.
        ApprovalDTO approvalDTO = approvalMapper.approvalToApprovalDTO(approval);

        restApprovalMockMvc.perform(post("/api/approvals")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(approvalDTO)))
                .andExpect(status().isBadRequest());

        List<Approval> approvals = approvalRepository.findAll();
        assertThat(approvals).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastModifiedTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = approvalRepository.findAll().size();
        // set the field null
        approval.setLastModifiedTime(null);

        // Create the Approval, which fails.
        ApprovalDTO approvalDTO = approvalMapper.approvalToApprovalDTO(approval);

        restApprovalMockMvc.perform(post("/api/approvals")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(approvalDTO)))
                .andExpect(status().isBadRequest());

        List<Approval> approvals = approvalRepository.findAll();
        assertThat(approvals).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllApprovals() throws Exception {
        // Initialize the database
        approvalRepository.saveAndFlush(approval);

        // Get all the approvals
        restApprovalMockMvc.perform(get("/api/approvals?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(approval.getId().intValue())))
                .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.intValue())))
                .andExpect(jsonPath("$.[*].lastModifiedTime").value(hasItem(DEFAULT_LAST_MODIFIED_TIME.intValue())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    public void getApproval() throws Exception {
        // Initialize the database
        approvalRepository.saveAndFlush(approval);

        // Get the approval
        restApprovalMockMvc.perform(get("/api/approvals/{id}", approval.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(approval.getId().intValue()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.intValue()))
            .andExpect(jsonPath("$.lastModifiedTime").value(DEFAULT_LAST_MODIFIED_TIME.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    public void getNonExistingApproval() throws Exception {
        // Get the approval
        restApprovalMockMvc.perform(get("/api/approvals/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApproval() throws Exception {
        // Initialize the database
        approvalRepository.saveAndFlush(approval);
        int databaseSizeBeforeUpdate = approvalRepository.findAll().size();

        // Update the approval
        Approval updatedApproval = approvalRepository.findOne(approval.getId());
        updatedApproval
                .createTime(UPDATED_CREATE_TIME)
                .lastModifiedTime(UPDATED_LAST_MODIFIED_TIME)
                .status(UPDATED_STATUS);
        ApprovalDTO approvalDTO = approvalMapper.approvalToApprovalDTO(updatedApproval);

        restApprovalMockMvc.perform(put("/api/approvals")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(approvalDTO)))
                .andExpect(status().isOk());

        // Validate the Approval in the database
        List<Approval> approvals = approvalRepository.findAll();
        assertThat(approvals).hasSize(databaseSizeBeforeUpdate);
        Approval testApproval = approvals.get(approvals.size() - 1);
        assertThat(testApproval.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testApproval.getLastModifiedTime()).isEqualTo(UPDATED_LAST_MODIFIED_TIME);
        assertThat(testApproval.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deleteApproval() throws Exception {
        // Initialize the database
        approvalRepository.saveAndFlush(approval);
        int databaseSizeBeforeDelete = approvalRepository.findAll().size();

        // Get the approval
        restApprovalMockMvc.perform(delete("/api/approvals/{id}", approval.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Approval> approvals = approvalRepository.findAll();
        assertThat(approvals).hasSize(databaseSizeBeforeDelete - 1);
    }
}
