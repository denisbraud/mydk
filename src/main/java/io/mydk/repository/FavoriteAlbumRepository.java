package io.mydk.repository;

import io.mydk.domain.FavoriteAlbum;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FavoriteAlbum entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FavoriteAlbumRepository extends JpaRepository<FavoriteAlbum, Long> {}
