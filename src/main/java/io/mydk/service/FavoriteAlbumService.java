package io.mydk.service;

import io.mydk.domain.FavoriteAlbum;
import io.mydk.repository.FavoriteAlbumRepository;
import io.mydk.service.dto.AlbumDTO;
import io.mydk.service.dto.FavoriteAlbumDTO;
import io.mydk.service.mapper.FavoriteAlbumMapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
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
    private final SpotifyService spotifyService;

    public FavoriteAlbumService(FavoriteAlbumRepository favoriteAlbumRepository, FavoriteAlbumMapper favoriteAlbumMapper, SpotifyService spotifyService) {
        this.favoriteAlbumRepository = favoriteAlbumRepository;
        this.favoriteAlbumMapper = favoriteAlbumMapper;
        this.spotifyService = spotifyService;
    }

    private FavoriteAlbumDTO toDto(FavoriteAlbum favoriteAlbum) {
        AlbumDTO spotifyAlbum = spotifyService.getAlbum(favoriteAlbum.getAlbumSpotifyId());
        spotifyAlbum = SpotifyService.addFavoriteInfos(spotifyAlbum, favoriteAlbum);
        FavoriteAlbumDTO dto = favoriteAlbumMapper.toDto(favoriteAlbum);
        dto.setAlbum(spotifyAlbum);
        return dto;
    }

    private FavoriteAlbumDTO toDto(FavoriteAlbum favoriteAlbum, Map<String, AlbumDTO> spotifyAlbums) {
        AlbumDTO spotifyAlbum = spotifyAlbums.get(favoriteAlbum.getAlbumSpotifyId());
        spotifyAlbum = SpotifyService.addFavoriteInfos(spotifyAlbum, favoriteAlbum);
        FavoriteAlbumDTO dto = favoriteAlbumMapper.toDto(favoriteAlbum);
        dto.setAlbum(spotifyAlbum);
        return dto;
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
        return toDto(favoriteAlbum);
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

        return favoriteAlbumRepository.findById(favoriteAlbumDTO.getId()).map(existingFavoriteAlbum -> {
            favoriteAlbumMapper.partialUpdate(existingFavoriteAlbum, favoriteAlbumDTO);

            return existingFavoriteAlbum;
        }).map(favoriteAlbumRepository::save).map(this::toDto);
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
        Page<FavoriteAlbum> albums = favoriteAlbumRepository.findAll(pageable);
        List<String> spotifyIds = albums.stream().map(FavoriteAlbum::getAlbumSpotifyId).collect(Collectors.toList());
        Map<String, AlbumDTO> spotifyAlbums = spotifyService.getAlbums(spotifyIds);
        return albums.map(album -> this.toDto(album, spotifyAlbums));
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
        return favoriteAlbumRepository.findById(id).map(this::toDto);
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
