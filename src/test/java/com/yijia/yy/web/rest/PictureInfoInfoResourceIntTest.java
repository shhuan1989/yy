package com.yijia.yy.web.rest;

import com.yijia.yy.yyOaApp;

import com.yijia.yy.domain.PictureInfo;
import com.yijia.yy.repository.PictureRepository;
import com.yijia.yy.service.PictureService;

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
 * Test class for the PictureResource REST controller.
 *
 * @see PictureInfoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = yyOaApp.class)
public class PictureInfoInfoResourceIntTest {

    private static final String DEFAULT_ORIGIN_NAME = "AAAAA";
    private static final String UPDATED_ORIGIN_NAME = "BBBBB";
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_THUMBNAIL_NAME = "AAAAA";
    private static final String UPDATED_THUMBNAIL_NAME = "BBBBB";

    private static final Long DEFAULT_CREATE_TIME = 1L;
    private static final Long UPDATED_CREATE_TIME = 2L;
    private static final String DEFAULT_CREATOR = "AAAAA";
    private static final String UPDATED_CREATOR = "BBBBB";
    private static final String DEFAULT_LAST_MODIFIER = "AAAAA";
    private static final String UPDATED_LAST_MODIFIER = "BBBBB";

    private static final Long DEFAULT_LAST_MODIFIED_TIME = 1L;
    private static final Long UPDATED_LAST_MODIFIED_TIME = 2L;

    @Inject
    private PictureRepository pictureRepository;

    @Inject
    private PictureService pictureService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPictureMockMvc;

    private PictureInfo pictureInfo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PictureInfoResource pictureInfoResource = new PictureInfoResource();
        ReflectionTestUtils.setField(pictureInfoResource, "pictureService", pictureService);
        this.restPictureMockMvc = MockMvcBuilders.standaloneSetup(pictureInfoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PictureInfo createEntity(EntityManager em) {
        PictureInfo pictureInfo = new PictureInfo()
                .originName(DEFAULT_ORIGIN_NAME)
                .name(DEFAULT_NAME)
                .createTime(DEFAULT_CREATE_TIME)
                .creator(DEFAULT_CREATOR);
        return pictureInfo;
    }

    @Before
    public void initTest() {
        pictureInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createPicture() throws Exception {
        int databaseSizeBeforeCreate = pictureRepository.findAll().size();

        // Create the PictureInfo

        restPictureMockMvc.perform(post("/api/pictureInfos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pictureInfo)))
                .andExpect(status().isCreated());

        // Validate the PictureInfo in the database
        List<PictureInfo> pictureInfos = pictureRepository.findAll();
        assertThat(pictureInfos).hasSize(databaseSizeBeforeCreate + 1);
        PictureInfo testPictureInfo = pictureInfos.get(pictureInfos.size() - 1);
        assertThat(testPictureInfo.getOriginName()).isEqualTo(DEFAULT_ORIGIN_NAME);
        assertThat(testPictureInfo.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPictureInfo.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testPictureInfo.getCreator()).isEqualTo(DEFAULT_CREATOR);
    }

    @Test
    @Transactional
    public void getAllPictures() throws Exception {
        // Initialize the database
        pictureRepository.saveAndFlush(pictureInfo);

        // Get all the pictures
        restPictureMockMvc.perform(get("/resource/pictures?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(pictureInfo.getId().intValue())))
                .andExpect(jsonPath("$.[*].originName").value(hasItem(DEFAULT_ORIGIN_NAME.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].thumbnailName").value(hasItem(DEFAULT_THUMBNAIL_NAME.toString())))
                .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.intValue())))
                .andExpect(jsonPath("$.[*].creator").value(hasItem(DEFAULT_CREATOR.toString())))
                .andExpect(jsonPath("$.[*].lastModifier").value(hasItem(DEFAULT_LAST_MODIFIER.toString())))
                .andExpect(jsonPath("$.[*].lastModifiedTime").value(hasItem(DEFAULT_LAST_MODIFIED_TIME.intValue())));
    }

    @Test
    @Transactional
    public void getPicture() throws Exception {
        // Initialize the database
        pictureRepository.saveAndFlush(pictureInfo);

        // Get the pictureInfo
        restPictureMockMvc.perform(get("/resource/pictures/{id}", pictureInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pictureInfo.getId().intValue()))
            .andExpect(jsonPath("$.originName").value(DEFAULT_ORIGIN_NAME.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.thumbnailName").value(DEFAULT_THUMBNAIL_NAME.toString()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.intValue()))
            .andExpect(jsonPath("$.creator").value(DEFAULT_CREATOR.toString()))
            .andExpect(jsonPath("$.lastModifier").value(DEFAULT_LAST_MODIFIER.toString()))
            .andExpect(jsonPath("$.lastModifiedTime").value(DEFAULT_LAST_MODIFIED_TIME.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPicture() throws Exception {
        // Get the pictureInfo
        restPictureMockMvc.perform(get("/resource/pictures/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePicture() throws Exception {
        // Initialize the database
        pictureService.save(pictureInfo);

        int databaseSizeBeforeUpdate = pictureRepository.findAll().size();

        // Update the pictureInfo
        PictureInfo updatedPictureInfo = pictureRepository.findOne(pictureInfo.getId());
        updatedPictureInfo
                .originName(UPDATED_ORIGIN_NAME)
                .name(UPDATED_NAME)
                .createTime(UPDATED_CREATE_TIME)
                .creator(UPDATED_CREATOR);

        restPictureMockMvc.perform(put("/api/pictureInfos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPictureInfo)))
                .andExpect(status().isOk());

        // Validate the PictureInfo in the database
        List<PictureInfo> pictureInfos = pictureRepository.findAll();
        assertThat(pictureInfos).hasSize(databaseSizeBeforeUpdate);
        PictureInfo testPictureInfo = pictureInfos.get(pictureInfos.size() - 1);
        assertThat(testPictureInfo.getOriginName()).isEqualTo(UPDATED_ORIGIN_NAME);
        assertThat(testPictureInfo.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPictureInfo.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testPictureInfo.getCreator()).isEqualTo(UPDATED_CREATOR);
    }

    @Test
    @Transactional
    public void deletePicture() throws Exception {
        // Initialize the database
        pictureService.save(pictureInfo);

        int databaseSizeBeforeDelete = pictureRepository.findAll().size();

        // Get the pictureInfo
        restPictureMockMvc.perform(delete("/api/pictureInfos/{id}", pictureInfo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PictureInfo> pictureInfos = pictureRepository.findAll();
        assertThat(pictureInfos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
