package io.mydk.service.mapper;

import io.mydk.domain.FavoriteAlbum;
import io.mydk.service.dto.FavoriteAlbumDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FavoriteAlbum} and its DTO {@link FavoriteAlbumDTO}.
 */
@Mapper(componentModel = "spring")
public interface FavoriteAlbumMapper extends EntityMapper<FavoriteAlbumDTO, FavoriteAlbum> {}
