package com.yijia.yy.web.rest;

import com.yijia.yy.YiyingOaApp;

import com.yijia.yy.domain.FileInfo;
import com.yijia.yy.repository.FileInfoRepository;
import com.yijia.yy.service.FileInfoService;
import com.yijia.yy.service.dto.FileInfoDTO;
import com.yijia.yy.service.mapper.FileInfoMapper;

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
 * Test class for the FileInfoResource REST controller.
 *
 * @see FileInfoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YiyingOaApp.class)
public class FileInfoResourceIntTest {

    private static final String DEFAULT_ORIGIN_NAME = "AAAAA";
    private static final String UPDATED_ORIGIN_NAME = "BBBBB";
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final Long DEFAULT_CREATE_TIME = 1L;
    private static final Long UPDATED_CREATE_TIME = 2L;
    private static final String DEFAULT_CREATOR = "AAAAA";
    private static final String UPDATED_CREATOR = "BBBBB";
    private static final String DEFAULT_LAST_MODIFIER = "AAAAA";
    private static final String UPDATED_LAST_MODIFIER = "BBBBB";

    private static final Long DEFAULT_LAST_MODIFIED_TIME = 1L;
    private static final Long UPDATED_LAST_MODIFIED_TIME = 2L;

    @Inject
    private FileInfoRepository fileInfoRepository;

    @Inject
    private FileInfoMapper fileInfoMapper;

    @Inject
    private FileInfoService fileInfoService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restFileInfoMockMvc;

    private FileInfo fileInfo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FileInfoResource fileInfoResource = new FileInfoResource();
        ReflectionTestUtils.setField(fileInfoResource, "fileInfoService", fileInfoService);
        this.restFileInfoMockMvc = MockMvcBuilders.standaloneSetup(fileInfoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileInfo createEntity(EntityManager em) {
        FileInfo fileInfo = new FileInfo()
                .originName(DEFAULT_ORIGIN_NAME)
                .name(DEFAULT_NAME)
                .createTime(DEFAULT_CREATE_TIME)
                .creator(DEFAULT_CREATOR);
        return fileInfo;
    }

    @Before
    public void initTest() {
        fileInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createFileInfo() throws Exception {
        int databaseSizeBeforeCreate = fileInfoRepository.findAll().size();

        // Create the FileInfo
        FileInfoDTO fileInfoDTO = fileInfoMapper.fileInfoToFileInfoDTO(fileInfo);

        restFileInfoMockMvc.perform(post("/api/file-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fileInfoDTO)))
                .andExpect(status().isCreated());

        // Validate the FileInfo in the database
        List<FileInfo> fileInfos = fileInfoRepository.findAll();
        assertThat(fileInfos).hasSize(databaseSizeBeforeCreate + 1);
        FileInfo testFileInfo = fileInfos.get(fileInfos.size() - 1);
        assertThat(testFileInfo.getOriginName()).isEqualTo(DEFAULT_ORIGIN_NAME);
        assertThat(testFileInfo.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFileInfo.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testFileInfo.getCreator()).isEqualTo(DEFAULT_CREATOR);
    }

    @Test
    @Transactional
    public void getAllFileInfos() throws Exception {
        // Initialize the database
        fileInfoRepository.saveAndFlush(fileInfo);

        // Get all the fileInfos
        restFileInfoMockMvc.perform(get("/api/file-infos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(fileInfo.getId().intValue())))
                .andExpect(jsonPath("$.[*].originName").value(hasItem(DEFAULT_ORIGIN_NAME.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.intValue())))
                .andExpect(jsonPath("$.[*].creator").value(hasItem(DEFAULT_CREATOR.toString())))
                .andExpect(jsonPath("$.[*].lastModifier").value(hasItem(DEFAULT_LAST_MODIFIER.toString())))
                .andExpect(jsonPath("$.[*].lastModifiedTime").value(hasItem(DEFAULT_LAST_MODIFIED_TIME.intValue())));
    }

    @Test
    @Transactional
    public void getFileInfo() throws Exception {
        // Initialize the database
        fileInfoRepository.saveAndFlush(fileInfo);

        // Get the fileInfo
        restFileInfoMockMvc.perform(get("/api/file-infos/{id}", fileInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(fileInfo.getId().intValue()))
            .andExpect(jsonPath("$.originName").value(DEFAULT_ORIGIN_NAME.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.intValue()))
            .andExpect(jsonPath("$.creator").value(DEFAULT_CREATOR.toString()))
            .andExpect(jsonPath("$.lastModifier").value(DEFAULT_LAST_MODIFIER.toString()))
            .andExpect(jsonPath("$.lastModifiedTime").value(DEFAULT_LAST_MODIFIED_TIME.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFileInfo() throws Exception {
        // Get the fileInfo
        restFileInfoMockMvc.perform(get("/api/file-infos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFileInfo() throws Exception {
        // Initialize the database
        fileInfoRepository.saveAndFlush(fileInfo);
        int databaseSizeBeforeUpdate = fileInfoRepository.findAll().size();

        // Update the fileInfo
        FileInfo updatedFileInfo = fileInfoRepository.findOne(fileInfo.getId());
        updatedFileInfo
                .originName(UPDATED_ORIGIN_NAME)
                .name(UPDATED_NAME)
                .createTime(UPDATED_CREATE_TIME)
                .creator(UPDATED_CREATOR);
        FileInfoDTO fileInfoDTO = fileInfoMapper.fileInfoToFileInfoDTO(updatedFileInfo);

        restFileInfoMockMvc.perform(put("/api/file-infos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fileInfoDTO)))
                .andExpect(status().isOk());

        // Validate the FileInfo in the database
        List<FileInfo> fileInfos = fileInfoRepository.findAll();
        assertThat(fileInfos).hasSize(databaseSizeBeforeUpdate);
        FileInfo testFileInfo = fileInfos.get(fileInfos.size() - 1);
        assertThat(testFileInfo.getOriginName()).isEqualTo(UPDATED_ORIGIN_NAME);
        assertThat(testFileInfo.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFileInfo.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testFileInfo.getCreator()).isEqualTo(UPDATED_CREATOR);
    }

    @Test
    @Transactional
    public void deleteFileInfo() throws Exception {
        // Initialize the database
        fileInfoRepository.saveAndFlush(fileInfo);
        int databaseSizeBeforeDelete = fileInfoRepository.findAll().size();

        // Get the fileInfo
        restFileInfoMockMvc.perform(delete("/api/file-infos/{id}", fileInfo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<FileInfo> fileInfos = fileInfoRepository.findAll();
        assertThat(fileInfos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
