package io.mydk.service;

import io.mydk.domain.FavoriteAlbum;
import io.mydk.repository.FavoriteAlbumRepository;
import io.mydk.security.SecurityUtils;
import io.mydk.service.dto.AlbumDTO;
import io.mydk.service.dto.ArtistDTO;
import io.mydk.util.StringUtil;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class SpotifyService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private static final String ITEMS = "items";
    private final RestTemplate restTemplate;
    private final FavoriteAlbumRepository favoriteAlbumRepository;
    private String accessToken = null;

    private static class SpotifyServiceException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        private SpotifyServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public SpotifyService(RestTemplate restTemplate, FavoriteAlbumRepository favoriteAlbumRepository) {
        this.restTemplate = restTemplate;
        this.favoriteAlbumRepository = favoriteAlbumRepository;
    }

    private String getAccessToken(boolean forceNew) {
        if (!forceNew && StringUtils.isNotBlank(accessToken)) {
            return accessToken;
        }
        try {
            Properties props = new Properties();
            props.load(getClass().getResourceAsStream("/spotify.properties"));
            accessToken = getAccessTokenNew(props.getProperty("clientId"), props.getProperty("clientSecret"));
        } catch (Exception e) {
            throw new SpotifyServiceException("src/main/resources/spotify.properties not found\nclientId=xxx\nclientSecret=xx", e);
        }
        return accessToken;
    }

    private String getAccessTokenNew(String clientId, String clientSecret) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic " //
            + Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes()));

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        String url = "https://accounts.spotify.com/api/token";
        log.info("Spotify POST {}", url);
        ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.POST, request, Object.class);
        Map<String, Object> result = getBody(response);
        return (String) result.get("access_token");
    }

    public List<ArtistDTO> searchArtist(String artistName, boolean exactMatch) {
        String q = getSearchQuery(artistName, null);
        Map<String, Object> result = getApiResult("search?type=artist&offset=0&limit=50&q=" + q);
        Map<String, Object> albums = getAttributes(result, "artists");
        List<Map<String, Object>> itemsArtists = getArray(albums, ITEMS);
        List<ArtistDTO> dtos = new ArrayList<>();
        for (Map<String, Object> artist : itemsArtists) {
            ArtistDTO dto = mapToArtist(artist);
            if (dto != null) {
                dtos.add(dto);
            }
        }
        if (exactMatch) {
            dtos = exactMatch(dtos, artistName);
        }
        return dtos;
    }

    public List<AlbumDTO> searchAlbum(String artistName, String albumName, boolean mainAlbumOnly, boolean exactMatch) {
        String q = getSearchQuery(artistName, albumName);
        if (q.isEmpty() && favoriteAlbumRepository != null) {
            return getCurrentUserFavoriteAlbums();
        }
        if (StringUtils.isBlank(albumName)) {
            List<ArtistDTO> artists = searchArtist(artistName, exactMatch);
            if (artists.size() == 1) {
                return getArtistAlbums(artists.get(0), mainAlbumOnly);
            }
        }
        int limit = 50;
        String query = "search?type=album&q=" + q + "&limit=" + limit + "&offset=";
        List<Map<String, Object>> itemsAlbums = new ArrayList<>();
        int offset = 0;
        int itemsAlbumsISize = limit;
        while (itemsAlbumsISize == limit) {
            Map<String, Object> result = getApiResult(query + offset);
            Map<String, Object> albums = getAttributes(result, "albums");
            List<Map<String, Object>> itemsAlbumsI = getArray(albums, ITEMS);
            itemsAlbumsISize = itemsAlbumsI.size();
            itemsAlbums.addAll(itemsAlbumsI);
            offset += limit;
        }
        List<AlbumDTO> dtos = mapToAlbums(itemsAlbums, mainAlbumOnly);
        if (exactMatch) {
            dtos = exactMatch(dtos, artistName, albumName);
        }
        addFavoriteInfos(dtos);
        Collections.sort(dtos);
        return dtos;
    }

    public List<AlbumDTO> getArtistAlbums(ArtistDTO artist, boolean mainAlbumOnly) {
        // https://developer.spotify.com/documentation/web-api/reference/#/operations/get-an-artists-albums
        // https://developer.spotify.com/console/get-artist-albums/
        int limit = 50;
        String query = "artists/" + artist.getSpotifyId() + "/albums?include_groups=album" + (mainAlbumOnly ? "" : ",compilation") + "&limit=" + limit + "&offset=";
        List<Map<String, Object>> itemsAlbums = new ArrayList<>();
        int offset = 0;
        int itemsAlbumsISize = limit;
        while (itemsAlbumsISize == limit) {
            Map<String, Object> result = getApiResult(query + offset);
            List<Map<String, Object>> itemsAlbumsI = getArray(result, ITEMS);
            itemsAlbumsISize = itemsAlbumsI.size();
            itemsAlbums.addAll(itemsAlbumsI);
            offset += limit;
        }
        List<AlbumDTO> dtos = mapToAlbums(itemsAlbums, mainAlbumOnly);
        addFavoriteInfos(dtos);
        Collections.sort(dtos);
        return dtos;
    }

    public Map<String, AlbumDTO> getAlbums(List<String> spotifyIds) {
        Map<String, AlbumDTO> dtos = new LinkedHashMap<>();
        int len = spotifyIds.size();
        int from = 0;
        int to = Math.min(from + 20, len);
        while (from < len) {
            Map<String, Object> result = getApiResult("albums?ids=" + String.join(",", spotifyIds.subList(from, to)));
            List<Map<String, Object>> itemsAlbums = getArray(result, "albums");
            for (Map<String, Object> album : itemsAlbums) {
                AlbumDTO dto = mapToAlbum(album);
                dtos.put(dto.getSpotifyId(), dto);
            }
            from += 20;
            to = Math.min(from + 20, len);
        }
        return dtos;
    }

    public AlbumDTO getAlbum(String spotifyId) {
        Map<String, Object> result = getApiResult("albums/" + spotifyId);
        return mapToAlbum(result);
    }

    private List<AlbumDTO> getCurrentUserFavoriteAlbums() {
        String currentLogin = SecurityUtils.getCurrentUserLogin().orElse(null);
        if (favoriteAlbumRepository == null || StringUtils.isBlank(currentLogin)) {
            return Collections.emptyList();
        }
        List<FavoriteAlbum> albums = favoriteAlbumRepository.findByLogin(currentLogin);
        List<String> spotifyIds = albums.stream().map(FavoriteAlbum::getAlbumSpotifyId).collect(Collectors.toList());
        Map<String, AlbumDTO> spotifyAlbums = getAlbums(spotifyIds);
        List<AlbumDTO> dtos = new ArrayList<>();
        for (FavoriteAlbum album : albums) {
            AlbumDTO dto = spotifyAlbums.get(album.getAlbumSpotifyId());
            dtos.add(addFavoriteInfos(dto, album));
        }
        Collections.sort(dtos);
        return dtos;
    }

    private void addFavoriteInfos(List<AlbumDTO> dtos) {
        String currentLogin = SecurityUtils.getCurrentUserLogin().orElse(null);
        if (favoriteAlbumRepository == null || StringUtils.isBlank(currentLogin)) {
            return;
        }
        List<FavoriteAlbum> albums = favoriteAlbumRepository.findByLogin(currentLogin);
        for (AlbumDTO dto : dtos) {
            FavoriteAlbum album = albums.stream().filter(a -> a.getAlbumSpotifyId().equals(dto.getSpotifyId())).findAny().orElse(null);
            addFavoriteInfos(dto, album);
        }
    }

    private static boolean isMainAlbum(AlbumDTO dto) {
        String albumTypeLw = dto.getType().toLowerCase();
        return "album".equals(albumTypeLw) && isMainAlbumFromName(dto.getName()) && isMainAlbumFromName(dto.getNameSuffix());
    }

    private static boolean isMainAlbumFromName(String name) {
        String nameLw = StringUtils.defaultString(name).toLowerCase();
        return !nameLw.startsWith("live ") && !nameLw.equals("live") && !(nameLw.endsWith(" live") && !nameLw.endsWith("to live"))//
            && !nameLw.contains("b-sides") && !nameLw.contains("b sides") //
            && !nameLw.contains("remix") && !nameLw.contains("singles") && !nameLw.contains("demos") //
            && !nameLw.contains("retrospective") && !nameLw.contains("collection") && !nameLw.contains("itunes ")//
            && !nameLw.contains("selected songs") && !nameLw.contains("best of");
    }

    private static List<ArtistDTO> exactMatch(List<ArtistDTO> dtos, String artistName) {
        boolean foundExactMatchOnArtist = false;
        for (ArtistDTO dto : dtos) {
            if (dto.getName().equalsIgnoreCase(artistName)) {
                foundExactMatchOnArtist = true;
            }
        }
        if (!foundExactMatchOnArtist) {
            return dtos;
        }
        List<ArtistDTO> newDtos = new ArrayList<>();
        for (ArtistDTO dto : dtos) {
            if (dto.getName().equalsIgnoreCase(artistName)) {
                newDtos.add(dto);
            }
        }
        return newDtos;
    }

    private static List<AlbumDTO> exactMatch(List<AlbumDTO> dtos, String artistName, String albumName) {
        boolean foundExactMatchOnArtist = false;
        boolean foundExactMatchOnAlbum = false;
        for (AlbumDTO dto : dtos) {
            if (dto.getArtistName().equalsIgnoreCase(artistName)) {
                foundExactMatchOnArtist = true;
            }
            if (dto.getName().equalsIgnoreCase(albumName)) {
                foundExactMatchOnAlbum = true;
            }
        }
        if (!foundExactMatchOnArtist && !foundExactMatchOnAlbum) {
            return dtos;
        }
        List<AlbumDTO> newDtos = new ArrayList<>();
        for (AlbumDTO dto : dtos) {
            if (foundExactMatchOnArtist && dto.getArtistName().equalsIgnoreCase(artistName)) {
                newDtos.add(dto);
            }
        }
        return newDtos;
    }

    private static String getSearchQuery(String artistName, String albumName) {
        StringBuilder q = new StringBuilder();
        if (StringUtils.isNotBlank(artistName)) {
            q.append("artist:" + artistName);
        }
        if (StringUtils.isNotBlank(artistName) && StringUtils.isNotBlank(albumName)) {
            q.append("+");
        }
        if (StringUtils.isNotBlank(albumName)) {
            q.append("album:" + albumName);
        }
        return q.toString();
    }

    private Map<String, Object> getApiResult(String urlSuffix) {
        String url = "https://api.spotify.com/v1/" + urlSuffix;
        log.info("Spotify GET {}", url);
        try {
            return getBody(restTemplate.exchange(url, HttpMethod.GET, getApiRequest(getAccessToken(false)), Object.class));
        } catch (HttpClientErrorException e) {
            if (StringUtils.containsIgnoreCase(e.getMessage(), "access token expired")) {
                log.info("access token expired : getting new one");
                return getBody(restTemplate.exchange(url, HttpMethod.GET, getApiRequest(getAccessToken(true)), Object.class));
            }
            throw e;
        }
    }

    private HttpEntity<String> getApiRequest(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        return new HttpEntity<>("", headers);
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> getBody(ResponseEntity<Object> response) {
        Object body = response == null ? null : response.getBody();
        if (!(body instanceof Map<?, ?>)) {
            return Collections.emptyMap();
        }
        return (Map<String, Object>) body;
    }

    @SuppressWarnings("unchecked")
    private static List<Map<String, Object>> getArray(Map<String, Object> element, String key) {
        Object array = element == null ? null : element.get(key);
        if (!(array instanceof List<?>)) {
            return Collections.emptyList();
        }
        return (List<Map<String, Object>>) array;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> getAttributes(Map<String, Object> element, String key) {
        Object attrs = element == null ? null : element.get(key);
        if (!(attrs instanceof Map<?, ?>)) {
            return Collections.emptyMap();
        }
        return (Map<String, Object>) attrs;
    }

    private static ArtistDTO mapToArtist(Map<String, Object> artist) {
        Integer popularity = (Integer) artist.get("popularity");
        if (popularity != null && popularity < 5) {
            return null;
        }
        ArtistDTO dto = new ArtistDTO();
        dto.setSpotifyId((String) artist.get("id"));
        dto.setName((String) artist.get("name"));
        return dto;
    }

    private static List<AlbumDTO> mapToAlbums(List<Map<String, Object>> itemsAlbums, boolean mainAlbumOnly) {
        Map<String, TreeSet<AlbumDTO>> albumsVersions = new LinkedHashMap<>();
        List<AlbumDTO> dtos = new ArrayList<>();
        for (Map<String, Object> album : itemsAlbums) {
            AlbumDTO dto = mapToAlbum(album);
            if (mainAlbumOnly) {
                if (!isMainAlbum(dto)) {
                    continue;
                }
                String key = dto.getArtistName() + "-" + StringUtil.toCode(dto.getName());
                albumsVersions.computeIfAbsent(key, k -> new TreeSet<>()).add(dto);
            } else {
                dtos.add(dto);
            }
        }
        if (mainAlbumOnly) {
            for (Map.Entry<String, TreeSet<AlbumDTO>> es : albumsVersions.entrySet()) {
                dtos.add(es.getValue().iterator().next());
            }
        }
        return dtos;
    }

    private static AlbumDTO mapToAlbum(Map<String, Object> album) {
        AlbumDTO dto = new AlbumDTO();
        dto.setSpotifyId((String) album.get("id"));
        String name = StringUtils.replace((String) album.get("name"), "’", "'");
        int index = StringUtils.indexOfAny(name, '(', ':');
        if (index > 0) {
            dto.setNameSuffix(name.substring(index).trim());
            name = name.substring(0, index).trim();
        }
        dto.setName(name);
        dto.setType((String) album.get("album_type"));

        dto.setReleaseDate(StringUtils.substringBefore((String) album.get("release_date"), "-"));
        dto.setTotalTracks((Integer) album.get("total_tracks"));

        List<Map<String, Object>> artists = getArray(album, "artists");
        StringBuilder artistsName = new StringBuilder();
        for (Map<String, Object> artist : artists) {
            artistsName.append((String) artist.get("name")).append(" ");
        }
        dto.setArtistName(artistsName.toString().trim());

        List<Map<String, Object>> images = getArray(album, "images");
        String imageUrl = null;
        int imageWidth = 0;
        for (Map<String, Object> image : images) {
            String iUrl = (String) image.get("url");
            if (StringUtils.isBlank(iUrl)) {
                continue;
            }
            int iWidth = (Integer) image.get("width");
            if (StringUtils.isBlank(imageUrl) || iWidth > imageWidth) {
                imageUrl = iUrl;
                imageWidth = iWidth;
            }
        }
        dto.setImageUrl(imageUrl);
        return dto;
    }

    public static AlbumDTO addFavoriteInfos(AlbumDTO dto, FavoriteAlbum album) {
        if (dto == null || album == null) {
            return dto;
        }
        dto.setFavoriteAlbumId(album.getId());
        dto.setRank(album.getRank());
        dto.setComment(album.getComment());
        return dto;
    }
}
