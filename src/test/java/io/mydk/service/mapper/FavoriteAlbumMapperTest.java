package io.mydk.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FavoriteAlbumMapperTest {

    private FavoriteAlbumMapper favoriteAlbumMapper;

    @BeforeEach
    public void setUp() {
        favoriteAlbumMapper = new FavoriteAlbumMapperImpl();
    }
}
