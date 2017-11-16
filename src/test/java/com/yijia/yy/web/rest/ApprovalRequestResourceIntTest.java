package com.yijia.yy.web.rest;

import com.yijia.yy.YiyingOaApp;

import com.yijia.yy.domain.Approval;
import com.yijia.yy.domain.ApprovalRequest;
import com.yijia.yy.domain.enumeration.ApprovalStatus;
import com.yijia.yy.repository.ApprovalRequestRepository;
import com.yijia.yy.service.ApprovalRequestService;
import com.yijia.yy.service.dto.ApprovalRequestDTO;
import com.yijia.yy.service.mapper.ApprovalRequestMapper;

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
 * Test class for the ApprovalRequestResource REST controller.
 *
 * @see ApprovalRequestResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YiyingOaApp.class)
public class ApprovalRequestResourceIntTest {


    private static final Long DEFAULT_CREATE_TIME = 1L;
    private static final Long UPDATED_CREATE_TIME = 2L;

    private static final Long DEFAULT_LAST_MODIFIED_TIME = 1L;
    private static final Long UPDATED_LAST_MODIFIED_TIME = 2L;
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final ApprovalStatus DEFAULT_STATUS = ApprovalStatus.APPROVED;
    private static final ApprovalStatus UPDATED_STATUS = ApprovalStatus.CANCELLED;

    @Inject
    private ApprovalRequestRepository approvalRequestRepository;

    @Inject
    private ApprovalRequestMapper approvalRequestMapper;

    @Inject
    private ApprovalRequestService approvalRequestService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restApprovalRequestMockMvc;

    private ApprovalRequest approvalRequest;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ApprovalRequestResource approvalRequestResource = new ApprovalRequestResource();
        ReflectionTestUtils.setField(approvalRequestResource, "approvalRequestService", approvalRequestService);
        this.restApprovalRequestMockMvc = MockMvcBuilders.standaloneSetup(approvalRequestResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApprovalRequest createEntity(EntityManager em) {
        ApprovalRequest approvalRequest = new ApprovalRequest()
                .createTime(DEFAULT_CREATE_TIME)
                .lastModifiedTime(DEFAULT_LAST_MODIFIED_TIME)
                .name(DEFAULT_NAME)
                .status(DEFAULT_STATUS);
        return approvalRequest;
    }

    @Before
    public void initTest() {
        approvalRequest = createEntity(em);
    }

    @Test
    @Transactional
    public void createApprovalRequest() throws Exception {
        int databaseSizeBeforeCreate = approvalRequestRepository.findAll().size();

        // Create the ApprovalRequest
        ApprovalRequestDTO approvalRequestDTO = approvalRequestMapper.approvalRequestToApprovalRequestDTO(approvalRequest);

        restApprovalRequestMockMvc.perform(post("/api/approval-requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(approvalRequestDTO)))
                .andExpect(status().isCreated());

        // Validate the ApprovalRequest in the database
        List<ApprovalRequest> approvalRequests = approvalRequestRepository.findAll();
        assertThat(approvalRequests).hasSize(databaseSizeBeforeCreate + 1);
        ApprovalRequest testApprovalRequest = approvalRequests.get(approvalRequests.size() - 1);
        assertThat(testApprovalRequest.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testApprovalRequest.getLastModifiedTime()).isEqualTo(DEFAULT_LAST_MODIFIED_TIME);
        assertThat(testApprovalRequest.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testApprovalRequest.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void checkCreateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = approvalRequestRepository.findAll().size();
        // set the field null
        approvalRequest.setCreateTime(null);

        // Create the ApprovalRequest, which fails.
        ApprovalRequestDTO approvalRequestDTO = approvalRequestMapper.approvalRequestToApprovalRequestDTO(approvalRequest);

        restApprovalRequestMockMvc.perform(post("/api/approval-requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(approvalRequestDTO)))
                .andExpect(status().isBadRequest());

        List<ApprovalRequest> approvalRequests = approvalRequestRepository.findAll();
        assertThat(approvalRequests).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastModifiedTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = approvalRequestRepository.findAll().size();
        // set the field null
        approvalRequest.setLastModifiedTime(null);

        // Create the ApprovalRequest, which fails.
        ApprovalRequestDTO approvalRequestDTO = approvalRequestMapper.approvalRequestToApprovalRequestDTO(approvalRequest);

        restApprovalRequestMockMvc.perform(post("/api/approval-requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(approvalRequestDTO)))
                .andExpect(status().isBadRequest());

        List<ApprovalRequest> approvalRequests = approvalRequestRepository.findAll();
        assertThat(approvalRequests).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllApprovalRequests() throws Exception {
        // Initialize the database
        approvalRequestRepository.saveAndFlush(approvalRequest);

        // Get all the approvalRequests
        restApprovalRequestMockMvc.perform(get("/api/approval-requests?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(approvalRequest.getId().intValue())))
                .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.intValue())))
                .andExpect(jsonPath("$.[*].lastModifiedTime").value(hasItem(DEFAULT_LAST_MODIFIED_TIME.intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    public void getApprovalRequest() throws Exception {
        // Initialize the database
        approvalRequestRepository.saveAndFlush(approvalRequest);

        // Get the approvalRequest
        restApprovalRequestMockMvc.perform(get("/api/approval-requests/{id}", approvalRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(approvalRequest.getId().intValue()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.intValue()))
            .andExpect(jsonPath("$.lastModifiedTime").value(DEFAULT_LAST_MODIFIED_TIME.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    public void getNonExistingApprovalRequest() throws Exception {
        // Get the approvalRequest
        restApprovalRequestMockMvc.perform(get("/api/approval-requests/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApprovalRequest() throws Exception {
        // Initialize the database
        approvalRequestRepository.saveAndFlush(approvalRequest);
        int databaseSizeBeforeUpdate = approvalRequestRepository.findAll().size();

        // Update the approvalRequest
        ApprovalRequest updatedApprovalRequest = approvalRequestRepository.findOne(approvalRequest.getId());
        updatedApprovalRequest
                .createTime(UPDATED_CREATE_TIME)
                .lastModifiedTime(UPDATED_LAST_MODIFIED_TIME)
                .name(UPDATED_NAME)
                .status(UPDATED_STATUS);
        ApprovalRequestDTO approvalRequestDTO = approvalRequestMapper.approvalRequestToApprovalRequestDTO(updatedApprovalRequest);

        restApprovalRequestMockMvc.perform(put("/api/approval-requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(approvalRequestDTO)))
                .andExpect(status().isOk());

        // Validate the ApprovalRequest in the database
        List<ApprovalRequest> approvalRequests = approvalRequestRepository.findAll();
        assertThat(approvalRequests).hasSize(databaseSizeBeforeUpdate);
        ApprovalRequest testApprovalRequest = approvalRequests.get(approvalRequests.size() - 1);
        assertThat(testApprovalRequest.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testApprovalRequest.getLastModifiedTime()).isEqualTo(UPDATED_LAST_MODIFIED_TIME);
        assertThat(testApprovalRequest.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testApprovalRequest.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deleteApprovalRequest() throws Exception {
        // Initialize the database
        approvalRequestRepository.saveAndFlush(approvalRequest);
        int databaseSizeBeforeDelete = approvalRequestRepository.findAll().size();

        // Get the approvalRequest
        restApprovalRequestMockMvc.perform(delete("/api/approval-requests/{id}", approvalRequest.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ApprovalRequest> approvalRequests = approvalRequestRepository.findAll();
        assertThat(approvalRequests).hasSize(databaseSizeBeforeDelete - 1);
    }
}
