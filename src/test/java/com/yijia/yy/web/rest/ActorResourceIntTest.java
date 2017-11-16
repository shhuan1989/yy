package com.yijia.yy.web.rest;

import com.yijia.yy.YiyingOaApp;

import com.yijia.yy.domain.Actor;
import com.yijia.yy.domain.enumeration.Gender;
import com.yijia.yy.repository.ActorRepository;
import com.yijia.yy.service.ActorService;
import com.yijia.yy.service.dto.ActorDTO;
import com.yijia.yy.service.mapper.ActorMapper;

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
 * Test class for the ActorResource REST controller.
 *
 * @see ActorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YiyingOaApp.class)
public class ActorResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final Gender DEFAULT_GENDER = Gender.FEMALE;
    private static final Gender UPDATED_GENDER = Gender.MALE;

    private static final Long DEFAULT_BIRTH_DATE = 1L;
    private static final Long UPDATED_BIRTH_DATE = 2L;

    private static final Float DEFAULT_HEIGHT = 1F;
    private static final Float UPDATED_HEIGHT = 2F;

    private static final Float DEFAULT_BUST = 1F;
    private static final Float UPDATED_BUST = 2F;

    private static final Float DEFAULT_WAIST = 1F;
    private static final Float UPDATED_WAIST = 2F;

    private static final Float DEFAULT_HIP = 1F;
    private static final Float UPDATED_HIP = 2F;
    private static final String DEFAULT_TEL = "AAAAA";
    private static final String UPDATED_TEL = "BBBBB";
    private static final String DEFAULT_INPUT_OPERATOR = "AAAAA";
    private static final String UPDATED_INPUT_OPERATOR = "BBBBB";

    private static final Long DEFAULT_INPUT_TIME = 1L;
    private static final Long UPDATED_INPUT_TIME = 2L;
    private static final String DEFAULT_LAST_MODIFIER = "AAAAA";
    private static final String UPDATED_LAST_MODIFIER = "BBBBB";

    private static final Long DEFAULT_LAST_MODIFIED_TIME = 1L;
    private static final Long UPDATED_LAST_MODIFIED_TIME = 2L;

    @Inject
    private ActorRepository actorRepository;

    @Inject
    private ActorMapper actorMapper;

    @Inject
    private ActorService actorService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restActorMockMvc;

    private Actor actor;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ActorResource actorResource = new ActorResource();
        ReflectionTestUtils.setField(actorResource, "actorService", actorService);
        this.restActorMockMvc = MockMvcBuilders.standaloneSetup(actorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Actor createEntity(EntityManager em) {
        Actor actor = new Actor()
                .name(DEFAULT_NAME)
                .gender(DEFAULT_GENDER)
                .birthDate(DEFAULT_BIRTH_DATE)
                .height(DEFAULT_HEIGHT)
                .bust(DEFAULT_BUST)
                .waist(DEFAULT_WAIST)
                .hip(DEFAULT_HIP)
                .tel(DEFAULT_TEL)
                .inputOperator(DEFAULT_INPUT_OPERATOR)
                .inputTime(DEFAULT_INPUT_TIME)
                .lastModifier(DEFAULT_LAST_MODIFIER)
                .lastModifiedTime(DEFAULT_LAST_MODIFIED_TIME);
        return actor;
    }

    @Before
    public void initTest() {
        actor = createEntity(em);
    }

    @Test
    @Transactional
    public void createActor() throws Exception {
        int databaseSizeBeforeCreate = actorRepository.findAll().size();

        // Create the Actor
        ActorDTO actorDTO = actorMapper.actorToActorDTO(actor);

        restActorMockMvc.perform(post("/api/actors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(actorDTO)))
                .andExpect(status().isCreated());

        // Validate the Actor in the database
        List<Actor> actors = actorRepository.findAll();
        assertThat(actors).hasSize(databaseSizeBeforeCreate + 1);
        Actor testActor = actors.get(actors.size() - 1);
        assertThat(testActor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testActor.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testActor.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testActor.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testActor.getBust()).isEqualTo(DEFAULT_BUST);
        assertThat(testActor.getWaist()).isEqualTo(DEFAULT_WAIST);
        assertThat(testActor.getHip()).isEqualTo(DEFAULT_HIP);
        assertThat(testActor.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testActor.getInputOperator()).isEqualTo(DEFAULT_INPUT_OPERATOR);
        assertThat(testActor.getInputTime()).isEqualTo(DEFAULT_INPUT_TIME);
        assertThat(testActor.getLastModifier()).isEqualTo(DEFAULT_LAST_MODIFIER);
        assertThat(testActor.getLastModifiedTime()).isEqualTo(DEFAULT_LAST_MODIFIED_TIME);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = actorRepository.findAll().size();
        // set the field null
        actor.setName(null);

        // Create the Actor, which fails.
        ActorDTO actorDTO = actorMapper.actorToActorDTO(actor);

        restActorMockMvc.perform(post("/api/actors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(actorDTO)))
                .andExpect(status().isBadRequest());

        List<Actor> actors = actorRepository.findAll();
        assertThat(actors).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllActors() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actors
        restActorMockMvc.perform(get("/api/actors?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(actor.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
                .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.intValue())))
                .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT.doubleValue())))
                .andExpect(jsonPath("$.[*].bust").value(hasItem(DEFAULT_BUST.doubleValue())))
                .andExpect(jsonPath("$.[*].waist").value(hasItem(DEFAULT_WAIST.doubleValue())))
                .andExpect(jsonPath("$.[*].hip").value(hasItem(DEFAULT_HIP.doubleValue())))
                .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL.toString())))
                .andExpect(jsonPath("$.[*].inputOperator").value(hasItem(DEFAULT_INPUT_OPERATOR.toString())))
                .andExpect(jsonPath("$.[*].inputTime").value(hasItem(DEFAULT_INPUT_TIME.intValue())))
                .andExpect(jsonPath("$.[*].lastModifier").value(hasItem(DEFAULT_LAST_MODIFIER.toString())))
                .andExpect(jsonPath("$.[*].lastModifiedTime").value(hasItem(DEFAULT_LAST_MODIFIED_TIME.intValue())));
    }

    @Test
    @Transactional
    public void getActor() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get the actor
        restActorMockMvc.perform(get("/api/actors/{id}", actor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(actor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.intValue()))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT.doubleValue()))
            .andExpect(jsonPath("$.bust").value(DEFAULT_BUST.doubleValue()))
            .andExpect(jsonPath("$.waist").value(DEFAULT_WAIST.doubleValue()))
            .andExpect(jsonPath("$.hip").value(DEFAULT_HIP.doubleValue()))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL.toString()))
            .andExpect(jsonPath("$.inputOperator").value(DEFAULT_INPUT_OPERATOR.toString()))
            .andExpect(jsonPath("$.inputTime").value(DEFAULT_INPUT_TIME.intValue()))
            .andExpect(jsonPath("$.lastModifier").value(DEFAULT_LAST_MODIFIER.toString()))
            .andExpect(jsonPath("$.lastModifiedTime").value(DEFAULT_LAST_MODIFIED_TIME.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingActor() throws Exception {
        // Get the actor
        restActorMockMvc.perform(get("/api/actors/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActor() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);
        int databaseSizeBeforeUpdate = actorRepository.findAll().size();

        // Update the actor
        Actor updatedActor = actorRepository.findOne(actor.getId());
        updatedActor
                .name(UPDATED_NAME)
                .gender(UPDATED_GENDER)
                .birthDate(UPDATED_BIRTH_DATE)
                .height(UPDATED_HEIGHT)
                .bust(UPDATED_BUST)
                .waist(UPDATED_WAIST)
                .hip(UPDATED_HIP)
                .tel(UPDATED_TEL)
                .inputOperator(UPDATED_INPUT_OPERATOR)
                .inputTime(UPDATED_INPUT_TIME)
                .lastModifier(UPDATED_LAST_MODIFIER)
                .lastModifiedTime(UPDATED_LAST_MODIFIED_TIME);
        ActorDTO actorDTO = actorMapper.actorToActorDTO(updatedActor);

        restActorMockMvc.perform(put("/api/actors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(actorDTO)))
                .andExpect(status().isOk());

        // Validate the Actor in the database
        List<Actor> actors = actorRepository.findAll();
        assertThat(actors).hasSize(databaseSizeBeforeUpdate);
        Actor testActor = actors.get(actors.size() - 1);
        assertThat(testActor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testActor.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testActor.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testActor.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testActor.getBust()).isEqualTo(UPDATED_BUST);
        assertThat(testActor.getWaist()).isEqualTo(UPDATED_WAIST);
        assertThat(testActor.getHip()).isEqualTo(UPDATED_HIP);
        assertThat(testActor.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testActor.getInputOperator()).isEqualTo(UPDATED_INPUT_OPERATOR);
        assertThat(testActor.getInputTime()).isEqualTo(UPDATED_INPUT_TIME);
        assertThat(testActor.getLastModifier()).isEqualTo(UPDATED_LAST_MODIFIER);
        assertThat(testActor.getLastModifiedTime()).isEqualTo(UPDATED_LAST_MODIFIED_TIME);
    }

    @Test
    @Transactional
    public void deleteActor() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);
        int databaseSizeBeforeDelete = actorRepository.findAll().size();

        // Get the actor
        restActorMockMvc.perform(delete("/api/actors/{id}", actor.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Actor> actors = actorRepository.findAll();
        assertThat(actors).hasSize(databaseSizeBeforeDelete - 1);
    }
}
