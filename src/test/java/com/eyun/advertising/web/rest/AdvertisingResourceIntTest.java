package com.eyun.advertising.web.rest;

import com.eyun.advertising.AdvertisingApp;

import com.eyun.advertising.config.SecurityBeanOverrideConfiguration;

import com.eyun.advertising.domain.Advertising;
import com.eyun.advertising.domain.Post;
import com.eyun.advertising.repository.AdvertisingRepository;
import com.eyun.advertising.service.AdvertisingService;
import com.eyun.advertising.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.eyun.advertising.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AdvertisingResource REST controller.
 *
 * @see AdvertisingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AdvertisingApp.class, SecurityBeanOverrideConfiguration.class})
public class AdvertisingResourceIntTest {

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final String DEFAULT_LINK = "AAAAAAAAAA";
    private static final String UPDATED_LINK = "BBBBBBBBBB";

    private static final String DEFAULT_ALT = "AAAAAAAAAA";
    private static final String UPDATED_ALT = "BBBBBBBBBB";

    private static final String DEFAULT_EXTEND = "AAAAAAAAAA";
    private static final String UPDATED_EXTEND = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_VERSION = 1;
    private static final Integer UPDATED_VERSION = 2;

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    @Autowired
    private AdvertisingRepository advertisingRepository;

    @Autowired
    private AdvertisingService advertisingService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAdvertisingMockMvc;

    private Advertising advertising;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AdvertisingResource advertisingResource = new AdvertisingResource(advertisingService);
        this.restAdvertisingMockMvc = MockMvcBuilders.standaloneSetup(advertisingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Advertising createEntity(EntityManager em) {
        Advertising advertising = new Advertising()
            .image(DEFAULT_IMAGE)
            .link(DEFAULT_LINK)
            .alt(DEFAULT_ALT)
            .extend(DEFAULT_EXTEND)
            .created_time(DEFAULT_CREATED_TIME)
            .modified_time(DEFAULT_MODIFIED_TIME)
            .version(DEFAULT_VERSION)
            .deleted(DEFAULT_DELETED);
        // Add required entity
        Post post = PostResourceIntTest.createEntity(em);
        em.persist(post);
        em.flush();
        advertising.setPost(post);
        return advertising;
    }

    @Before
    public void initTest() {
        advertising = createEntity(em);
    }

    @Test
    @Transactional
    public void createAdvertising() throws Exception {
        int databaseSizeBeforeCreate = advertisingRepository.findAll().size();

        // Create the Advertising
        restAdvertisingMockMvc.perform(post("/api/advertisings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(advertising)))
            .andExpect(status().isCreated());

        // Validate the Advertising in the database
        List<Advertising> advertisingList = advertisingRepository.findAll();
        assertThat(advertisingList).hasSize(databaseSizeBeforeCreate + 1);
        Advertising testAdvertising = advertisingList.get(advertisingList.size() - 1);
        assertThat(testAdvertising.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testAdvertising.getLink()).isEqualTo(DEFAULT_LINK);
        assertThat(testAdvertising.getAlt()).isEqualTo(DEFAULT_ALT);
        assertThat(testAdvertising.getExtend()).isEqualTo(DEFAULT_EXTEND);
        assertThat(testAdvertising.getCreated_time()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testAdvertising.getModified_time()).isEqualTo(DEFAULT_MODIFIED_TIME);
        assertThat(testAdvertising.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testAdvertising.isDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    public void createAdvertisingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = advertisingRepository.findAll().size();

        // Create the Advertising with an existing ID
        advertising.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdvertisingMockMvc.perform(post("/api/advertisings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(advertising)))
            .andExpect(status().isBadRequest());

        // Validate the Advertising in the database
        List<Advertising> advertisingList = advertisingRepository.findAll();
        assertThat(advertisingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAdvertisings() throws Exception {
        // Initialize the database
        advertisingRepository.saveAndFlush(advertising);

        // Get all the advertisingList
        restAdvertisingMockMvc.perform(get("/api/advertisings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(advertising.getId().intValue())))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK.toString())))
            .andExpect(jsonPath("$.[*].alt").value(hasItem(DEFAULT_ALT.toString())))
            .andExpect(jsonPath("$.[*].extend").value(hasItem(DEFAULT_EXTEND.toString())))
            .andExpect(jsonPath("$.[*].created_time").value(hasItem(DEFAULT_CREATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].modified_time").value(hasItem(DEFAULT_MODIFIED_TIME.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    public void getAdvertising() throws Exception {
        // Initialize the database
        advertisingRepository.saveAndFlush(advertising);

        // Get the advertising
        restAdvertisingMockMvc.perform(get("/api/advertisings/{id}", advertising.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(advertising.getId().intValue()))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE.toString()))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK.toString()))
            .andExpect(jsonPath("$.alt").value(DEFAULT_ALT.toString()))
            .andExpect(jsonPath("$.extend").value(DEFAULT_EXTEND.toString()))
            .andExpect(jsonPath("$.created_time").value(DEFAULT_CREATED_TIME.toString()))
            .andExpect(jsonPath("$.modified_time").value(DEFAULT_MODIFIED_TIME.toString()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAdvertising() throws Exception {
        // Get the advertising
        restAdvertisingMockMvc.perform(get("/api/advertisings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAdvertising() throws Exception {
        // Initialize the database
        advertisingService.save(advertising);

        int databaseSizeBeforeUpdate = advertisingRepository.findAll().size();

        // Update the advertising
        Advertising updatedAdvertising = advertisingRepository.findOne(advertising.getId());
        // Disconnect from session so that the updates on updatedAdvertising are not directly saved in db
        em.detach(updatedAdvertising);
        updatedAdvertising
            .image(UPDATED_IMAGE)
            .link(UPDATED_LINK)
            .alt(UPDATED_ALT)
            .extend(UPDATED_EXTEND)
            .created_time(UPDATED_CREATED_TIME)
            .modified_time(UPDATED_MODIFIED_TIME)
            .version(UPDATED_VERSION)
            .deleted(UPDATED_DELETED);

        restAdvertisingMockMvc.perform(put("/api/advertisings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAdvertising)))
            .andExpect(status().isOk());

        // Validate the Advertising in the database
        List<Advertising> advertisingList = advertisingRepository.findAll();
        assertThat(advertisingList).hasSize(databaseSizeBeforeUpdate);
        Advertising testAdvertising = advertisingList.get(advertisingList.size() - 1);
        assertThat(testAdvertising.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testAdvertising.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testAdvertising.getAlt()).isEqualTo(UPDATED_ALT);
        assertThat(testAdvertising.getExtend()).isEqualTo(UPDATED_EXTEND);
        assertThat(testAdvertising.getCreated_time()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testAdvertising.getModified_time()).isEqualTo(UPDATED_MODIFIED_TIME);
        assertThat(testAdvertising.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testAdvertising.isDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void updateNonExistingAdvertising() throws Exception {
        int databaseSizeBeforeUpdate = advertisingRepository.findAll().size();

        // Create the Advertising

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAdvertisingMockMvc.perform(put("/api/advertisings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(advertising)))
            .andExpect(status().isCreated());

        // Validate the Advertising in the database
        List<Advertising> advertisingList = advertisingRepository.findAll();
        assertThat(advertisingList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAdvertising() throws Exception {
        // Initialize the database
        advertisingService.save(advertising);

        int databaseSizeBeforeDelete = advertisingRepository.findAll().size();

        // Get the advertising
        restAdvertisingMockMvc.perform(delete("/api/advertisings/{id}", advertising.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Advertising> advertisingList = advertisingRepository.findAll();
        assertThat(advertisingList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Advertising.class);
        Advertising advertising1 = new Advertising();
        advertising1.setId(1L);
        Advertising advertising2 = new Advertising();
        advertising2.setId(advertising1.getId());
        assertThat(advertising1).isEqualTo(advertising2);
        advertising2.setId(2L);
        assertThat(advertising1).isNotEqualTo(advertising2);
        advertising1.setId(null);
        assertThat(advertising1).isNotEqualTo(advertising2);
    }
}
