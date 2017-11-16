package com.yijia.yy.web.rest;

import com.yijia.yy.yyOaApp;

import com.yijia.yy.domain.ShootConfig;
import com.yijia.yy.repository.ShootConfigRepository;
import com.yijia.yy.service.ShootConfigService;
import com.yijia.yy.service.dto.ShootConfigDTO;
import com.yijia.yy.service.mapper.ShootConfigMapper;

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
 * Test class for the ShootConfigsResource REST controller.
 *
 * @see ShootConfigResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = yyOaApp.class)
public class ShootConfigsResourceIntTestItem {


    @Inject
    private ShootConfigRepository shootConfigRepository;

    @Inject
    private ShootConfigMapper shootConfigMapper;

    @Inject
    private ShootConfigService shootConfigService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restShootConfigsMockMvc;

    private ShootConfig shootConfig;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ShootConfigResource shootConfigResource = new ShootConfigResource();
        ReflectionTestUtils.setField(shootConfigResource, "shootConfigService", shootConfigService);
        this.restShootConfigsMockMvc = MockMvcBuilders.standaloneSetup(shootConfigResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShootConfig createEntity(EntityManager em) {
        ShootConfig shootConfig = new ShootConfig();
        return shootConfig;
    }

    @Before
    public void initTest() {
        shootConfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createShootConfigs() throws Exception {
        int databaseSizeBeforeCreate = shootConfigRepository.findAll().size();

        // Create the ShootConfig
        ShootConfigDTO shootConfigDTO = shootConfigMapper.shootConfigToShootConfigDTO(shootConfig);

        restShootConfigsMockMvc.perform(post("/api/shoot-configs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(shootConfigDTO)))
                .andExpect(status().isCreated());

        // Validate the ShootConfig in the database
        List<ShootConfig> shootConfigs = shootConfigRepository.findAll();
        assertThat(shootConfigs).hasSize(databaseSizeBeforeCreate + 1);
        ShootConfig testShootConfig = shootConfigs.get(shootConfigs.size() - 1);
    }

    @Test
    @Transactional
    public void getAllShootConfigs() throws Exception {
        // Initialize the database
        shootConfigRepository.saveAndFlush(shootConfig);

        // Get all the shootConfig
        restShootConfigsMockMvc.perform(get("/api/shoot-configs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(shootConfig.getId().intValue())));
    }

    @Test
    @Transactional
    public void getShootConfigs() throws Exception {
        // Initialize the database
        shootConfigRepository.saveAndFlush(shootConfig);

        // Get the shootConfig
        restShootConfigsMockMvc.perform(get("/api/shoot-configs/{id}", shootConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(shootConfig.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingShootConfigs() throws Exception {
        // Get the shootConfig
        restShootConfigsMockMvc.perform(get("/api/shoot-configs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShootConfigs() throws Exception {
        // Initialize the database
        shootConfigRepository.saveAndFlush(shootConfig);
        int databaseSizeBeforeUpdate = shootConfigRepository.findAll().size();

        // Update the shootConfig
        ShootConfig updatedShootConfig = shootConfigRepository.findOne(shootConfig.getId());
        ShootConfigDTO shootConfigDTO = shootConfigMapper.shootConfigToShootConfigDTO(updatedShootConfig);

        restShootConfigsMockMvc.perform(put("/api/shoot-configs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(shootConfigDTO)))
                .andExpect(status().isOk());

        // Validate the ShootConfig in the database
        List<ShootConfig> shootConfigs = shootConfigRepository.findAll();
        assertThat(shootConfigs).hasSize(databaseSizeBeforeUpdate);
        ShootConfig testShootConfig = shootConfigs.get(shootConfigs.size() - 1);
    }

    @Test
    @Transactional
    public void deleteShootConfigs() throws Exception {
        // Initialize the database
        shootConfigRepository.saveAndFlush(shootConfig);
        int databaseSizeBeforeDelete = shootConfigRepository.findAll().size();

        // Get the shootConfig
        restShootConfigsMockMvc.perform(delete("/api/shoot-configs/{id}", shootConfig.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ShootConfig> shootConfigs = shootConfigRepository.findAll();
        assertThat(shootConfigs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
