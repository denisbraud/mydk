package io.mydk.service.dto;

import java.io.Serializable;

public class ArtistDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String spotifyId;
    private String name;

    public String getSpotifyId() {
        return spotifyId;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
