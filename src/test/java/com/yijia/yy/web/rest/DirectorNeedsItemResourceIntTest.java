package com.yijia.yy.web.rest;

import com.yijia.yy.yyOaApp;

import com.yijia.yy.domain.DirectorNeedsItem;
import com.yijia.yy.repository.DirectorNeedsItemRepository;
import com.yijia.yy.service.DirectorNeedsItemService;
import com.yijia.yy.service.dto.DirectorNeedsItemDTO;
import com.yijia.yy.service.mapper.DirectorNeedsItemMapper;

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
 * Test class for the DirectorNeedsItemResource REST controller.
 *
 * @see DirectorNeedsItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = yyOaApp.class)
public class DirectorNeedsItemResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final Integer DEFAULT_AMOUNT = 1;
    private static final Integer UPDATED_AMOUNT = 2;
    private static final String DEFAULT_MEMO = "AAAAA";
    private static final String UPDATED_MEMO = "BBBBB";

    @Inject
    private DirectorNeedsItemRepository directorNeedsItemRepository;

    @Inject
    private DirectorNeedsItemMapper directorNeedsItemMapper;

    @Inject
    private DirectorNeedsItemService directorNeedsItemService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDirectorNeedsItemMockMvc;

    private DirectorNeedsItem directorNeedsItem;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DirectorNeedsItemResource directorNeedsItemResource = new DirectorNeedsItemResource();
        ReflectionTestUtils.setField(directorNeedsItemResource, "directorNeedsItemService", directorNeedsItemService);
        this.restDirectorNeedsItemMockMvc = MockMvcBuilders.standaloneSetup(directorNeedsItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DirectorNeedsItem createEntity(EntityManager em) {
        DirectorNeedsItem directorNeedsItem = new DirectorNeedsItem()
                .name(DEFAULT_NAME)
                .amount(DEFAULT_AMOUNT)
                .memo(DEFAULT_MEMO);
        return directorNeedsItem;
    }

    @Before
    public void initTest() {
        directorNeedsItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createDirectorNeedsItem() throws Exception {
        int databaseSizeBeforeCreate = directorNeedsItemRepository.findAll().size();

        // Create the DirectorNeedsItem
        DirectorNeedsItemDTO directorNeedsItemDTO = directorNeedsItemMapper.directorNeedsItemToDirectorNeedsItemDTO(directorNeedsItem);

        restDirectorNeedsItemMockMvc.perform(post("/api/director-needs-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(directorNeedsItemDTO)))
                .andExpect(status().isCreated());

        // Validate the DirectorNeedsItem in the database
        List<DirectorNeedsItem> directorNeedsItems = directorNeedsItemRepository.findAll();
        assertThat(directorNeedsItems).hasSize(databaseSizeBeforeCreate + 1);
        DirectorNeedsItem testDirectorNeedsItem = directorNeedsItems.get(directorNeedsItems.size() - 1);
        assertThat(testDirectorNeedsItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDirectorNeedsItem.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testDirectorNeedsItem.getMemo()).isEqualTo(DEFAULT_MEMO);
    }

    @Test
    @Transactional
    public void getAllDirectorNeedsItems() throws Exception {
        // Initialize the database
        directorNeedsItemRepository.saveAndFlush(directorNeedsItem);

        // Get all the directorNeedsItems
        restDirectorNeedsItemMockMvc.perform(get("/api/director-needs-items?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(directorNeedsItem.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
                .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO.toString())));
    }

    @Test
    @Transactional
    public void getDirectorNeedsItem() throws Exception {
        // Initialize the database
        directorNeedsItemRepository.saveAndFlush(directorNeedsItem);

        // Get the directorNeedsItem
        restDirectorNeedsItemMockMvc.perform(get("/api/director-needs-items/{id}", directorNeedsItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(directorNeedsItem.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDirectorNeedsItem() throws Exception {
        // Get the directorNeedsItem
        restDirectorNeedsItemMockMvc.perform(get("/api/director-needs-items/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDirectorNeedsItem() throws Exception {
        // Initialize the database
        directorNeedsItemRepository.saveAndFlush(directorNeedsItem);
        int databaseSizeBeforeUpdate = directorNeedsItemRepository.findAll().size();

        // Update the directorNeedsItem
        DirectorNeedsItem updatedDirectorNeedsItem = directorNeedsItemRepository.findOne(directorNeedsItem.getId());
        updatedDirectorNeedsItem
                .name(UPDATED_NAME)
                .amount(UPDATED_AMOUNT)
                .memo(UPDATED_MEMO);
        DirectorNeedsItemDTO directorNeedsItemDTO = directorNeedsItemMapper.directorNeedsItemToDirectorNeedsItemDTO(updatedDirectorNeedsItem);

        restDirectorNeedsItemMockMvc.perform(put("/api/director-needs-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(directorNeedsItemDTO)))
                .andExpect(status().isOk());

        // Validate the DirectorNeedsItem in the database
        List<DirectorNeedsItem> directorNeedsItems = directorNeedsItemRepository.findAll();
        assertThat(directorNeedsItems).hasSize(databaseSizeBeforeUpdate);
        DirectorNeedsItem testDirectorNeedsItem = directorNeedsItems.get(directorNeedsItems.size() - 1);
        assertThat(testDirectorNeedsItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDirectorNeedsItem.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testDirectorNeedsItem.getMemo()).isEqualTo(UPDATED_MEMO);
    }

    @Test
    @Transactional
    public void deleteDirectorNeedsItem() throws Exception {
        // Initialize the database
        directorNeedsItemRepository.saveAndFlush(directorNeedsItem);
        int databaseSizeBeforeDelete = directorNeedsItemRepository.findAll().size();

        // Get the directorNeedsItem
        restDirectorNeedsItemMockMvc.perform(delete("/api/director-needs-items/{id}", directorNeedsItem.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<DirectorNeedsItem> directorNeedsItems = directorNeedsItemRepository.findAll();
        assertThat(directorNeedsItems).hasSize(databaseSizeBeforeDelete - 1);
    }
}
