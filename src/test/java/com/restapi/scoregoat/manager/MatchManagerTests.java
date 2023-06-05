package com.restapi.scoregoat.manager;

import com.restapi.scoregoat.config.SeasonConfig;
import com.restapi.scoregoat.domain.Match;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MatchManagerTests {
    @Autowired
    private MatchManager manager;

    @Test
    void testMatchResultAssign() {
        //Given
        Match match = new Match(327L, SeasonConfig.DEFAULT_LEAGUE.getId(), 365L, OffsetDateTime.parse("2023-04-01T14:00:00+00:00"),
                "Not Started", "81:48", "Liverpool", "Liverpool.logo",
                true, "Everton", "Everton.logo", false, 2, 1);

        //When
        String result = manager.matchResultAssign(match);

        //Then
        assertEquals("home", result);
    }

    @Test
    void testRemoveEmptyPredictions() {
        //Given
        Map<Long, String> predictionList = new HashMap<>();
        predictionList.put(12545L, "away");
        predictionList.put(24455L, "");

        //When
        Map<Long, String> filteredList = manager.removeEmptyPredictions(predictionList);

        //Then
        assertEquals(1, filteredList.size());
    }
}
