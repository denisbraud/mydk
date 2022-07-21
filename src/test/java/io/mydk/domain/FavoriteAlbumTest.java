package io.mydk.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.mydk.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FavoriteAlbumTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FavoriteAlbum.class);
        FavoriteAlbum favoriteAlbum1 = new FavoriteAlbum();
        favoriteAlbum1.setId(1L);
        FavoriteAlbum favoriteAlbum2 = new FavoriteAlbum();
        favoriteAlbum2.setId(favoriteAlbum1.getId());
        assertThat(favoriteAlbum1).isEqualTo(favoriteAlbum2);
        favoriteAlbum2.setId(2L);
        assertThat(favoriteAlbum1).isNotEqualTo(favoriteAlbum2);
        favoriteAlbum1.setId(null);
        assertThat(favoriteAlbum1).isNotEqualTo(favoriteAlbum2);
    }
}
