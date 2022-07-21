package io.mydk.repository;

import io.mydk.domain.FavoriteAlbum;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FavoriteAlbum entity.
 */
@Repository
public interface FavoriteAlbumRepository extends JpaRepository<FavoriteAlbum, Long> {
    List<FavoriteAlbum> findByLogin(String login);
}
