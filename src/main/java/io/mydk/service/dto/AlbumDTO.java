package io.mydk.service.dto;

import java.io.Serializable;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

public class AlbumDTO implements Comparable<AlbumDTO>, Serializable {
    private static final long serialVersionUID = 1L;
    private String spotifyId;
    private String name;
    private String nameSuffix;
    private String type;
    private String artistName;
    private String imageUrl;
    private String releaseDate;
    private int totalTracks;

    private Long favoriteAlbumId;
    private Integer rank;
    private String comment;

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

    public String getNameSuffix() {
        return nameSuffix;
    }

    public void setNameSuffix(String nameSuffix) {
        this.nameSuffix = nameSuffix;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getTotalTracks() {
        return totalTracks;
    }

    public void setTotalTracks(int totalTracks) {
        this.totalTracks = totalTracks;
    }

    public Long getFavoriteAlbumId() {
        return favoriteAlbumId;
    }

    public void setFavoriteAlbumId(Long favoriteAlbumId) {
        this.favoriteAlbumId = favoriteAlbumId;
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
    public String toString() {
        String str = "";
        if (StringUtils.isNotBlank(artistName)) {
            str += " " + artistName;
        }
        if (StringUtils.isNotBlank(name)) {
            str += " " + name;
        }
        if (StringUtils.isNotBlank(nameSuffix)) {
            str += " " + nameSuffix;
        }
        if (StringUtils.isNotBlank(releaseDate)) {
            str += " " + releaseDate;
        }
        return str.trim();
    }

    @Override
    public int compareTo(AlbumDTO obj) {
        int compare = StringUtils.compare(releaseDate, obj.getReleaseDate());
        if (compare != 0) {
            return compare;
        }
        String nameSuffixComputed = getNameSuffixComputedForSort(nameSuffix);
        String objNameSuffixComputed = getNameSuffixComputedForSort(obj.getNameSuffix());
        return StringUtils.compare(nameSuffixComputed, objNameSuffixComputed);
    }

    private String getNameSuffixComputedForSort(String suffix) {
        if (StringUtils.isBlank(suffix)) {
            return "0";
        } else if (StringUtils.containsIgnoreCase(suffix, "(remaster")) {
            return "1-" + suffix;
        } else if (StringUtils.containsIgnoreCase(suffix, "remaster")) {
            return "2-" + suffix;
        } else {
            return "3-" + suffix;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(spotifyId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AlbumDTO other = (AlbumDTO) obj;
        return Objects.equals(spotifyId, other.spotifyId);
    }
}
