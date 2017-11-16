package com.yijia.yy.web.rest;

import com.yijia.yy.YiyingOaApp;

import com.yijia.yy.domain.DirectorNeeds;
import com.yijia.yy.repository.DirectorNeedsRepository;
import com.yijia.yy.service.DirectorNeedsService;
import com.yijia.yy.service.dto.DirectorNeedsDTO;
import com.yijia.yy.service.mapper.DirectorNeedsMapper;

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
 * Test class for the DirectorNeedsResource REST controller.
 *
 * @see DirectorNeedsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YiyingOaApp.class)
public class DirectorNeedsResourceIntTest {


    @Inject
    private DirectorNeedsRepository directorNeedsRepository;

    @Inject
    private DirectorNeedsMapper directorNeedsMapper;

    @Inject
    private DirectorNeedsService directorNeedsService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDirectorNeedsMockMvc;

    private DirectorNeeds directorNeeds;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DirectorNeedsResource directorNeedsResource = new DirectorNeedsResource();
        ReflectionTestUtils.setField(directorNeedsResource, "directorNeedsService", directorNeedsService);
        this.restDirectorNeedsMockMvc = MockMvcBuilders.standaloneSetup(directorNeedsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DirectorNeeds createEntity(EntityManager em) {
        DirectorNeeds directorNeeds = new DirectorNeeds();
        return directorNeeds;
    }

    @Before
    public void initTest() {
        directorNeeds = createEntity(em);
    }

    @Test
    @Transactional
    public void createDirectorNeeds() throws Exception {
        int databaseSizeBeforeCreate = directorNeedsRepository.findAll().size();

        // Create the DirectorNeeds
        DirectorNeedsDTO directorNeedsDTO = directorNeedsMapper.directorNeedsToDirectorNeedsDTO(directorNeeds);

        restDirectorNeedsMockMvc.perform(post("/api/director-needs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(directorNeedsDTO)))
                .andExpect(status().isCreated());

        // Validate the DirectorNeeds in the database
        List<DirectorNeeds> directorNeeds = directorNeedsRepository.findAll();
        assertThat(directorNeeds).hasSize(databaseSizeBeforeCreate + 1);
        DirectorNeeds testDirectorNeeds = directorNeeds.get(directorNeeds.size() - 1);
    }

    @Test
    @Transactional
    public void getAllDirectorNeeds() throws Exception {
        // Initialize the database
        directorNeedsRepository.saveAndFlush(directorNeeds);

        // Get all the directorNeeds
        restDirectorNeedsMockMvc.perform(get("/api/director-needs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(directorNeeds.getId().intValue())));
    }

    @Test
    @Transactional
    public void getDirectorNeeds() throws Exception {
        // Initialize the database
        directorNeedsRepository.saveAndFlush(directorNeeds);

        // Get the directorNeeds
        restDirectorNeedsMockMvc.perform(get("/api/director-needs/{id}", directorNeeds.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(directorNeeds.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDirectorNeeds() throws Exception {
        // Get the directorNeeds
        restDirectorNeedsMockMvc.perform(get("/api/director-needs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDirectorNeeds() throws Exception {
        // Initialize the database
        directorNeedsRepository.saveAndFlush(directorNeeds);
        int databaseSizeBeforeUpdate = directorNeedsRepository.findAll().size();

        // Update the directorNeeds
        DirectorNeeds updatedDirectorNeeds = directorNeedsRepository.findOne(directorNeeds.getId());
        DirectorNeedsDTO directorNeedsDTO = directorNeedsMapper.directorNeedsToDirectorNeedsDTO(updatedDirectorNeeds);

        restDirectorNeedsMockMvc.perform(put("/api/director-needs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(directorNeedsDTO)))
                .andExpect(status().isOk());

        // Validate the DirectorNeeds in the database
        List<DirectorNeeds> directorNeeds = directorNeedsRepository.findAll();
        assertThat(directorNeeds).hasSize(databaseSizeBeforeUpdate);
        DirectorNeeds testDirectorNeeds = directorNeeds.get(directorNeeds.size() - 1);
    }

    @Test
    @Transactional
    public void deleteDirectorNeeds() throws Exception {
        // Initialize the database
        directorNeedsRepository.saveAndFlush(directorNeeds);
        int databaseSizeBeforeDelete = directorNeedsRepository.findAll().size();

        // Get the directorNeeds
        restDirectorNeedsMockMvc.perform(delete("/api/director-needs/{id}", directorNeeds.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<DirectorNeeds> directorNeeds = directorNeedsRepository.findAll();
        assertThat(directorNeeds).hasSize(databaseSizeBeforeDelete - 1);
    }
}
