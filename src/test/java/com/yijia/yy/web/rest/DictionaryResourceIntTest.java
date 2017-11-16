package com.yijia.yy.web.rest;

import com.yijia.yy.yyOaApp;

import com.yijia.yy.domain.Dictionary;
import com.yijia.yy.repository.DictionaryRepository;
import com.yijia.yy.service.DictionaryService;
import com.yijia.yy.service.dto.DictionaryDTO;
import com.yijia.yy.service.mapper.DictionaryMapper;

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
 * Test class for the DictionaryResource REST controller.
 *
 * @see DictionaryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = yyOaApp.class)
public class DictionaryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_CREATOR = "AAAAA";
    private static final String UPDATED_CREATOR = "BBBBB";

    private static final Long DEFAULT_CREATE_TIME = 1L;
    private static final Long UPDATED_CREATE_TIME = 2L;
    private static final String DEFAULT_COMMENT = "AAAAA";
    private static final String UPDATED_COMMENT = "BBBBB";

    private static final Boolean DEFAULT_IS_SYSTEM = false;
    private static final Boolean UPDATED_IS_SYSTEM = true;

    private static final Long DEFAULT_LAST_MODIFIED_TIME = 1L;
    private static final Long UPDATED_LAST_MODIFIED_TIME = 2L;
    private static final String DEFAULT_LAST_MODIFIER = "AAAAA";
    private static final String UPDATED_LAST_MODIFIER = "BBBBB";

    @Inject
    private DictionaryRepository dictionaryRepository;

    @Inject
    private DictionaryMapper dictionaryMapper;

    @Inject
    private DictionaryService dictionaryService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDictionaryMockMvc;

    private Dictionary dictionary;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DictionaryResource dictionaryResource = new DictionaryResource();
        ReflectionTestUtils.setField(dictionaryResource, "dictionaryService", dictionaryService);
        this.restDictionaryMockMvc = MockMvcBuilders.standaloneSetup(dictionaryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dictionary createEntity(EntityManager em) {
        Dictionary dictionary = new Dictionary()
                .name(DEFAULT_NAME)
                .creator(DEFAULT_CREATOR)
                .createTime(DEFAULT_CREATE_TIME)
                .comment(DEFAULT_COMMENT)
                .isSystem(DEFAULT_IS_SYSTEM)
                .lastModifiedTime(DEFAULT_LAST_MODIFIED_TIME)
                .lastModifier(DEFAULT_LAST_MODIFIER);
        return dictionary;
    }

    @Before
    public void initTest() {
        dictionary = createEntity(em);
    }

    @Test
    @Transactional
    public void createDictionary() throws Exception {
        int databaseSizeBeforeCreate = dictionaryRepository.findAll().size();

        // Create the Dictionary
        DictionaryDTO dictionaryDTO = dictionaryMapper.dictionaryToDictionaryDTO(dictionary);

        restDictionaryMockMvc.perform(post("/api/dictionaries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dictionaryDTO)))
                .andExpect(status().isCreated());

        // Validate the Dictionary in the database
        List<Dictionary> dictionaries = dictionaryRepository.findAll();
        assertThat(dictionaries).hasSize(databaseSizeBeforeCreate + 1);
        Dictionary testDictionary = dictionaries.get(dictionaries.size() - 1);
        assertThat(testDictionary.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDictionary.getCreator()).isEqualTo(DEFAULT_CREATOR);
        assertThat(testDictionary.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testDictionary.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testDictionary.isIsSystem()).isEqualTo(DEFAULT_IS_SYSTEM);
        assertThat(testDictionary.getLastModifiedTime()).isEqualTo(DEFAULT_LAST_MODIFIED_TIME);
        assertThat(testDictionary.getLastModifier()).isEqualTo(DEFAULT_LAST_MODIFIER);
    }

    @Test
    @Transactional
    public void getAllDictionaries() throws Exception {
        // Initialize the database
        dictionaryRepository.saveAndFlush(dictionary);

        // Get all the dictionaries
        restDictionaryMockMvc.perform(get("/api/dictionaries?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(dictionary.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].creator").value(hasItem(DEFAULT_CREATOR.toString())))
                .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.intValue())))
                .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
                .andExpect(jsonPath("$.[*].isSystem").value(hasItem(DEFAULT_IS_SYSTEM.booleanValue())))
                .andExpect(jsonPath("$.[*].lastModifiedTime").value(hasItem(DEFAULT_LAST_MODIFIED_TIME.intValue())))
                .andExpect(jsonPath("$.[*].lastModifier").value(hasItem(DEFAULT_LAST_MODIFIER.toString())));
    }

    @Test
    @Transactional
    public void getDictionary() throws Exception {
        // Initialize the database
        dictionaryRepository.saveAndFlush(dictionary);

        // Get the dictionary
        restDictionaryMockMvc.perform(get("/api/dictionaries/{id}", dictionary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dictionary.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.creator").value(DEFAULT_CREATOR.toString()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.intValue()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.isSystem").value(DEFAULT_IS_SYSTEM.booleanValue()))
            .andExpect(jsonPath("$.lastModifiedTime").value(DEFAULT_LAST_MODIFIED_TIME.intValue()))
            .andExpect(jsonPath("$.lastModifier").value(DEFAULT_LAST_MODIFIER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDictionary() throws Exception {
        // Get the dictionary
        restDictionaryMockMvc.perform(get("/api/dictionaries/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDictionary() throws Exception {
        // Initialize the database
        dictionaryRepository.saveAndFlush(dictionary);
        int databaseSizeBeforeUpdate = dictionaryRepository.findAll().size();

        // Update the dictionary
        Dictionary updatedDictionary = dictionaryRepository.findOne(dictionary.getId());
        updatedDictionary
                .name(UPDATED_NAME)
                .creator(UPDATED_CREATOR)
                .createTime(UPDATED_CREATE_TIME)
                .comment(UPDATED_COMMENT)
                .isSystem(UPDATED_IS_SYSTEM)
                .lastModifiedTime(UPDATED_LAST_MODIFIED_TIME)
                .lastModifier(UPDATED_LAST_MODIFIER);
        DictionaryDTO dictionaryDTO = dictionaryMapper.dictionaryToDictionaryDTO(updatedDictionary);

        restDictionaryMockMvc.perform(put("/api/dictionaries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dictionaryDTO)))
                .andExpect(status().isOk());

        // Validate the Dictionary in the database
        List<Dictionary> dictionaries = dictionaryRepository.findAll();
        assertThat(dictionaries).hasSize(databaseSizeBeforeUpdate);
        Dictionary testDictionary = dictionaries.get(dictionaries.size() - 1);
        assertThat(testDictionary.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDictionary.getCreator()).isEqualTo(UPDATED_CREATOR);
        assertThat(testDictionary.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testDictionary.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testDictionary.isIsSystem()).isEqualTo(UPDATED_IS_SYSTEM);
        assertThat(testDictionary.getLastModifiedTime()).isEqualTo(UPDATED_LAST_MODIFIED_TIME);
        assertThat(testDictionary.getLastModifier()).isEqualTo(UPDATED_LAST_MODIFIER);
    }

    @Test
    @Transactional
    public void deleteDictionary() throws Exception {
        // Initialize the database
        dictionaryRepository.saveAndFlush(dictionary);
        int databaseSizeBeforeDelete = dictionaryRepository.findAll().size();

        // Get the dictionary
        restDictionaryMockMvc.perform(delete("/api/dictionaries/{id}", dictionary.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Dictionary> dictionaries = dictionaryRepository.findAll();
        assertThat(dictionaries).hasSize(databaseSizeBeforeDelete - 1);
    }
}
