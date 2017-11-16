package com.yijia.yy.web.rest;

import com.yijia.yy.YiyingOaApp;

import com.yijia.yy.domain.Client;
import com.yijia.yy.repository.ClientRepository;
import com.yijia.yy.service.ClientService;
import com.yijia.yy.service.dto.ClientDTO;
import com.yijia.yy.service.mapper.ClientMapper;

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
 * Test class for the ClientResource REST controller.
 *
 * @see ClientResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YiyingOaApp.class)
public class ClientResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_CONTACT = "AAAAA";
    private static final String UPDATED_CONTACT = "BBBBB";
    private static final String DEFAULT_CONTACT_TEL = "AAAAA";
    private static final String UPDATED_CONTACT_TEL = "BBBBB";

    private static final Long DEFAULT_CREATE_TIME = 1L;
    private static final Long UPDATED_CREATE_TIME = 2L;
    private static final String DEFAULT_TEL_CORP = "AAAAA";
    private static final String UPDATED_TEL_CORP = "BBBBB";
    private static final String DEFAULT_WEBSITE_CORP = "AAAAA";
    private static final String UPDATED_WEBSITE_CORP = "BBBBB";
    private static final String DEFAULT_ADDRESS_CORP = "AAAAA";
    private static final String UPDATED_ADDRESS_CORP = "BBBBB";
    private static final String DEFAULT_QQ = "AAAAA";
    private static final String UPDATED_QQ = "BBBBB";
    private static final String DEFAULT_WECHAT = "AAAAA";
    private static final String UPDATED_WECHAT = "BBBBB";
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";
    private static final String DEFAULT_OWNER = "AAAAA";
    private static final String UPDATED_OWNER = "BBBBB";
    private static final String DEFAULT_CONTACT_JOB_TITLE = "AAAAA";
    private static final String UPDATED_CONTACT_JOB_TITLE = "BBBBB";
    private static final String DEFAULT_CONTACT_BIRTH_DATE = "AAAAA";
    private static final String UPDATED_CONTACT_BIRTH_DATE = "BBBBB";
    private static final String DEFAULT_CONTACT_HOBBY = "AAAAA";
    private static final String UPDATED_CONTACT_HOBBY = "BBBBB";
    private static final String DEFAULT_COMMENT = "AAAAA";
    private static final String UPDATED_COMMENT = "BBBBB";

    private static final Long DEFAULT_LAST_MODIFIED_TIME = 1L;
    private static final Long UPDATED_LAST_MODIFIED_TIME = 2L;
    private static final String DEFAULT_INPUT_OPERATOR = "AAAAA";
    private static final String UPDATED_INPUT_OPERATOR = "BBBBB";
    private static final String DEFAULT_LAST_MODIFIER = "AAAAA";
    private static final String UPDATED_LAST_MODIFIER = "BBBBB";

    @Inject
    private ClientRepository clientRepository;

    @Inject
    private ClientMapper clientMapper;

    @Inject
    private ClientService clientService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restClientMockMvc;

    private Client client;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClientResource clientResource = new ClientResource();
        ReflectionTestUtils.setField(clientResource, "clientService", clientService);
        this.restClientMockMvc = MockMvcBuilders.standaloneSetup(clientResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Client createEntity(EntityManager em) {
        Client client = new Client()
                .name(DEFAULT_NAME)
                .contact(DEFAULT_CONTACT)
                .contactTel(DEFAULT_CONTACT_TEL)
                .createTime(DEFAULT_CREATE_TIME)
                .telCorp(DEFAULT_TEL_CORP)
                .websiteCorp(DEFAULT_WEBSITE_CORP)
                .addressCorp(DEFAULT_ADDRESS_CORP)
                .qq(DEFAULT_QQ)
                .wechat(DEFAULT_WECHAT)
                .email(DEFAULT_EMAIL)
                .owner(DEFAULT_OWNER)
                .contactJobTitle(DEFAULT_CONTACT_JOB_TITLE)
                .contactBirthDate(DEFAULT_CONTACT_BIRTH_DATE)
                .contactHobby(DEFAULT_CONTACT_HOBBY)
                .comment(DEFAULT_COMMENT)
                .lastModifiedTime(DEFAULT_LAST_MODIFIED_TIME)
                .inputOperator(DEFAULT_INPUT_OPERATOR)
                .lastModifier(DEFAULT_LAST_MODIFIER);
        return client;
    }

    @Before
    public void initTest() {
        client = createEntity(em);
    }

    @Test
    @Transactional
    public void createClient() throws Exception {
        int databaseSizeBeforeCreate = clientRepository.findAll().size();

        // Create the Client
        ClientDTO clientDTO = clientMapper.clientToClientDTO(client);

        restClientMockMvc.perform(post("/api/clients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(clientDTO)))
                .andExpect(status().isCreated());

        // Validate the Client in the database
        List<Client> clients = clientRepository.findAll();
        assertThat(clients).hasSize(databaseSizeBeforeCreate + 1);
        Client testClient = clients.get(clients.size() - 1);
        assertThat(testClient.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testClient.getContact()).isEqualTo(DEFAULT_CONTACT);
        assertThat(testClient.getContactTel()).isEqualTo(DEFAULT_CONTACT_TEL);
        assertThat(testClient.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testClient.getTelCorp()).isEqualTo(DEFAULT_TEL_CORP);
        assertThat(testClient.getWebsiteCorp()).isEqualTo(DEFAULT_WEBSITE_CORP);
        assertThat(testClient.getAddressCorp()).isEqualTo(DEFAULT_ADDRESS_CORP);
        assertThat(testClient.getQq()).isEqualTo(DEFAULT_QQ);
        assertThat(testClient.getWechat()).isEqualTo(DEFAULT_WECHAT);
        assertThat(testClient.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testClient.getOwner()).isEqualTo(DEFAULT_OWNER);
        assertThat(testClient.getContactJobTitle()).isEqualTo(DEFAULT_CONTACT_JOB_TITLE);
        assertThat(testClient.getContactBirthDate()).isEqualTo(DEFAULT_CONTACT_BIRTH_DATE);
        assertThat(testClient.getContactHobby()).isEqualTo(DEFAULT_CONTACT_HOBBY);
        assertThat(testClient.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testClient.getLastModifiedTime()).isEqualTo(DEFAULT_LAST_MODIFIED_TIME);
        assertThat(testClient.getInputOperator()).isEqualTo(DEFAULT_INPUT_OPERATOR);
        assertThat(testClient.getLastModifier()).isEqualTo(DEFAULT_LAST_MODIFIER);
    }

    @Test
    @Transactional
    public void getAllClients() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clients
        restClientMockMvc.perform(get("/api/clients?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT.toString())))
                .andExpect(jsonPath("$.[*].contactTel").value(hasItem(DEFAULT_CONTACT_TEL.toString())))
                .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.intValue())))
                .andExpect(jsonPath("$.[*].telCorp").value(hasItem(DEFAULT_TEL_CORP.toString())))
                .andExpect(jsonPath("$.[*].websiteCorp").value(hasItem(DEFAULT_WEBSITE_CORP.toString())))
                .andExpect(jsonPath("$.[*].addressCorp").value(hasItem(DEFAULT_ADDRESS_CORP.toString())))
                .andExpect(jsonPath("$.[*].qq").value(hasItem(DEFAULT_QQ.toString())))
                .andExpect(jsonPath("$.[*].wechat").value(hasItem(DEFAULT_WECHAT.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER.toString())))
                .andExpect(jsonPath("$.[*].contactJobTitle").value(hasItem(DEFAULT_CONTACT_JOB_TITLE.toString())))
                .andExpect(jsonPath("$.[*].contactBirthDate").value(hasItem(DEFAULT_CONTACT_BIRTH_DATE.toString())))
                .andExpect(jsonPath("$.[*].contactHobby").value(hasItem(DEFAULT_CONTACT_HOBBY.toString())))
                .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
                .andExpect(jsonPath("$.[*].lastModifiedTime").value(hasItem(DEFAULT_LAST_MODIFIED_TIME.intValue())))
                .andExpect(jsonPath("$.[*].inputOperator").value(hasItem(DEFAULT_INPUT_OPERATOR.toString())))
                .andExpect(jsonPath("$.[*].lastModifier").value(hasItem(DEFAULT_LAST_MODIFIER.toString())));
    }

    @Test
    @Transactional
    public void getClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get the client
        restClientMockMvc.perform(get("/api/clients/{id}", client.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(client.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT.toString()))
            .andExpect(jsonPath("$.contactTel").value(DEFAULT_CONTACT_TEL.toString()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.intValue()))
            .andExpect(jsonPath("$.telCorp").value(DEFAULT_TEL_CORP.toString()))
            .andExpect(jsonPath("$.websiteCorp").value(DEFAULT_WEBSITE_CORP.toString()))
            .andExpect(jsonPath("$.addressCorp").value(DEFAULT_ADDRESS_CORP.toString()))
            .andExpect(jsonPath("$.qq").value(DEFAULT_QQ.toString()))
            .andExpect(jsonPath("$.wechat").value(DEFAULT_WECHAT.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.owner").value(DEFAULT_OWNER.toString()))
            .andExpect(jsonPath("$.contactJobTitle").value(DEFAULT_CONTACT_JOB_TITLE.toString()))
            .andExpect(jsonPath("$.contactBirthDate").value(DEFAULT_CONTACT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.contactHobby").value(DEFAULT_CONTACT_HOBBY.toString()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.lastModifiedTime").value(DEFAULT_LAST_MODIFIED_TIME.intValue()))
            .andExpect(jsonPath("$.inputOperator").value(DEFAULT_INPUT_OPERATOR.toString()))
            .andExpect(jsonPath("$.lastModifier").value(DEFAULT_LAST_MODIFIER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingClient() throws Exception {
        // Get the client
        restClientMockMvc.perform(get("/api/clients/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();

        // Update the client
        Client updatedClient = clientRepository.findOne(client.getId());
        updatedClient
                .name(UPDATED_NAME)
                .contact(UPDATED_CONTACT)
                .contactTel(UPDATED_CONTACT_TEL)
                .createTime(UPDATED_CREATE_TIME)
                .telCorp(UPDATED_TEL_CORP)
                .websiteCorp(UPDATED_WEBSITE_CORP)
                .addressCorp(UPDATED_ADDRESS_CORP)
                .qq(UPDATED_QQ)
                .wechat(UPDATED_WECHAT)
                .email(UPDATED_EMAIL)
                .owner(UPDATED_OWNER)
                .contactJobTitle(UPDATED_CONTACT_JOB_TITLE)
                .contactBirthDate(UPDATED_CONTACT_BIRTH_DATE)
                .contactHobby(UPDATED_CONTACT_HOBBY)
                .comment(UPDATED_COMMENT)
                .lastModifiedTime(UPDATED_LAST_MODIFIED_TIME)
                .inputOperator(UPDATED_INPUT_OPERATOR)
                .lastModifier(UPDATED_LAST_MODIFIER);
        ClientDTO clientDTO = clientMapper.clientToClientDTO(updatedClient);

        restClientMockMvc.perform(put("/api/clients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(clientDTO)))
                .andExpect(status().isOk());

        // Validate the Client in the database
        List<Client> clients = clientRepository.findAll();
        assertThat(clients).hasSize(databaseSizeBeforeUpdate);
        Client testClient = clients.get(clients.size() - 1);
        assertThat(testClient.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testClient.getContact()).isEqualTo(UPDATED_CONTACT);
        assertThat(testClient.getContactTel()).isEqualTo(UPDATED_CONTACT_TEL);
        assertThat(testClient.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testClient.getTelCorp()).isEqualTo(UPDATED_TEL_CORP);
        assertThat(testClient.getWebsiteCorp()).isEqualTo(UPDATED_WEBSITE_CORP);
        assertThat(testClient.getAddressCorp()).isEqualTo(UPDATED_ADDRESS_CORP);
        assertThat(testClient.getQq()).isEqualTo(UPDATED_QQ);
        assertThat(testClient.getWechat()).isEqualTo(UPDATED_WECHAT);
        assertThat(testClient.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testClient.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testClient.getContactJobTitle()).isEqualTo(UPDATED_CONTACT_JOB_TITLE);
        assertThat(testClient.getContactBirthDate()).isEqualTo(UPDATED_CONTACT_BIRTH_DATE);
        assertThat(testClient.getContactHobby()).isEqualTo(UPDATED_CONTACT_HOBBY);
        assertThat(testClient.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testClient.getLastModifiedTime()).isEqualTo(UPDATED_LAST_MODIFIED_TIME);
        assertThat(testClient.getInputOperator()).isEqualTo(UPDATED_INPUT_OPERATOR);
        assertThat(testClient.getLastModifier()).isEqualTo(UPDATED_LAST_MODIFIER);
    }

    @Test
    @Transactional
    public void deleteClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);
        int databaseSizeBeforeDelete = clientRepository.findAll().size();

        // Get the client
        restClientMockMvc.perform(delete("/api/clients/{id}", client.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Client> clients = clientRepository.findAll();
        assertThat(clients).hasSize(databaseSizeBeforeDelete - 1);
    }
}
