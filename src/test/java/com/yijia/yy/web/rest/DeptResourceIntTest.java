package com.yijia.yy.web.rest;

import com.yijia.yy.YiyingOaApp;

import com.yijia.yy.domain.Dept;
import com.yijia.yy.repository.DeptRepository;
import com.yijia.yy.service.DeptService;
import com.yijia.yy.service.dto.DeptDTO;
import com.yijia.yy.service.mapper.DeptMapper;

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
 * Test class for the DeptResource REST controller.
 *
 * @see DeptResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YiyingOaApp.class)
public class DeptResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private DeptRepository deptRepository;

    @Inject
    private DeptMapper deptMapper;

    @Inject
    private DeptService deptService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDeptMockMvc;

    private Dept dept;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DeptResource deptResource = new DeptResource();
        ReflectionTestUtils.setField(deptResource, "deptService", deptService);
        this.restDeptMockMvc = MockMvcBuilders.standaloneSetup(deptResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dept createEntity(EntityManager em) {
        Dept dept = new Dept()
                .name(DEFAULT_NAME);
        return dept;
    }

    @Before
    public void initTest() {
        dept = createEntity(em);
    }

    @Test
    @Transactional
    public void createDept() throws Exception {
        int databaseSizeBeforeCreate = deptRepository.findAll().size();

        // Create the Dept
        DeptDTO deptDTO = deptMapper.deptToDeptDTO(dept);

        restDeptMockMvc.perform(post("/api/depts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(deptDTO)))
                .andExpect(status().isCreated());

        // Validate the Dept in the database
        List<Dept> depts = deptRepository.findAll();
        assertThat(depts).hasSize(databaseSizeBeforeCreate + 1);
        Dept testDept = depts.get(depts.size() - 1);
        assertThat(testDept.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllDepts() throws Exception {
        // Initialize the database
        deptRepository.saveAndFlush(dept);

        // Get all the depts
        restDeptMockMvc.perform(get("/api/depts?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(dept.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getDept() throws Exception {
        // Initialize the database
        deptRepository.saveAndFlush(dept);

        // Get the dept
        restDeptMockMvc.perform(get("/api/depts/{id}", dept.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dept.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDept() throws Exception {
        // Get the dept
        restDeptMockMvc.perform(get("/api/depts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDept() throws Exception {
        // Initialize the database
        deptRepository.saveAndFlush(dept);
        int databaseSizeBeforeUpdate = deptRepository.findAll().size();

        // Update the dept
        Dept updatedDept = deptRepository.findOne(dept.getId());
        updatedDept
                .name(UPDATED_NAME);
        DeptDTO deptDTO = deptMapper.deptToDeptDTO(updatedDept);

        restDeptMockMvc.perform(put("/api/depts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(deptDTO)))
                .andExpect(status().isOk());

        // Validate the Dept in the database
        List<Dept> depts = deptRepository.findAll();
        assertThat(depts).hasSize(databaseSizeBeforeUpdate);
        Dept testDept = depts.get(depts.size() - 1);
        assertThat(testDept.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteDept() throws Exception {
        // Initialize the database
        deptRepository.saveAndFlush(dept);
        int databaseSizeBeforeDelete = deptRepository.findAll().size();

        // Get the dept
        restDeptMockMvc.perform(delete("/api/depts/{id}", dept.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Dept> depts = deptRepository.findAll();
        assertThat(depts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
