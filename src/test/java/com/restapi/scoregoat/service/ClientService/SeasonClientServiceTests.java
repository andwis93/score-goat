package com.restapi.scoregoat.service.ClientService;

import com.restapi.scoregoat.client.FootballClient;
import com.restapi.scoregoat.domain.Season;
import com.restapi.scoregoat.service.DBService.SeasonDBService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class SeasonClientServiceTests {
    @InjectMocks
    SeasonClientService service;
    @Mock
    private SeasonDBService dbService;
    @Mock
    private FootballClient client;


    @Test
    public void shouldSetSeasonYear() {
        //Given
        when(client.getFootballSeason()).thenReturn("2023");

        //When
        Season theSeason = service.setSeason();

        //Then
        verify(dbService, times(1)).deleteAll();
        verify(dbService, times(1)).save(any(Season.class));
        assertEquals("2023", theSeason.getYear());
    }

    @Test
    public void shouldFetchSeason() {
        //Given
        List<Season> list = new ArrayList<>();
        Season season = new Season("2023");
        list.add(season);
        when(dbService.findAll()).thenReturn(list);

        //When
        Season theSeason = service.fetchSeason();

        //Then
        assertEquals("2023", theSeason.getYear());
    }
}
