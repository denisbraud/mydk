package io.mydk.web.rest;

import io.mydk.service.SpotifyService;
import io.mydk.service.dto.AlbumDTO;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/spotify")
public class SpotifyResource {
    private final SpotifyService spotifyService;

    public SpotifyResource(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<AlbumDTO>> search(//
        @RequestParam(value = "artistName", required = false) String artistName, //
        @RequestParam(value = "albumName", required = false) String albumName, //
        @RequestParam(value = "mainAlbumOnly", required = false) boolean mainAlbumOnly) {
        List<AlbumDTO> dtos = spotifyService.searchAlbum(artistName, albumName, mainAlbumOnly, mainAlbumOnly);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }
}
