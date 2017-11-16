package com.yijia.yy.web.rest;

import com.yijia.yy.yyOaApp;

import com.yijia.yy.domain.DirectorNeedsComment;
import com.yijia.yy.repository.DirectorNeedsCommentRepository;
import com.yijia.yy.service.DirectorNeedsCommentService;
import com.yijia.yy.service.dto.DirectorNeedsCommentDTO;
import com.yijia.yy.service.mapper.DirectorNeedsCommentMapper;

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
 * Test class for the DirectorNeedsCommentResource REST controller.
 *
 * @see DirectorNeedsCommentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = yyOaApp.class)
public class DirectorNeedsCommentResourceIntTest {

    private static final String DEFAULT_CONTENT = "AAAAA";
    private static final String UPDATED_CONTENT = "BBBBB";

    private static final Long DEFAULT_CREATE_TIME = 1L;
    private static final Long UPDATED_CREATE_TIME = 2L;

    @Inject
    private DirectorNeedsCommentRepository directorNeedsCommentRepository;

    @Inject
    private DirectorNeedsCommentMapper directorNeedsCommentMapper;

    @Inject
    private DirectorNeedsCommentService directorNeedsCommentService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDirectorNeedsCommentMockMvc;

    private DirectorNeedsComment directorNeedsComment;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DirectorNeedsCommentResource directorNeedsCommentResource = new DirectorNeedsCommentResource();
        ReflectionTestUtils.setField(directorNeedsCommentResource, "directorNeedsCommentService", directorNeedsCommentService);
        this.restDirectorNeedsCommentMockMvc = MockMvcBuilders.standaloneSetup(directorNeedsCommentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DirectorNeedsComment createEntity(EntityManager em) {
        DirectorNeedsComment directorNeedsComment = new DirectorNeedsComment()
                .content(DEFAULT_CONTENT)
                .createTime(DEFAULT_CREATE_TIME);
        return directorNeedsComment;
    }

    @Before
    public void initTest() {
        directorNeedsComment = createEntity(em);
    }

    @Test
    @Transactional
    public void createDirectorNeedsComment() throws Exception {
        int databaseSizeBeforeCreate = directorNeedsCommentRepository.findAll().size();

        // Create the DirectorNeedsComment
        DirectorNeedsCommentDTO directorNeedsCommentDTO = directorNeedsCommentMapper.directorNeedsCommentToDirectorNeedsCommentDTO(directorNeedsComment);

        restDirectorNeedsCommentMockMvc.perform(post("/api/director-needs-comments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(directorNeedsCommentDTO)))
                .andExpect(status().isCreated());

        // Validate the DirectorNeedsComment in the database
        List<DirectorNeedsComment> directorNeedsComments = directorNeedsCommentRepository.findAll();
        assertThat(directorNeedsComments).hasSize(databaseSizeBeforeCreate + 1);
        DirectorNeedsComment testDirectorNeedsComment = directorNeedsComments.get(directorNeedsComments.size() - 1);
        assertThat(testDirectorNeedsComment.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testDirectorNeedsComment.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
    }

    @Test
    @Transactional
    public void getAllDirectorNeedsComments() throws Exception {
        // Initialize the database
        directorNeedsCommentRepository.saveAndFlush(directorNeedsComment);

        // Get all the directorNeedsComments
        restDirectorNeedsCommentMockMvc.perform(get("/api/director-needs-comments?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(directorNeedsComment.getId().intValue())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
                .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.intValue())));
    }

    @Test
    @Transactional
    public void getDirectorNeedsComment() throws Exception {
        // Initialize the database
        directorNeedsCommentRepository.saveAndFlush(directorNeedsComment);

        // Get the directorNeedsComment
        restDirectorNeedsCommentMockMvc.perform(get("/api/director-needs-comments/{id}", directorNeedsComment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(directorNeedsComment.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDirectorNeedsComment() throws Exception {
        // Get the directorNeedsComment
        restDirectorNeedsCommentMockMvc.perform(get("/api/director-needs-comments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDirectorNeedsComment() throws Exception {
        // Initialize the database
        directorNeedsCommentRepository.saveAndFlush(directorNeedsComment);
        int databaseSizeBeforeUpdate = directorNeedsCommentRepository.findAll().size();

        // Update the directorNeedsComment
        DirectorNeedsComment updatedDirectorNeedsComment = directorNeedsCommentRepository.findOne(directorNeedsComment.getId());
        updatedDirectorNeedsComment
                .content(UPDATED_CONTENT)
                .createTime(UPDATED_CREATE_TIME);
        DirectorNeedsCommentDTO directorNeedsCommentDTO = directorNeedsCommentMapper.directorNeedsCommentToDirectorNeedsCommentDTO(updatedDirectorNeedsComment);

        restDirectorNeedsCommentMockMvc.perform(put("/api/director-needs-comments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(directorNeedsCommentDTO)))
                .andExpect(status().isOk());

        // Validate the DirectorNeedsComment in the database
        List<DirectorNeedsComment> directorNeedsComments = directorNeedsCommentRepository.findAll();
        assertThat(directorNeedsComments).hasSize(databaseSizeBeforeUpdate);
        DirectorNeedsComment testDirectorNeedsComment = directorNeedsComments.get(directorNeedsComments.size() - 1);
        assertThat(testDirectorNeedsComment.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testDirectorNeedsComment.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    public void deleteDirectorNeedsComment() throws Exception {
        // Initialize the database
        directorNeedsCommentRepository.saveAndFlush(directorNeedsComment);
        int databaseSizeBeforeDelete = directorNeedsCommentRepository.findAll().size();

        // Get the directorNeedsComment
        restDirectorNeedsCommentMockMvc.perform(delete("/api/director-needs-comments/{id}", directorNeedsComment.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<DirectorNeedsComment> directorNeedsComments = directorNeedsCommentRepository.findAll();
        assertThat(directorNeedsComments).hasSize(databaseSizeBeforeDelete - 1);
    }
}
