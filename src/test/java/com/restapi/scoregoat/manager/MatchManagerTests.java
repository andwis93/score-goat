package com.restapi.scoregoat.manager;

import com.restapi.scoregoat.config.SeasonConfig;
import com.restapi.scoregoat.domain.*;
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

    @Test
    void testMatchPointsAssign() {
        //Given
        Match match = new Match(327L, SeasonConfig.DEFAULT_LEAGUE.getId(), 365L,
                OffsetDateTime.parse("2023-04-01T14:00:00+00:00"), MatchStatusType.FINISHED.getType(),
                "81:48", "Liverpool", "Liverpool.logo", true, "Everton",
                "Everton.logo", false, 2, 1);

        Map<Long, String> list = new HashMap<>();
        list.put(333L, Result.HOME.getResult());

        User user = new User("Name1","Email1@test.com", "Password1");
        user.setId(202L);

        MatchPrediction prediction = new MatchPrediction(22L, SeasonConfig.DEFAULT_LEAGUE.getId(), list.get(333L),
                user,match.getFixtureId(),0, Result.HOME.getResult());

        //When
        int points = manager.matchPointsAssign(prediction);

        //Then
        assertEquals(3, points);
    }
}
