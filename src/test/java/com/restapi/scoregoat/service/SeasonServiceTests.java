package com.restapi.scoregoat.service;

import com.restapi.scoregoat.client.FootballClient;
import com.restapi.scoregoat.domain.Season;
import com.restapi.scoregoat.repository.SeasonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class SeasonServiceTests {
    @InjectMocks
    SeasonService service;
    @Mock
    private SeasonRepository repository;
    @Mock
    private FootballClient client;

    @Test
    public void shouldSetSeasonYear() {
        //Given
        Season season = new Season(1L, "2023");
        when(repository.save(season)).thenReturn(season);
        when(client.getFootballSeason()).thenReturn("2023");

        //When
        Season theSeason = service.setSeason();

        //Then
        verify(repository, times(1)).deleteAll();
        verify(repository, times(1)).save(any(Season.class));
        assertEquals("2023", theSeason.getYear());
    }
}
