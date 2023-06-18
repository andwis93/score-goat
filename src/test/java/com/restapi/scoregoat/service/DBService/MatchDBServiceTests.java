package com.restapi.scoregoat.service.DBService;

import com.restapi.scoregoat.config.SeasonConfig;
import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.repository.MatchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.OffsetDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MatchDBServiceTests {
    @InjectMocks
    private MatchDBService service;
    @Mock
    private MatchRepository repository;

    @Test
    void testFindMatch() {
        //Given
        Match match = new Match(1L, SeasonConfig.DEFAULT_LEAGUE.getId(), 365L, OffsetDateTime.parse("2023-04-01T14:00:00+00:00"),
                "Not Started", "81:48", "Liverpool", "Liverpool.logo",
                true, "Everton", "Everton.logo", false, 2, 1);
        when(repository.existsByFixtureId(365L)).thenReturn(true);
        when(repository.findByFixtureId(365L)).thenReturn(Optional.of(match));

        //When
        Match theMatch = service.findMatchByFixture(365L);

        //Then
        assertEquals(1L, theMatch.getId());
    }
}
