package com.yijia.yy.web.rest;

import com.yijia.yy.YiyingOaApp;

import com.yijia.yy.domain.Role;
import com.yijia.yy.repository.RoleRepository;
import com.yijia.yy.service.RoleService;
import com.yijia.yy.service.dto.RoleDTO;
import com.yijia.yy.service.mapper.RoleMapper;

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
 * Test class for the RoleResource REST controller.
 *
 * @see RoleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YiyingOaApp.class)
public class RoleResourceIntTest {


    private static final Long DEFAULT_CREATE_TIME = 1L;
    private static final Long UPDATED_CREATE_TIME = 2L;

    private static final Long DEFAULT_LAST_MODIFIED_TIME = 1L;
    private static final Long UPDATED_LAST_MODIFIED_TIME = 2L;
    private static final String DEFAULT_AUTHS = "AAAAA";
    private static final String UPDATED_AUTHS = "BBBBB";
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private RoleRepository roleRepository;

    @Inject
    private RoleMapper roleMapper;

    @Inject
    private RoleService roleService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restRoleMockMvc;

    private Role role;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RoleResource roleResource = new RoleResource();
        ReflectionTestUtils.setField(roleResource, "roleService", roleService);
        this.restRoleMockMvc = MockMvcBuilders.standaloneSetup(roleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Role createEntity(EntityManager em) {
        Role role = new Role()
                .createTime(DEFAULT_CREATE_TIME)
                .lastModifiedTime(DEFAULT_LAST_MODIFIED_TIME)
                .auths(DEFAULT_AUTHS)
                .name(DEFAULT_NAME);
        return role;
    }

    @Before
    public void initTest() {
        role = createEntity(em);
    }

    @Test
    @Transactional
    public void createRole() throws Exception {
        int databaseSizeBeforeCreate = roleRepository.findAll().size();

        // Create the Role
        RoleDTO roleDTO = roleMapper.roleToRoleDTO(role);

        restRoleMockMvc.perform(post("/api/roles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(roleDTO)))
                .andExpect(status().isCreated());

        // Validate the Role in the database
        List<Role> roles = roleRepository.findAll();
        assertThat(roles).hasSize(databaseSizeBeforeCreate + 1);
        Role testRole = roles.get(roles.size() - 1);
        assertThat(testRole.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testRole.getLastModifiedTime()).isEqualTo(DEFAULT_LAST_MODIFIED_TIME);
        assertThat(testRole.getAuths()).isEqualTo(DEFAULT_AUTHS);
        assertThat(testRole.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllRoles() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        // Get all the roles
        restRoleMockMvc.perform(get("/api/roles?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(role.getId().intValue())))
                .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.intValue())))
                .andExpect(jsonPath("$.[*].lastModifiedTime").value(hasItem(DEFAULT_LAST_MODIFIED_TIME.intValue())))
                .andExpect(jsonPath("$.[*].auths").value(hasItem(DEFAULT_AUTHS.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getRole() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);

        // Get the role
        restRoleMockMvc.perform(get("/api/roles/{id}", role.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(role.getId().intValue()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.intValue()))
            .andExpect(jsonPath("$.lastModifiedTime").value(DEFAULT_LAST_MODIFIED_TIME.intValue()))
            .andExpect(jsonPath("$.auths").value(DEFAULT_AUTHS.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRole() throws Exception {
        // Get the role
        restRoleMockMvc.perform(get("/api/roles/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRole() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);
        int databaseSizeBeforeUpdate = roleRepository.findAll().size();

        // Update the role
        Role updatedRole = roleRepository.findOne(role.getId());
        updatedRole
                .createTime(UPDATED_CREATE_TIME)
                .lastModifiedTime(UPDATED_LAST_MODIFIED_TIME)
                .auths(UPDATED_AUTHS)
                .name(UPDATED_NAME);
        RoleDTO roleDTO = roleMapper.roleToRoleDTO(updatedRole);

        restRoleMockMvc.perform(put("/api/roles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(roleDTO)))
                .andExpect(status().isOk());

        // Validate the Role in the database
        List<Role> roles = roleRepository.findAll();
        assertThat(roles).hasSize(databaseSizeBeforeUpdate);
        Role testRole = roles.get(roles.size() - 1);
        assertThat(testRole.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testRole.getLastModifiedTime()).isEqualTo(UPDATED_LAST_MODIFIED_TIME);
        assertThat(testRole.getAuths()).isEqualTo(UPDATED_AUTHS);
        assertThat(testRole.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteRole() throws Exception {
        // Initialize the database
        roleRepository.saveAndFlush(role);
        int databaseSizeBeforeDelete = roleRepository.findAll().size();

        // Get the role
        restRoleMockMvc.perform(delete("/api/roles/{id}", role.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Role> roles = roleRepository.findAll();
        assertThat(roles).hasSize(databaseSizeBeforeDelete - 1);
    }
}
