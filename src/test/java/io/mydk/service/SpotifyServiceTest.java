package io.mydk.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.mydk.service.dto.AlbumDTO;
import io.mydk.service.dto.ArtistDTO;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

class SpotifyServiceTest {
    private static RestTemplate restTemplate = new RestTemplate();
    private static SpotifyService spotifyService = new SpotifyService(restTemplate, null);

    @Test
    void searchArtist() {
        List<ArtistDTO> dtos = spotifyService.searchArtist("eiffel", true);
        assertEquals("[Eiffel]", dtos.toString());
    }

    @Test
    void searchAlbum() {
        List<AlbumDTO> dtos = spotifyService.searchAlbum("u2", "", true, true);
        assertEquals("["//
            + "U2 Boy 1980, "//
            + "U2 October 1981, "//
            + "U2 War 1983, "//
            + "U2 The Unforgettable Fire 1984, "//
            + "U2 The Joshua Tree 1987, "//
            + "U2 Rattle And Hum 1988, "//
            + "U2 Achtung Baby 1991, "//
            + "U2 Zooropa 1993, "//
            + "U2 Pop 1997, "//
            + "U2 All That You Can't Leave Behind 2000, "//
            + "U2 How To Dismantle An Atomic Bomb 2004, "//
            + "U2 Under A Blood Red Sky 2008, "//
            + "U2 No Line On The Horizon 2009, "//
            + "U2 Songs Of Innocence 2014, "//
            + "U2 Songs Of Experience (Deluxe Edition) 2017]", dtos.toString());

        dtos = spotifyService.searchAlbum("dEUS", "", true, true);
        assertEquals("["//
            + "dEUS Worst Case Scenario 1994, "//
            + "dEUS In A Bar, Under The Sea 1996, "//
            + "dEUS The Ideal Crash 1999, "//
            + "dEUS Pocket Revolution 2005, "//
            + "dEUS Vantage Point 2008, "//
            + "dEUS Keep You Close 2011, "//
            + "dEUS Following Sea 2012]", dtos.toString());

        dtos = spotifyService.searchAlbum("eiffel", "", true, true);
        assertEquals("["//
            + "Eiffel Abricotine 2001, "//
            + "Eiffel Le 1 / 4 d'heure des ahuris 2002, "//
            + "Eiffel Les yeux fermes (Live) 2004, "//
            + "Eiffel Tandoori 2006, "//
            + "Eiffel A tout moment 2009, "//
            + "Eiffel Foule monstre 2012, "//
            + "Eiffel Stupor Machine 2019]", dtos.toString());

        dtos = spotifyService.searchAlbum("pixies", "", true, true);
        assertEquals("["//
            + "Pixies Come On Pilgrim 1987, "//
            + "Pixies Surfer Rosa 1988, "//
            + "Pixies Doolittle 1989, "//
            + "Pixies Bossanova 1990, "//
            + "Pixies Trompe le Monde 1991, "//
            + "Pixies Indie Cindy 2014, "//
            + "Pixies Head Carrier 2016, "//
            + "Pixies Beneath the Eyrie (Deluxe) 2020, " //
            + "Pixies Doggerel 2022]", dtos.toString());

        dtos = spotifyService.searchAlbum("Pixies", "Doolittle", true, true);
        assertEquals("[Pixies Doolittle 1989]", dtos.toString());

        dtos = spotifyService.searchAlbum("Björk", "", true, true);
        assertEquals("["//
            + "Björk Gling-Glo 1990, "//
            + "Björk Debut 1993, "//
            + "Björk Post 1995, "//
            + "Björk Telegram 1996, "//
            + "Björk Homogenic 1997, "//
            + "Björk Selma Songs 2000, "//
            + "Björk Vespertine 2001, "//
            + "Björk Family tree 2002, "//
            + "Björk Medulla 2004, "//
            + "Björk Drawing Restraint 9 2005, "//
            + "Björk Volta 2007, "//
            + "Björk Voltaic 2009, "//
            + "Dirty Projectors Björk Mount Wittenberg Orca 2011, "//
            + "Björk Biophilia 2011, "//
            + "Björk Bastards 2012, "//
            + "Björk Vulnicura 2015, "//
            + "Björk Vulnicura Strings 2015, "//
            + "Björk Utopia 2017, "//
            + "Björk Fossora 2022]", dtos.toString());

        dtos = spotifyService.searchAlbum("Ben Harper", "", true, true);
        assertEquals("[Ben Harper Welcome To The Cruel World 1994, "//
            + "Ben Harper Fight For Your Mind 1995, "//
            + "Ben Harper The Will To Live 1997, "//
            + "Ben Harper Diamonds On The Inside 2003, "//
            + "Ben Harper The Blind Boys Of Alabama There Will Be A Light 2004, "//
            + "Ben Harper Both Sides Of The Gun 2006, "//
            + "Ben Harper Innocent Criminals Lifeline Tour Edition 2008, "//
            + "Ben Harper Give Till It's Gone 2011, "//
            + "Ben Harper Charlie Musselwhite Get Up! 2013, "//
            + "Ben Harper Ellen Harper Childhood Home 2014, "//
            + "Ben Harper Charlie Musselwhite No Mercy In This Land (Deluxe Edition) 2018, "//
            + "Ben Harper Winter Is For Lovers 2020, "//
            + "Ben Harper Bloodline Maintenance 2022]", dtos.toString());
    }

    @Test
    void getAlbum() {
        AlbumDTO dto = spotifyService.getAlbum("2co53OwPjlTvPDQTpErBtU");
        assertEquals("U2 Boy (Remastered) 1980", dto.toString());
    }

    @Test
    void getAlbums() {
        Map<String, AlbumDTO> dtos = spotifyService.getAlbums(Arrays.asList("2co53OwPjlTvPDQTpErBtU"));
        assertEquals("[U2 Boy (Remastered) 1980]", dtos.values().toString());

        dtos = spotifyService.getAlbums(Arrays.asList(//
            "2co53OwPjlTvPDQTpErBtU", //
            "595N69tnPNf3f023L7ftWa", //
            "2P2KGKsq7ItViExKB2TLzJ", //
            "4JfQDfKe4N5NCEyMP1mYT6", //
            "5vBZRYu2GLA65nfxBvG1a7", //
            "7hIoJcH4ObWasDFq78u1x9", //
            "5n52kyQKeUZs5ObZJejLQd", //
            "0IYjMBLA9PgtXyRPlLmTDE", //
            "5mojJwWgWNJcY3odUGgQc3", //
            "7gskILm9UyDvFlmmAoqn2g", //
            "5PQPur1PEZFDkI0AXbxFlB", //
            "1mgye9GJ5e2qCufD6wCNKC", //
            "2cVfcuckyfRP6KVAHsRPHC", //
            "45Ec0wYJ7npo6lXH4GWmxz", //
            "6S9YaGXnmRe8tWJ0e457HP", //

            "2Am8BBO6lHx7EganMrEFbi", //
            "1YZWEKi3WAzSk6IGnObgUO", //
            "4byfKlimeRvrbK6LkVkDDR", //
            "6rPwjofaO0LybGprINX1xT", //
            "2cG4AnDfRlkNmRz2viCFCc", //

            "79CvxovoFgGNuAMvQp9wul"));
        assertEquals("["//
            + "U2 Boy (Remastered) 1980, "//
            + "U2 October 1981, "//
            + "U2 War 1983, "//
            + "U2 The Unforgettable Fire 1984, "//
            + "U2 The Joshua Tree 1987, "//
            + "U2 Rattle And Hum 1988, "//
            + "U2 Achtung Baby 1991, "//
            + "U2 Zooropa 1993, "//
            + "U2 Pop 1997, "//
            + "U2 All That You Can't Leave Behind 2000, "//
            + "U2 How To Dismantle An Atomic Bomb 2004, "//
            + "U2 Under A Blood Red Sky 2008, "//
            + "U2 No Line On The Horizon 2009, "//
            + "U2 Songs Of Innocence 2014, "//
            + "U2 Songs Of Experience (Deluxe Edition) 2017, "//

            + "dEUS Worst Case Scenario 1994, "//
            + "dEUS The Ideal Crash 1999, "//
            + "dEUS Pocket Revolution 2005, "//
            + "dEUS Vantage Point 2008, "//
            + "dEUS Following Sea 2012, "//

            + "Pixies Come On Pilgrim 1987]", dtos.values().toString());
    }
}
