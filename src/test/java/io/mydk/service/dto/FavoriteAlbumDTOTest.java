package io.mydk.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.mydk.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FavoriteAlbumDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FavoriteAlbumDTO.class);
        FavoriteAlbumDTO favoriteAlbumDTO1 = new FavoriteAlbumDTO();
        favoriteAlbumDTO1.setId(1L);
        FavoriteAlbumDTO favoriteAlbumDTO2 = new FavoriteAlbumDTO();
        assertThat(favoriteAlbumDTO1).isNotEqualTo(favoriteAlbumDTO2);
        favoriteAlbumDTO2.setId(favoriteAlbumDTO1.getId());
        assertThat(favoriteAlbumDTO1).isEqualTo(favoriteAlbumDTO2);
        favoriteAlbumDTO2.setId(2L);
        assertThat(favoriteAlbumDTO1).isNotEqualTo(favoriteAlbumDTO2);
        favoriteAlbumDTO1.setId(null);
        assertThat(favoriteAlbumDTO1).isNotEqualTo(favoriteAlbumDTO2);
    }
}
