package io.mydk.service;

import io.mydk.service.dto.AlbumDTO;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeSet;
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
    private String accessToken = null;

    private static class SpotifyServiceException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        private SpotifyServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public SpotifyService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
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

    public List<AlbumDTO> searchAlbum(String artistName, String albumName, boolean mainAlbumOnly, boolean exactMatch) {
        String q = getSearchQuery(artistName, albumName);
        Map<String, Object> result = getApiResult("search?type=album&offset=0&limit=50&q=" + q);
        Map<String, Object> albums = getAttributes(result, "albums");
        List<Map<String, Object>> itemsAlbums = getArray(albums, ITEMS);
        List<AlbumDTO> dtos = mapToAlbums(itemsAlbums, mainAlbumOnly);
        if (exactMatch) {
            dtos = exactMatch(dtos, artistName, albumName);
        }
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

    private static boolean isMainAlbum(AlbumDTO dto) {
        String albumTypeLw = dto.getType().toLowerCase();
        String nameLw = dto.getName().toLowerCase();
        return "album".equals(albumTypeLw) && //
            !nameLw.startsWith("live ") && !nameLw.startsWith("live:") //
            && !nameLw.contains("b-sides") && !nameLw.contains("b sides") //
            && !nameLw.contains(" remix") //
            && !nameLw.contains(" selected songs") && !nameLw.contains("best of");
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

    private static List<AlbumDTO> mapToAlbums(List<Map<String, Object>> itemsAlbums, boolean mainAlbumOnly) {
        Map<String, TreeSet<AlbumDTO>> albumsVersions = new LinkedHashMap<>();
        List<AlbumDTO> dtos = new ArrayList<>();
        for (Map<String, Object> album : itemsAlbums) {
            AlbumDTO dto = mapToAlbum(album);
            if (mainAlbumOnly) {
                if (!isMainAlbum(dto)) {
                    continue;
                }
                String key = dto.getArtistName() + "-" + dto.getName();
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

        String[] names = StringUtils.split((String) album.get("name"), "(");
        dto.setName(StringUtils.replace(names[0].trim(), "’", "'"));
        if (names.length > 1) {
            dto.setNameSuffix("(" + names[1].trim());
        }
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
}
