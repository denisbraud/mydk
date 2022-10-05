package io.mydk.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.mydk.domain.FavoriteAlbum} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FavoriteAlbumDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String login;

    @NotNull
    @Size(max = 255)
    private String albumSpotifyId;

    @NotNull
    private Integer rank;

    @Size(max = 255)
    private String comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAlbumSpotifyId() {
        return albumSpotifyId;
    }

    public void setAlbumSpotifyId(String albumSpotifyId) {
        this.albumSpotifyId = albumSpotifyId;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FavoriteAlbumDTO)) {
            return false;
        }

        FavoriteAlbumDTO favoriteAlbumDTO = (FavoriteAlbumDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, favoriteAlbumDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FavoriteAlbumDTO{" +
            "id=" + getId() +
            ", login='" + getLogin() + "'" +
            ", albumSpotifyId='" + getAlbumSpotifyId() + "'" +
            ", rank=" + getRank() +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
