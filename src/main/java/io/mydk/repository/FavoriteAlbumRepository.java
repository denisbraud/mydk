package io.mydk.repository;

import io.mydk.domain.FavoriteAlbum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FavoriteAlbum entity.
 */
@Repository
public interface FavoriteAlbumRepository extends JpaRepository<FavoriteAlbum, Long> {
}
