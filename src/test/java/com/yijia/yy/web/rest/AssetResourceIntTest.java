package com.yijia.yy.web.rest;

import com.yijia.yy.yyOaApp;

import com.yijia.yy.domain.Asset;
import com.yijia.yy.repository.AssetRepository;
import com.yijia.yy.service.AssetService;
import com.yijia.yy.service.dto.AssetDTO;
import com.yijia.yy.service.mapper.AssetMapper;

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
 * Test class for the AssetResource REST controller.
 *
 * @see AssetResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = yyOaApp.class)
public class AssetResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final Integer DEFAULT_AMOUNT = 1;
    private static final Integer UPDATED_AMOUNT = 2;

    private static final Boolean DEFAULT_NEED_RETURN = false;
    private static final Boolean UPDATED_NEED_RETURN = true;

    private static final Long DEFAULT_LAST_MODIFIED_TIME = 1L;
    private static final Long UPDATED_LAST_MODIFIED_TIME = 2L;

    @Inject
    private AssetRepository assetRepository;

    @Inject
    private AssetMapper assetMapper;

    @Inject
    private AssetService assetService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAssetMockMvc;

    private Asset asset;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AssetResource assetResource = new AssetResource();
        ReflectionTestUtils.setField(assetResource, "assetService", assetService);
        this.restAssetMockMvc = MockMvcBuilders.standaloneSetup(assetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Asset createEntity(EntityManager em) {
        Asset asset = new Asset()
                .name(DEFAULT_NAME)
                .amount(DEFAULT_AMOUNT)
                .needReturn(DEFAULT_NEED_RETURN)
                .lastModifiedTime(DEFAULT_LAST_MODIFIED_TIME);
        return asset;
    }

    @Before
    public void initTest() {
        asset = createEntity(em);
    }

    @Test
    @Transactional
    public void createAsset() throws Exception {
        int databaseSizeBeforeCreate = assetRepository.findAll().size();

        // Create the Asset
        AssetDTO assetDTO = assetMapper.assetToAssetDTO(asset);

        restAssetMockMvc.perform(post("/api/assets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(assetDTO)))
                .andExpect(status().isCreated());

        // Validate the Asset in the database
        List<Asset> assets = assetRepository.findAll();
        assertThat(assets).hasSize(databaseSizeBeforeCreate + 1);
        Asset testAsset = assets.get(assets.size() - 1);
        assertThat(testAsset.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAsset.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testAsset.isNeedReturn()).isEqualTo(DEFAULT_NEED_RETURN);
        assertThat(testAsset.getLastModifiedTime()).isEqualTo(DEFAULT_LAST_MODIFIED_TIME);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetRepository.findAll().size();
        // set the field null
        asset.setName(null);

        // Create the Asset, which fails.
        AssetDTO assetDTO = assetMapper.assetToAssetDTO(asset);

        restAssetMockMvc.perform(post("/api/assets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(assetDTO)))
                .andExpect(status().isBadRequest());

        List<Asset> assets = assetRepository.findAll();
        assertThat(assets).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAssets() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get all the assets
        restAssetMockMvc.perform(get("/api/assets?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(asset.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
                .andExpect(jsonPath("$.[*].needReturn").value(hasItem(DEFAULT_NEED_RETURN.booleanValue())))
                .andExpect(jsonPath("$.[*].lastModifiedTime").value(hasItem(DEFAULT_LAST_MODIFIED_TIME.intValue())));
    }

    @Test
    @Transactional
    public void getAsset() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);

        // Get the asset
        restAssetMockMvc.perform(get("/api/assets/{id}", asset.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(asset.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT))
            .andExpect(jsonPath("$.needReturn").value(DEFAULT_NEED_RETURN.booleanValue()))
            .andExpect(jsonPath("$.lastModifiedTime").value(DEFAULT_LAST_MODIFIED_TIME.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAsset() throws Exception {
        // Get the asset
        restAssetMockMvc.perform(get("/api/assets/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAsset() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);
        int databaseSizeBeforeUpdate = assetRepository.findAll().size();

        // Update the asset
        Asset updatedAsset = assetRepository.findOne(asset.getId());
        updatedAsset
                .name(UPDATED_NAME)
                .amount(UPDATED_AMOUNT)
                .needReturn(UPDATED_NEED_RETURN)
                .lastModifiedTime(UPDATED_LAST_MODIFIED_TIME);
        AssetDTO assetDTO = assetMapper.assetToAssetDTO(updatedAsset);

        restAssetMockMvc.perform(put("/api/assets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(assetDTO)))
                .andExpect(status().isOk());

        // Validate the Asset in the database
        List<Asset> assets = assetRepository.findAll();
        assertThat(assets).hasSize(databaseSizeBeforeUpdate);
        Asset testAsset = assets.get(assets.size() - 1);
        assertThat(testAsset.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAsset.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testAsset.isNeedReturn()).isEqualTo(UPDATED_NEED_RETURN);
        assertThat(testAsset.getLastModifiedTime()).isEqualTo(UPDATED_LAST_MODIFIED_TIME);
    }

    @Test
    @Transactional
    public void deleteAsset() throws Exception {
        // Initialize the database
        assetRepository.saveAndFlush(asset);
        int databaseSizeBeforeDelete = assetRepository.findAll().size();

        // Get the asset
        restAssetMockMvc.perform(delete("/api/assets/{id}", asset.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Asset> assets = assetRepository.findAll();
        assertThat(assets).hasSize(databaseSizeBeforeDelete - 1);
    }
}
