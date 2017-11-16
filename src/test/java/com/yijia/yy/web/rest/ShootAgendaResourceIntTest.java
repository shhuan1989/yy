package com.yijia.yy.web.rest;

import com.yijia.yy.yyOaApp;

import com.yijia.yy.domain.ShootAgenda;
import com.yijia.yy.repository.ShootAgendaRepository;
import com.yijia.yy.service.ShootAgendaService;
import com.yijia.yy.service.dto.ShootAgendaDTO;
import com.yijia.yy.service.mapper.ShootAgendaMapper;

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
 * Test class for the ShootAgendaResource REST controller.
 *
 * @see ShootAgendaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = yyOaApp.class)
public class ShootAgendaResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";

    private static final Long DEFAULT_START_TIME = 1L;
    private static final Long UPDATED_START_TIME = 2L;

    private static final Long DEFAULT_END_TIME = 1L;
    private static final Long UPDATED_END_TIME = 2L;
    private static final String DEFAULT_BACKGROUD_COLOR = "AAAAA";
    private static final String UPDATED_BACKGROUD_COLOR = "BBBBB";
    private static final String DEFAULT_BORDER_COLOR = "AAAAA";
    private static final String UPDATED_BORDER_COLOR = "BBBBB";

    private static final Boolean DEFAULT_ALL_DAY = false;
    private static final Boolean UPDATED_ALL_DAY = true;
    private static final String DEFAULT_URL = "AAAAA";
    private static final String UPDATED_URL = "BBBBB";
    private static final String DEFAULT_LOCATION = "AAAAA";
    private static final String UPDATED_LOCATION = "BBBBB";

    @Inject
    private ShootAgendaRepository shootAgendaRepository;

    @Inject
    private ShootAgendaMapper shootAgendaMapper;

    @Inject
    private ShootAgendaService shootAgendaService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restShootAgendaMockMvc;

    private ShootAgenda shootAgenda;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ShootAgendaResource shootAgendaResource = new ShootAgendaResource();
        ReflectionTestUtils.setField(shootAgendaResource, "shootAgendaService", shootAgendaService);
        this.restShootAgendaMockMvc = MockMvcBuilders.standaloneSetup(shootAgendaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShootAgenda createEntity(EntityManager em) {
        ShootAgenda shootAgenda = new ShootAgenda()
                .title(DEFAULT_TITLE)
                .startTime(DEFAULT_START_TIME)
                .endTime(DEFAULT_END_TIME)
                .backgroundColor(DEFAULT_BACKGROUD_COLOR)
                .borderColor(DEFAULT_BORDER_COLOR)
                .allDay(DEFAULT_ALL_DAY)
                .url(DEFAULT_URL)
                .location(DEFAULT_LOCATION);
        return shootAgenda;
    }

    @Before
    public void initTest() {
        shootAgenda = createEntity(em);
    }

    @Test
    @Transactional
    public void createShootAgenda() throws Exception {
        int databaseSizeBeforeCreate = shootAgendaRepository.findAll().size();

        // Create the ShootAgenda
        ShootAgendaDTO shootAgendaDTO = shootAgendaMapper.shootAgendaToShootAgendaDTO(shootAgenda);

        restShootAgendaMockMvc.perform(post("/api/shoot-agenda")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(shootAgendaDTO)))
                .andExpect(status().isCreated());

        // Validate the ShootAgenda in the database
        List<ShootAgenda> shootAgenda = shootAgendaRepository.findAll();
        assertThat(shootAgenda).hasSize(databaseSizeBeforeCreate + 1);
        ShootAgenda testShootAgenda = shootAgenda.get(shootAgenda.size() - 1);
        assertThat(testShootAgenda.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testShootAgenda.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testShootAgenda.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testShootAgenda.getBackgroundColor()).isEqualTo(DEFAULT_BACKGROUD_COLOR);
        assertThat(testShootAgenda.getBorderColor()).isEqualTo(DEFAULT_BORDER_COLOR);
        assertThat(testShootAgenda.isAllDay()).isEqualTo(DEFAULT_ALL_DAY);
        assertThat(testShootAgenda.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testShootAgenda.getLocation()).isEqualTo(DEFAULT_LOCATION);
    }

    @Test
    @Transactional
    public void checkStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = shootAgendaRepository.findAll().size();
        // set the field null
        shootAgenda.setStartTime(null);

        // Create the ShootAgenda, which fails.
        ShootAgendaDTO shootAgendaDTO = shootAgendaMapper.shootAgendaToShootAgendaDTO(shootAgenda);

        restShootAgendaMockMvc.perform(post("/api/shoot-agenda")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(shootAgendaDTO)))
                .andExpect(status().isBadRequest());

        List<ShootAgenda> shootAgenda = shootAgendaRepository.findAll();
        assertThat(shootAgenda).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = shootAgendaRepository.findAll().size();
        // set the field null
        shootAgenda.setEndTime(null);

        // Create the ShootAgenda, which fails.
        ShootAgendaDTO shootAgendaDTO = shootAgendaMapper.shootAgendaToShootAgendaDTO(shootAgenda);

        restShootAgendaMockMvc.perform(post("/api/shoot-agenda")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(shootAgendaDTO)))
                .andExpect(status().isBadRequest());

        List<ShootAgenda> shootAgenda = shootAgendaRepository.findAll();
        assertThat(shootAgenda).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllShootAgenda() throws Exception {
        // Initialize the database
        shootAgendaRepository.saveAndFlush(shootAgenda);

        // Get all the shootAgenda
        restShootAgendaMockMvc.perform(get("/api/shoot-agenda?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(shootAgenda.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.intValue())))
                .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.intValue())))
                .andExpect(jsonPath("$.[*].backgroudColor").value(hasItem(DEFAULT_BACKGROUD_COLOR.toString())))
                .andExpect(jsonPath("$.[*].borderColor").value(hasItem(DEFAULT_BORDER_COLOR.toString())))
                .andExpect(jsonPath("$.[*].allDay").value(hasItem(DEFAULT_ALL_DAY.booleanValue())))
                .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
                .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())));
    }

    @Test
    @Transactional
    public void getShootAgenda() throws Exception {
        // Initialize the database
        shootAgendaRepository.saveAndFlush(shootAgenda);

        // Get the shootAgenda
        restShootAgendaMockMvc.perform(get("/api/shoot-agenda/{id}", shootAgenda.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(shootAgenda.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.intValue()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.intValue()))
            .andExpect(jsonPath("$.backgroudColor").value(DEFAULT_BACKGROUD_COLOR.toString()))
            .andExpect(jsonPath("$.borderColor").value(DEFAULT_BORDER_COLOR.toString()))
            .andExpect(jsonPath("$.allDay").value(DEFAULT_ALL_DAY.booleanValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingShootAgenda() throws Exception {
        // Get the shootAgenda
        restShootAgendaMockMvc.perform(get("/api/shoot-agenda/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShootAgenda() throws Exception {
        // Initialize the database
        shootAgendaRepository.saveAndFlush(shootAgenda);
        int databaseSizeBeforeUpdate = shootAgendaRepository.findAll().size();

        // Update the shootAgenda
        ShootAgenda updatedShootAgenda = shootAgendaRepository.findOne(shootAgenda.getId());
        updatedShootAgenda
                .title(UPDATED_TITLE)
                .startTime(UPDATED_START_TIME)
                .endTime(UPDATED_END_TIME)
                .backgroundColor(UPDATED_BACKGROUD_COLOR)
                .borderColor(UPDATED_BORDER_COLOR)
                .allDay(UPDATED_ALL_DAY)
                .url(UPDATED_URL)
                .location(UPDATED_LOCATION);
        ShootAgendaDTO shootAgendaDTO = shootAgendaMapper.shootAgendaToShootAgendaDTO(updatedShootAgenda);

        restShootAgendaMockMvc.perform(put("/api/shoot-agenda")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(shootAgendaDTO)))
                .andExpect(status().isOk());

        // Validate the ShootAgenda in the database
        List<ShootAgenda> shootAgenda = shootAgendaRepository.findAll();
        assertThat(shootAgenda).hasSize(databaseSizeBeforeUpdate);
        ShootAgenda testShootAgenda = shootAgenda.get(shootAgenda.size() - 1);
        assertThat(testShootAgenda.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testShootAgenda.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testShootAgenda.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testShootAgenda.getBackgroundColor()).isEqualTo(UPDATED_BACKGROUD_COLOR);
        assertThat(testShootAgenda.getBorderColor()).isEqualTo(UPDATED_BORDER_COLOR);
        assertThat(testShootAgenda.isAllDay()).isEqualTo(UPDATED_ALL_DAY);
        assertThat(testShootAgenda.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testShootAgenda.getLocation()).isEqualTo(UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void deleteShootAgenda() throws Exception {
        // Initialize the database
        shootAgendaRepository.saveAndFlush(shootAgenda);
        int databaseSizeBeforeDelete = shootAgendaRepository.findAll().size();

        // Get the shootAgenda
        restShootAgendaMockMvc.perform(delete("/api/shoot-agenda/{id}", shootAgenda.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ShootAgenda> shootAgenda = shootAgendaRepository.findAll();
        assertThat(shootAgenda).hasSize(databaseSizeBeforeDelete - 1);
    }
}
