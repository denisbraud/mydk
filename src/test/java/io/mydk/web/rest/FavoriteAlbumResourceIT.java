package io.mydk.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.mydk.IntegrationTest;
import io.mydk.domain.FavoriteAlbum;
import io.mydk.repository.FavoriteAlbumRepository;
import io.mydk.service.dto.FavoriteAlbumDTO;
import io.mydk.service.mapper.FavoriteAlbumMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FavoriteAlbumResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FavoriteAlbumResourceIT {

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_ALBUM_SPOTIFY_ID = "5vBZRYu2GLA65nfxBvG1a7";
    private static final String UPDATED_ALBUM_SPOTIFY_ID = "1YZWEKi3WAzSk6IGnObgUO";

    private static final Integer DEFAULT_RANK = 1;
    private static final Integer UPDATED_RANK = 2;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/favorite-albums";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FavoriteAlbumRepository favoriteAlbumRepository;

    @Autowired
    private FavoriteAlbumMapper favoriteAlbumMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFavoriteAlbumMockMvc;

    private FavoriteAlbum favoriteAlbum;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FavoriteAlbum createEntity(EntityManager em) {
        FavoriteAlbum favoriteAlbum = new FavoriteAlbum()
            .login(DEFAULT_LOGIN)
            .albumSpotifyId(DEFAULT_ALBUM_SPOTIFY_ID)
            .rank(DEFAULT_RANK)
            .comment(DEFAULT_COMMENT);
        return favoriteAlbum;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FavoriteAlbum createUpdatedEntity(EntityManager em) {
        FavoriteAlbum favoriteAlbum = new FavoriteAlbum()
            .login(UPDATED_LOGIN)
            .albumSpotifyId(UPDATED_ALBUM_SPOTIFY_ID)
            .rank(UPDATED_RANK)
            .comment(UPDATED_COMMENT);
        return favoriteAlbum;
    }

    @BeforeEach
    public void initTest() {
        favoriteAlbum = createEntity(em);
    }

    @Test
    @Transactional
    void createFavoriteAlbum() throws Exception {
        int databaseSizeBeforeCreate = favoriteAlbumRepository.findAll().size();
        // Create the FavoriteAlbum
        FavoriteAlbumDTO favoriteAlbumDTO = favoriteAlbumMapper.toDto(favoriteAlbum);
        restFavoriteAlbumMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(favoriteAlbumDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FavoriteAlbum in the database
        List<FavoriteAlbum> favoriteAlbumList = favoriteAlbumRepository.findAll();
        assertThat(favoriteAlbumList).hasSize(databaseSizeBeforeCreate + 1);
        FavoriteAlbum testFavoriteAlbum = favoriteAlbumList.get(favoriteAlbumList.size() - 1);
        assertThat(testFavoriteAlbum.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testFavoriteAlbum.getAlbumSpotifyId()).isEqualTo(DEFAULT_ALBUM_SPOTIFY_ID);
        assertThat(testFavoriteAlbum.getRank()).isEqualTo(DEFAULT_RANK);
        assertThat(testFavoriteAlbum.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    void createFavoriteAlbumWithExistingId() throws Exception {
        // Create the FavoriteAlbum with an existing ID
        favoriteAlbum.setId(1L);
        FavoriteAlbumDTO favoriteAlbumDTO = favoriteAlbumMapper.toDto(favoriteAlbum);

        int databaseSizeBeforeCreate = favoriteAlbumRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFavoriteAlbumMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(favoriteAlbumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FavoriteAlbum in the database
        List<FavoriteAlbum> favoriteAlbumList = favoriteAlbumRepository.findAll();
        assertThat(favoriteAlbumList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLoginIsRequired() throws Exception {
        int databaseSizeBeforeTest = favoriteAlbumRepository.findAll().size();
        // set the field null
        favoriteAlbum.setLogin(null);

        // Create the FavoriteAlbum, which fails.
        FavoriteAlbumDTO favoriteAlbumDTO = favoriteAlbumMapper.toDto(favoriteAlbum);

        restFavoriteAlbumMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(favoriteAlbumDTO))
            )
            .andExpect(status().isBadRequest());

        List<FavoriteAlbum> favoriteAlbumList = favoriteAlbumRepository.findAll();
        assertThat(favoriteAlbumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAlbumSpotifyIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = favoriteAlbumRepository.findAll().size();
        // set the field null
        favoriteAlbum.setAlbumSpotifyId(null);

        // Create the FavoriteAlbum, which fails.
        FavoriteAlbumDTO favoriteAlbumDTO = favoriteAlbumMapper.toDto(favoriteAlbum);

        restFavoriteAlbumMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(favoriteAlbumDTO))
            )
            .andExpect(status().isBadRequest());

        List<FavoriteAlbum> favoriteAlbumList = favoriteAlbumRepository.findAll();
        assertThat(favoriteAlbumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRankIsRequired() throws Exception {
        int databaseSizeBeforeTest = favoriteAlbumRepository.findAll().size();
        // set the field null
        favoriteAlbum.setRank(null);

        // Create the FavoriteAlbum, which fails.
        FavoriteAlbumDTO favoriteAlbumDTO = favoriteAlbumMapper.toDto(favoriteAlbum);

        restFavoriteAlbumMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(favoriteAlbumDTO))
            )
            .andExpect(status().isBadRequest());

        List<FavoriteAlbum> favoriteAlbumList = favoriteAlbumRepository.findAll();
        assertThat(favoriteAlbumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFavoriteAlbums() throws Exception {
        // Initialize the database
        favoriteAlbumRepository.saveAndFlush(favoriteAlbum);

        // Get all the favoriteAlbumList
        restFavoriteAlbumMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(favoriteAlbum.getId().intValue())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].albumSpotifyId").value(hasItem(DEFAULT_ALBUM_SPOTIFY_ID)))
            .andExpect(jsonPath("$.[*].rank").value(hasItem(DEFAULT_RANK)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)));
    }

    @Test
    @Transactional
    void getFavoriteAlbum() throws Exception {
        // Initialize the database
        favoriteAlbumRepository.saveAndFlush(favoriteAlbum);

        // Get the favoriteAlbum
        restFavoriteAlbumMockMvc
            .perform(get(ENTITY_API_URL_ID, favoriteAlbum.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(favoriteAlbum.getId().intValue()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN))
            .andExpect(jsonPath("$.albumSpotifyId").value(DEFAULT_ALBUM_SPOTIFY_ID))
            .andExpect(jsonPath("$.rank").value(DEFAULT_RANK))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT));
    }

    @Test
    @Transactional
    void getNonExistingFavoriteAlbum() throws Exception {
        // Get the favoriteAlbum
        restFavoriteAlbumMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFavoriteAlbum() throws Exception {
        // Initialize the database
        favoriteAlbumRepository.saveAndFlush(favoriteAlbum);

        int databaseSizeBeforeUpdate = favoriteAlbumRepository.findAll().size();

        // Update the favoriteAlbum
        FavoriteAlbum updatedFavoriteAlbum = favoriteAlbumRepository.findById(favoriteAlbum.getId()).get();
        // Disconnect from session so that the updates on updatedFavoriteAlbum are not directly saved in db
        em.detach(updatedFavoriteAlbum);
        updatedFavoriteAlbum.login(UPDATED_LOGIN).albumSpotifyId(UPDATED_ALBUM_SPOTIFY_ID).rank(UPDATED_RANK).comment(UPDATED_COMMENT);
        FavoriteAlbumDTO favoriteAlbumDTO = favoriteAlbumMapper.toDto(updatedFavoriteAlbum);

        restFavoriteAlbumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, favoriteAlbumDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(favoriteAlbumDTO))
            )
            .andExpect(status().isOk());

        // Validate the FavoriteAlbum in the database
        List<FavoriteAlbum> favoriteAlbumList = favoriteAlbumRepository.findAll();
        assertThat(favoriteAlbumList).hasSize(databaseSizeBeforeUpdate);
        FavoriteAlbum testFavoriteAlbum = favoriteAlbumList.get(favoriteAlbumList.size() - 1);
        assertThat(testFavoriteAlbum.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testFavoriteAlbum.getAlbumSpotifyId()).isEqualTo(UPDATED_ALBUM_SPOTIFY_ID);
        assertThat(testFavoriteAlbum.getRank()).isEqualTo(UPDATED_RANK);
        assertThat(testFavoriteAlbum.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void putNonExistingFavoriteAlbum() throws Exception {
        int databaseSizeBeforeUpdate = favoriteAlbumRepository.findAll().size();
        favoriteAlbum.setId(count.incrementAndGet());

        // Create the FavoriteAlbum
        FavoriteAlbumDTO favoriteAlbumDTO = favoriteAlbumMapper.toDto(favoriteAlbum);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFavoriteAlbumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, favoriteAlbumDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(favoriteAlbumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FavoriteAlbum in the database
        List<FavoriteAlbum> favoriteAlbumList = favoriteAlbumRepository.findAll();
        assertThat(favoriteAlbumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFavoriteAlbum() throws Exception {
        int databaseSizeBeforeUpdate = favoriteAlbumRepository.findAll().size();
        favoriteAlbum.setId(count.incrementAndGet());

        // Create the FavoriteAlbum
        FavoriteAlbumDTO favoriteAlbumDTO = favoriteAlbumMapper.toDto(favoriteAlbum);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFavoriteAlbumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(favoriteAlbumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FavoriteAlbum in the database
        List<FavoriteAlbum> favoriteAlbumList = favoriteAlbumRepository.findAll();
        assertThat(favoriteAlbumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFavoriteAlbum() throws Exception {
        int databaseSizeBeforeUpdate = favoriteAlbumRepository.findAll().size();
        favoriteAlbum.setId(count.incrementAndGet());

        // Create the FavoriteAlbum
        FavoriteAlbumDTO favoriteAlbumDTO = favoriteAlbumMapper.toDto(favoriteAlbum);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFavoriteAlbumMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(favoriteAlbumDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FavoriteAlbum in the database
        List<FavoriteAlbum> favoriteAlbumList = favoriteAlbumRepository.findAll();
        assertThat(favoriteAlbumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFavoriteAlbumWithPatch() throws Exception {
        // Initialize the database
        favoriteAlbumRepository.saveAndFlush(favoriteAlbum);

        int databaseSizeBeforeUpdate = favoriteAlbumRepository.findAll().size();

        // Update the favoriteAlbum using partial update
        FavoriteAlbum partialUpdatedFavoriteAlbum = new FavoriteAlbum();
        partialUpdatedFavoriteAlbum.setId(favoriteAlbum.getId());

        partialUpdatedFavoriteAlbum.login(UPDATED_LOGIN);

        restFavoriteAlbumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFavoriteAlbum.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFavoriteAlbum))
            )
            .andExpect(status().isOk());

        // Validate the FavoriteAlbum in the database
        List<FavoriteAlbum> favoriteAlbumList = favoriteAlbumRepository.findAll();
        assertThat(favoriteAlbumList).hasSize(databaseSizeBeforeUpdate);
        FavoriteAlbum testFavoriteAlbum = favoriteAlbumList.get(favoriteAlbumList.size() - 1);
        assertThat(testFavoriteAlbum.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testFavoriteAlbum.getAlbumSpotifyId()).isEqualTo(DEFAULT_ALBUM_SPOTIFY_ID);
        assertThat(testFavoriteAlbum.getRank()).isEqualTo(DEFAULT_RANK);
        assertThat(testFavoriteAlbum.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    void fullUpdateFavoriteAlbumWithPatch() throws Exception {
        // Initialize the database
        favoriteAlbumRepository.saveAndFlush(favoriteAlbum);

        int databaseSizeBeforeUpdate = favoriteAlbumRepository.findAll().size();

        // Update the favoriteAlbum using partial update
        FavoriteAlbum partialUpdatedFavoriteAlbum = new FavoriteAlbum();
        partialUpdatedFavoriteAlbum.setId(favoriteAlbum.getId());

        partialUpdatedFavoriteAlbum
            .login(UPDATED_LOGIN)
            .albumSpotifyId(UPDATED_ALBUM_SPOTIFY_ID)
            .rank(UPDATED_RANK)
            .comment(UPDATED_COMMENT);

        restFavoriteAlbumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFavoriteAlbum.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFavoriteAlbum))
            )
            .andExpect(status().isOk());

        // Validate the FavoriteAlbum in the database
        List<FavoriteAlbum> favoriteAlbumList = favoriteAlbumRepository.findAll();
        assertThat(favoriteAlbumList).hasSize(databaseSizeBeforeUpdate);
        FavoriteAlbum testFavoriteAlbum = favoriteAlbumList.get(favoriteAlbumList.size() - 1);
        assertThat(testFavoriteAlbum.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testFavoriteAlbum.getAlbumSpotifyId()).isEqualTo(UPDATED_ALBUM_SPOTIFY_ID);
        assertThat(testFavoriteAlbum.getRank()).isEqualTo(UPDATED_RANK);
        assertThat(testFavoriteAlbum.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    void patchNonExistingFavoriteAlbum() throws Exception {
        int databaseSizeBeforeUpdate = favoriteAlbumRepository.findAll().size();
        favoriteAlbum.setId(count.incrementAndGet());

        // Create the FavoriteAlbum
        FavoriteAlbumDTO favoriteAlbumDTO = favoriteAlbumMapper.toDto(favoriteAlbum);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFavoriteAlbumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, favoriteAlbumDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(favoriteAlbumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FavoriteAlbum in the database
        List<FavoriteAlbum> favoriteAlbumList = favoriteAlbumRepository.findAll();
        assertThat(favoriteAlbumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFavoriteAlbum() throws Exception {
        int databaseSizeBeforeUpdate = favoriteAlbumRepository.findAll().size();
        favoriteAlbum.setId(count.incrementAndGet());

        // Create the FavoriteAlbum
        FavoriteAlbumDTO favoriteAlbumDTO = favoriteAlbumMapper.toDto(favoriteAlbum);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFavoriteAlbumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(favoriteAlbumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FavoriteAlbum in the database
        List<FavoriteAlbum> favoriteAlbumList = favoriteAlbumRepository.findAll();
        assertThat(favoriteAlbumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFavoriteAlbum() throws Exception {
        int databaseSizeBeforeUpdate = favoriteAlbumRepository.findAll().size();
        favoriteAlbum.setId(count.incrementAndGet());

        // Create the FavoriteAlbum
        FavoriteAlbumDTO favoriteAlbumDTO = favoriteAlbumMapper.toDto(favoriteAlbum);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFavoriteAlbumMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(favoriteAlbumDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FavoriteAlbum in the database
        List<FavoriteAlbum> favoriteAlbumList = favoriteAlbumRepository.findAll();
        assertThat(favoriteAlbumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFavoriteAlbum() throws Exception {
        // Initialize the database
        favoriteAlbumRepository.saveAndFlush(favoriteAlbum);

        int databaseSizeBeforeDelete = favoriteAlbumRepository.findAll().size();

        // Delete the favoriteAlbum
        restFavoriteAlbumMockMvc
            .perform(delete(ENTITY_API_URL_ID, favoriteAlbum.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FavoriteAlbum> favoriteAlbumList = favoriteAlbumRepository.findAll();
        assertThat(favoriteAlbumList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
