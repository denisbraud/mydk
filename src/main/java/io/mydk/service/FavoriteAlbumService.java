package io.mydk.service;

import io.mydk.domain.FavoriteAlbum;
import io.mydk.repository.FavoriteAlbumRepository;
import io.mydk.service.dto.FavoriteAlbumDTO;
import io.mydk.service.mapper.FavoriteAlbumMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FavoriteAlbum}.
 */
@Service
@Transactional
public class FavoriteAlbumService {

    private final Logger log = LoggerFactory.getLogger(FavoriteAlbumService.class);

    private final FavoriteAlbumRepository favoriteAlbumRepository;

    private final FavoriteAlbumMapper favoriteAlbumMapper;

    public FavoriteAlbumService(FavoriteAlbumRepository favoriteAlbumRepository, FavoriteAlbumMapper favoriteAlbumMapper) {
        this.favoriteAlbumRepository = favoriteAlbumRepository;
        this.favoriteAlbumMapper = favoriteAlbumMapper;
    }

    /**
     * Save a favoriteAlbum.
     *
     * @param favoriteAlbumDTO the entity to save.
     * @return the persisted entity.
     */
    public FavoriteAlbumDTO save(FavoriteAlbumDTO favoriteAlbumDTO) {
        log.debug("Request to save FavoriteAlbum : {}", favoriteAlbumDTO);
        FavoriteAlbum favoriteAlbum = favoriteAlbumMapper.toEntity(favoriteAlbumDTO);
        favoriteAlbum = favoriteAlbumRepository.save(favoriteAlbum);
        return favoriteAlbumMapper.toDto(favoriteAlbum);
    }

    /**
     * Update a favoriteAlbum.
     *
     * @param favoriteAlbumDTO the entity to save.
     * @return the persisted entity.
     */
    public FavoriteAlbumDTO update(FavoriteAlbumDTO favoriteAlbumDTO) {
        log.debug("Request to update FavoriteAlbum : {}", favoriteAlbumDTO);
        FavoriteAlbum favoriteAlbum = favoriteAlbumMapper.toEntity(favoriteAlbumDTO);
        favoriteAlbum = favoriteAlbumRepository.save(favoriteAlbum);
        return favoriteAlbumMapper.toDto(favoriteAlbum);
    }

    /**
     * Partially update a favoriteAlbum.
     *
     * @param favoriteAlbumDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FavoriteAlbumDTO> partialUpdate(FavoriteAlbumDTO favoriteAlbumDTO) {
        log.debug("Request to partially update FavoriteAlbum : {}", favoriteAlbumDTO);

        return favoriteAlbumRepository
            .findById(favoriteAlbumDTO.getId())
            .map(existingFavoriteAlbum -> {
                favoriteAlbumMapper.partialUpdate(existingFavoriteAlbum, favoriteAlbumDTO);

                return existingFavoriteAlbum;
            })
            .map(favoriteAlbumRepository::save)
            .map(favoriteAlbumMapper::toDto);
    }

    /**
     * Get all the favoriteAlbums.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FavoriteAlbumDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FavoriteAlbums");
        return favoriteAlbumRepository.findAll(pageable).map(favoriteAlbumMapper::toDto);
    }

    /**
     * Get one favoriteAlbum by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FavoriteAlbumDTO> findOne(Long id) {
        log.debug("Request to get FavoriteAlbum : {}", id);
        return favoriteAlbumRepository.findById(id).map(favoriteAlbumMapper::toDto);
    }

    /**
     * Delete the favoriteAlbum by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete FavoriteAlbum : {}", id);
        favoriteAlbumRepository.deleteById(id);
    }
}
