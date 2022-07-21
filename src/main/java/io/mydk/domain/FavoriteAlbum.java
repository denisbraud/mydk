package io.mydk.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FavoriteAlbum.
 */
@Entity
@Table(name = "favorite_album")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FavoriteAlbum implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "login", length = 50, nullable = false)
    private String login;

    @NotNull
    @Size(max = 255)
    @Column(name = "album_spotify_id", length = 255, nullable = false)
    private String albumSpotifyId;

    @NotNull
    @Column(name = "rank", nullable = false)
    private Integer rank;

    @Size(max = 255)
    @Column(name = "comment", length = 255)
    private String comment;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FavoriteAlbum id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return this.login;
    }

    public FavoriteAlbum login(String login) {
        this.setLogin(login);
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAlbumSpotifyId() {
        return this.albumSpotifyId;
    }

    public FavoriteAlbum albumSpotifyId(String albumSpotifyId) {
        this.setAlbumSpotifyId(albumSpotifyId);
        return this;
    }

    public void setAlbumSpotifyId(String albumSpotifyId) {
        this.albumSpotifyId = albumSpotifyId;
    }

    public Integer getRank() {
        return this.rank;
    }

    public FavoriteAlbum rank(Integer rank) {
        this.setRank(rank);
        return this;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getComment() {
        return this.comment;
    }

    public FavoriteAlbum comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FavoriteAlbum)) {
            return false;
        }
        return id != null && id.equals(((FavoriteAlbum) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FavoriteAlbum{" +
            "id=" + getId() +
            ", login='" + getLogin() + "'" +
            ", albumSpotifyId='" + getAlbumSpotifyId() + "'" +
            ", rank=" + getRank() +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
