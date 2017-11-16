package com.yijia.yy.web.rest;

import com.yijia.yy.yyOaApp;

import com.yijia.yy.domain.Equipment;
import com.yijia.yy.repository.EquipmentRepository;
import com.yijia.yy.service.EquipmentService;
import com.yijia.yy.service.dto.EquipmentDTO;
import com.yijia.yy.service.mapper.EquipmentMapper;

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
 * Test class for the EquipmentResource REST controller.
 *
 * @see EquipmentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = yyOaApp.class)
public class EquipmentResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_VENDOR = "AAAAA";
    private static final String UPDATED_VENDOR = "BBBBB";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final Long DEFAULT_CREATE_TIME = 1L;
    private static final Long UPDATED_CREATE_TIME = 2L;
    private static final String DEFAULT_INPUT_OPERATOR = "AAAAA";
    private static final String UPDATED_INPUT_OPERATOR = "BBBBB";

    private static final Long DEFAULT_LAST_MODIFIED_TIME = 1L;
    private static final Long UPDATED_LAST_MODIFIED_TIME = 2L;
    private static final String DEFAULT_LAST_MODIFIER = "AAAAA";
    private static final String UPDATED_LAST_MODIFIER = "BBBBB";

    @Inject
    private EquipmentRepository equipmentRepository;

    @Inject
    private EquipmentMapper equipmentMapper;

    @Inject
    private EquipmentService equipmentService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restEquipmentMockMvc;

    private Equipment equipment;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EquipmentResource equipmentResource = new EquipmentResource();
        ReflectionTestUtils.setField(equipmentResource, "equipmentService", equipmentService);
        this.restEquipmentMockMvc = MockMvcBuilders.standaloneSetup(equipmentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Equipment createEntity(EntityManager em) {
        Equipment equipment = new Equipment()
                .name(DEFAULT_NAME)
                .vendor(DEFAULT_VENDOR)
                .price(DEFAULT_PRICE)
                .createTime(DEFAULT_CREATE_TIME)
                .inputOperator(DEFAULT_INPUT_OPERATOR)
                .lastModifiedTime(DEFAULT_LAST_MODIFIED_TIME)
                .lastModifier(DEFAULT_LAST_MODIFIER);
        return equipment;
    }

    @Before
    public void initTest() {
        equipment = createEntity(em);
    }

    @Test
    @Transactional
    public void createEquipment() throws Exception {
        int databaseSizeBeforeCreate = equipmentRepository.findAll().size();

        // Create the Equipment
        EquipmentDTO equipmentDTO = equipmentMapper.equipmentToEquipmentDTO(equipment);

        restEquipmentMockMvc.perform(post("/api/equipment")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
                .andExpect(status().isCreated());

        // Validate the Equipment in the database
        List<Equipment> equipment = equipmentRepository.findAll();
        assertThat(equipment).hasSize(databaseSizeBeforeCreate + 1);
        Equipment testEquipment = equipment.get(equipment.size() - 1);
        assertThat(testEquipment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEquipment.getVendor()).isEqualTo(DEFAULT_VENDOR);
        assertThat(testEquipment.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testEquipment.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testEquipment.getInputOperator()).isEqualTo(DEFAULT_INPUT_OPERATOR);
        assertThat(testEquipment.getLastModifiedTime()).isEqualTo(DEFAULT_LAST_MODIFIED_TIME);
        assertThat(testEquipment.getLastModifier()).isEqualTo(DEFAULT_LAST_MODIFIER);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = equipmentRepository.findAll().size();
        // set the field null
        equipment.setName(null);

        // Create the Equipment, which fails.
        EquipmentDTO equipmentDTO = equipmentMapper.equipmentToEquipmentDTO(equipment);

        restEquipmentMockMvc.perform(post("/api/equipment")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
                .andExpect(status().isBadRequest());

        List<Equipment> equipment = equipmentRepository.findAll();
        assertThat(equipment).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get all the equipment
        restEquipmentMockMvc.perform(get("/api/equipment?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(equipment.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].vendor").value(hasItem(DEFAULT_VENDOR.toString())))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
                .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.intValue())))
                .andExpect(jsonPath("$.[*].inputOperator").value(hasItem(DEFAULT_INPUT_OPERATOR.toString())))
                .andExpect(jsonPath("$.[*].lastModifiedTime").value(hasItem(DEFAULT_LAST_MODIFIED_TIME.intValue())))
                .andExpect(jsonPath("$.[*].lastModifier").value(hasItem(DEFAULT_LAST_MODIFIER.toString())));
    }

    @Test
    @Transactional
    public void getEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);

        // Get the equipment
        restEquipmentMockMvc.perform(get("/api/equipment/{id}", equipment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(equipment.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.vendor").value(DEFAULT_VENDOR.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.intValue()))
            .andExpect(jsonPath("$.inputOperator").value(DEFAULT_INPUT_OPERATOR.toString()))
            .andExpect(jsonPath("$.lastModifiedTime").value(DEFAULT_LAST_MODIFIED_TIME.intValue()))
            .andExpect(jsonPath("$.lastModifier").value(DEFAULT_LAST_MODIFIER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEquipment() throws Exception {
        // Get the equipment
        restEquipmentMockMvc.perform(get("/api/equipment/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);
        int databaseSizeBeforeUpdate = equipmentRepository.findAll().size();

        // Update the equipment
        Equipment updatedEquipment = equipmentRepository.findOne(equipment.getId());
        updatedEquipment
                .name(UPDATED_NAME)
                .vendor(UPDATED_VENDOR)
                .price(UPDATED_PRICE)
                .createTime(UPDATED_CREATE_TIME)
                .inputOperator(UPDATED_INPUT_OPERATOR)
                .lastModifiedTime(UPDATED_LAST_MODIFIED_TIME)
                .lastModifier(UPDATED_LAST_MODIFIER);
        EquipmentDTO equipmentDTO = equipmentMapper.equipmentToEquipmentDTO(updatedEquipment);

        restEquipmentMockMvc.perform(put("/api/equipment")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(equipmentDTO)))
                .andExpect(status().isOk());

        // Validate the Equipment in the database
        List<Equipment> equipment = equipmentRepository.findAll();
        assertThat(equipment).hasSize(databaseSizeBeforeUpdate);
        Equipment testEquipment = equipment.get(equipment.size() - 1);
        assertThat(testEquipment.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEquipment.getVendor()).isEqualTo(UPDATED_VENDOR);
        assertThat(testEquipment.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testEquipment.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testEquipment.getInputOperator()).isEqualTo(UPDATED_INPUT_OPERATOR);
        assertThat(testEquipment.getLastModifiedTime()).isEqualTo(UPDATED_LAST_MODIFIED_TIME);
        assertThat(testEquipment.getLastModifier()).isEqualTo(UPDATED_LAST_MODIFIER);
    }

    @Test
    @Transactional
    public void deleteEquipment() throws Exception {
        // Initialize the database
        equipmentRepository.saveAndFlush(equipment);
        int databaseSizeBeforeDelete = equipmentRepository.findAll().size();

        // Get the equipment
        restEquipmentMockMvc.perform(delete("/api/equipment/{id}", equipment.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Equipment> equipment = equipmentRepository.findAll();
        assertThat(equipment).hasSize(databaseSizeBeforeDelete - 1);
    }
}
