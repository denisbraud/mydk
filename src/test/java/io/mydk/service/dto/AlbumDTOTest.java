package io.mydk.service.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class AlbumDTOTest {
    @Test
    void compare() {
        List<AlbumDTO> dtos = Arrays.asList(//
            getAlbumDTO("The Josua Tree", "(Remastered)"), //
            getAlbumDTO("The Josua Tree", "(Deluxe Edition)"), //
            getAlbumDTO("The Josua Tree", "(Deluxe Edition Remastered)"), //
            getAlbumDTO("The Josua Tree", null));
        Collections.sort(dtos);
        assertEquals("["//
            + "The Josua Tree, "//
            + "The Josua Tree (Remastered), "//
            + "The Josua Tree (Deluxe Edition Remastered), "//
            + "The Josua Tree (Deluxe Edition)]", dtos.toString());
    }

    private AlbumDTO getAlbumDTO(String name, String nameSuffix) {
        AlbumDTO dto = new AlbumDTO();
        dto.setName(name);
        dto.setNameSuffix(nameSuffix);
        return dto;
    }
}
