package com.yijia.yy.web.rest;

import com.yijia.yy.yyOaApp;

import com.yijia.yy.domain.Staff;
import com.yijia.yy.repository.StaffRepository;
import com.yijia.yy.service.StaffService;
import com.yijia.yy.service.dto.StaffDTO;
import com.yijia.yy.service.mapper.StaffMapper;

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
 * Test class for the StaffResource REST controller.
 *
 * @see StaffResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = yyOaApp.class)
public class StaffResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_TEL = "AAAAA";
    private static final String UPDATED_TEL = "BBBBB";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;
    private static final String DEFAULT_INPUT_OPERATOR = "AAAAA";
    private static final String UPDATED_INPUT_OPERATOR = "BBBBB";

    private static final Long DEFAULT_CREATE_TIME = 1L;
    private static final Long UPDATED_CREATE_TIME = 2L;
    private static final String DEFAULT_LAST_MODIFIER = "AAAAA";
    private static final String UPDATED_LAST_MODIFIER = "BBBBB";

    private static final Long DEFAULT_LAST_MODIFIED_TIME = 1L;
    private static final Long UPDATED_LAST_MODIFIED_TIME = 2L;

    @Inject
    private StaffRepository staffRepository;

    @Inject
    private StaffMapper staffMapper;

    @Inject
    private StaffService staffService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restStaffMockMvc;

    private Staff staff;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StaffResource staffResource = new StaffResource();
        ReflectionTestUtils.setField(staffResource, "staffService", staffService);
        this.restStaffMockMvc = MockMvcBuilders.standaloneSetup(staffResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Staff createEntity(EntityManager em) {
        Staff staff = new Staff()
                .name(DEFAULT_NAME)
                .tel(DEFAULT_TEL)
                .price(DEFAULT_PRICE)
                .inputOperator(DEFAULT_INPUT_OPERATOR)
                .createTime(DEFAULT_CREATE_TIME)
                .lastModifier(DEFAULT_LAST_MODIFIER)
                .lastModifiedTime(DEFAULT_LAST_MODIFIED_TIME);
        return staff;
    }

    @Before
    public void initTest() {
        staff = createEntity(em);
    }

    @Test
    @Transactional
    public void createStaff() throws Exception {
        int databaseSizeBeforeCreate = staffRepository.findAll().size();

        // Create the Staff
        StaffDTO staffDTO = staffMapper.staffToStaffDTO(staff);

        restStaffMockMvc.perform(post("/api/staff")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(staffDTO)))
                .andExpect(status().isCreated());

        // Validate the Staff in the database
        List<Staff> staff = staffRepository.findAll();
        assertThat(staff).hasSize(databaseSizeBeforeCreate + 1);
        Staff testStaff = staff.get(staff.size() - 1);
        assertThat(testStaff.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStaff.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testStaff.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testStaff.getInputOperator()).isEqualTo(DEFAULT_INPUT_OPERATOR);
        assertThat(testStaff.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testStaff.getLastModifier()).isEqualTo(DEFAULT_LAST_MODIFIER);
        assertThat(testStaff.getLastModifiedTime()).isEqualTo(DEFAULT_LAST_MODIFIED_TIME);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = staffRepository.findAll().size();
        // set the field null
        staff.setName(null);

        // Create the Staff, which fails.
        StaffDTO staffDTO = staffMapper.staffToStaffDTO(staff);

        restStaffMockMvc.perform(post("/api/staff")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(staffDTO)))
                .andExpect(status().isBadRequest());

        List<Staff> staff = staffRepository.findAll();
        assertThat(staff).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStaff() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staff
        restStaffMockMvc.perform(get("/api/staff?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(staff.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL.toString())))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
                .andExpect(jsonPath("$.[*].inputOperator").value(hasItem(DEFAULT_INPUT_OPERATOR.toString())))
                .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.intValue())))
                .andExpect(jsonPath("$.[*].lastModifier").value(hasItem(DEFAULT_LAST_MODIFIER.toString())))
                .andExpect(jsonPath("$.[*].lastModifiedTime").value(hasItem(DEFAULT_LAST_MODIFIED_TIME.intValue())));
    }

    @Test
    @Transactional
    public void getStaff() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get the staff
        restStaffMockMvc.perform(get("/api/staff/{id}", staff.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(staff.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.inputOperator").value(DEFAULT_INPUT_OPERATOR.toString()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.intValue()))
            .andExpect(jsonPath("$.lastModifier").value(DEFAULT_LAST_MODIFIER.toString()))
            .andExpect(jsonPath("$.lastModifiedTime").value(DEFAULT_LAST_MODIFIED_TIME.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingStaff() throws Exception {
        // Get the staff
        restStaffMockMvc.perform(get("/api/staff/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStaff() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);
        int databaseSizeBeforeUpdate = staffRepository.findAll().size();

        // Update the staff
        Staff updatedStaff = staffRepository.findOne(staff.getId());
        updatedStaff
                .name(UPDATED_NAME)
                .tel(UPDATED_TEL)
                .price(UPDATED_PRICE)
                .inputOperator(UPDATED_INPUT_OPERATOR)
                .createTime(UPDATED_CREATE_TIME)
                .lastModifier(UPDATED_LAST_MODIFIER)
                .lastModifiedTime(UPDATED_LAST_MODIFIED_TIME);
        StaffDTO staffDTO = staffMapper.staffToStaffDTO(updatedStaff);

        restStaffMockMvc.perform(put("/api/staff")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(staffDTO)))
                .andExpect(status().isOk());

        // Validate the Staff in the database
        List<Staff> staff = staffRepository.findAll();
        assertThat(staff).hasSize(databaseSizeBeforeUpdate);
        Staff testStaff = staff.get(staff.size() - 1);
        assertThat(testStaff.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStaff.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testStaff.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testStaff.getInputOperator()).isEqualTo(UPDATED_INPUT_OPERATOR);
        assertThat(testStaff.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testStaff.getLastModifier()).isEqualTo(UPDATED_LAST_MODIFIER);
        assertThat(testStaff.getLastModifiedTime()).isEqualTo(UPDATED_LAST_MODIFIED_TIME);
    }

    @Test
    @Transactional
    public void deleteStaff() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);
        int databaseSizeBeforeDelete = staffRepository.findAll().size();

        // Get the staff
        restStaffMockMvc.perform(delete("/api/staff/{id}", staff.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Staff> staff = staffRepository.findAll();
        assertThat(staff).hasSize(databaseSizeBeforeDelete - 1);
    }
}
