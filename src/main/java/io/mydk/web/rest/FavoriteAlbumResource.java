package io.mydk.web.rest;

import io.mydk.repository.FavoriteAlbumRepository;
import io.mydk.service.FavoriteAlbumService;
import io.mydk.service.dto.FavoriteAlbumDTO;
import io.mydk.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link io.mydk.domain.FavoriteAlbum}.
 */
@RestController
@RequestMapping("/api")
public class FavoriteAlbumResource {

    private final Logger log = LoggerFactory.getLogger(FavoriteAlbumResource.class);

    private static final String ENTITY_NAME = "favoriteAlbum";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FavoriteAlbumService favoriteAlbumService;

    private final FavoriteAlbumRepository favoriteAlbumRepository;

    public FavoriteAlbumResource(FavoriteAlbumService favoriteAlbumService, FavoriteAlbumRepository favoriteAlbumRepository) {
        this.favoriteAlbumService = favoriteAlbumService;
        this.favoriteAlbumRepository = favoriteAlbumRepository;
    }

    /**
     * {@code POST  /favorite-albums} : Create a new favoriteAlbum.
     *
     * @param favoriteAlbumDTO the favoriteAlbumDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new favoriteAlbumDTO, or with status {@code 400 (Bad Request)} if the favoriteAlbum has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/favorite-albums")
    public ResponseEntity<FavoriteAlbumDTO> createFavoriteAlbum(@Valid @RequestBody FavoriteAlbumDTO favoriteAlbumDTO)
        throws URISyntaxException {
        log.debug("REST request to save FavoriteAlbum : {}", favoriteAlbumDTO);
        if (favoriteAlbumDTO.getId() != null) {
            throw new BadRequestAlertException("A new favoriteAlbum cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FavoriteAlbumDTO result = favoriteAlbumService.save(favoriteAlbumDTO);
        return ResponseEntity
            .created(new URI("/api/favorite-albums/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /favorite-albums/:id} : Updates an existing favoriteAlbum.
     *
     * @param id the id of the favoriteAlbumDTO to save.
     * @param favoriteAlbumDTO the favoriteAlbumDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated favoriteAlbumDTO,
     * or with status {@code 400 (Bad Request)} if the favoriteAlbumDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the favoriteAlbumDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/favorite-albums/{id}")
    public ResponseEntity<FavoriteAlbumDTO> updateFavoriteAlbum(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FavoriteAlbumDTO favoriteAlbumDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FavoriteAlbum : {}, {}", id, favoriteAlbumDTO);
        if (favoriteAlbumDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, favoriteAlbumDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!favoriteAlbumRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FavoriteAlbumDTO result = favoriteAlbumService.update(favoriteAlbumDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, favoriteAlbumDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /favorite-albums/:id} : Partial updates given fields of an existing favoriteAlbum, field will ignore if it is null
     *
     * @param id the id of the favoriteAlbumDTO to save.
     * @param favoriteAlbumDTO the favoriteAlbumDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated favoriteAlbumDTO,
     * or with status {@code 400 (Bad Request)} if the favoriteAlbumDTO is not valid,
     * or with status {@code 404 (Not Found)} if the favoriteAlbumDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the favoriteAlbumDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/favorite-albums/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FavoriteAlbumDTO> partialUpdateFavoriteAlbum(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FavoriteAlbumDTO favoriteAlbumDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FavoriteAlbum partially : {}, {}", id, favoriteAlbumDTO);
        if (favoriteAlbumDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, favoriteAlbumDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!favoriteAlbumRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FavoriteAlbumDTO> result = favoriteAlbumService.partialUpdate(favoriteAlbumDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, favoriteAlbumDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /favorite-albums} : get all the favoriteAlbums.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of favoriteAlbums in body.
     */
    @GetMapping("/favorite-albums")
    public ResponseEntity<List<FavoriteAlbumDTO>> getAllFavoriteAlbums(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of FavoriteAlbums");
        Page<FavoriteAlbumDTO> page = favoriteAlbumService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /favorite-albums/:id} : get the "id" favoriteAlbum.
     *
     * @param id the id of the favoriteAlbumDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the favoriteAlbumDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/favorite-albums/{id}")
    public ResponseEntity<FavoriteAlbumDTO> getFavoriteAlbum(@PathVariable Long id) {
        log.debug("REST request to get FavoriteAlbum : {}", id);
        Optional<FavoriteAlbumDTO> favoriteAlbumDTO = favoriteAlbumService.findOne(id);
        return ResponseUtil.wrapOrNotFound(favoriteAlbumDTO);
    }

    /**
     * {@code DELETE  /favorite-albums/:id} : delete the "id" favoriteAlbum.
     *
     * @param id the id of the favoriteAlbumDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/favorite-albums/{id}")
    public ResponseEntity<Void> deleteFavoriteAlbum(@PathVariable Long id) {
        log.debug("REST request to delete FavoriteAlbum : {}", id);
        favoriteAlbumService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
