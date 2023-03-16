package com.restapi.scoregoat.service;

import com.restapi.scoregoat.client.FootballClient;
import com.restapi.scoregoat.controller.exception.SeasonNotFoundException;
import com.restapi.scoregoat.domain.Season;
import com.restapi.scoregoat.repository.SeasonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class SeasonDbServiceTests {
    @InjectMocks
    SeasonDbService service;
    @Mock
    private SeasonRepository repository;
    @Mock
    private FootballClient client;

    @Test
    public void shouldSetSeasonYear() throws SeasonNotFoundException {
        //Given
        Long id = 39L;
        Season season = new Season(client.getFootballSeason(id));
        when(client.getFootballSeason(id)).thenReturn("2023");
        when(repository.save(season)).thenReturn(season);

        //When
        Season theSeason = service.setSeason(id);

        //Then
        verify(repository, times(1)).deleteAll();
        verify(repository, times(1)).save(any(Season.class));
        assertEquals("2023", theSeason.getYear());
    }
}
