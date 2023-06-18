package com.restapi.scoregoat.manager;

import com.restapi.scoregoat.config.SeasonConfig;
import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.repository.GraduationRepository;
import com.restapi.scoregoat.service.DBService.UserDBService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GraduationUpdateTests {
    @InjectMocks
    private GraduationManager manager;
    @Mock
    private GraduationRepository repository;
    @Mock
    private UserDBService service;

    @Test
    void testGraduateUpdate() {
        //Given
        Map<Long, String> list = new HashMap<>();
        list.put(333L, Result.HOME.getResult());
        list.put(327L, Result.AWAY.getResult());

        User user = new User("Name1","Email1@test.com", "Password1");
        user.setId(202L);

        Match match = new Match(327L, SeasonConfig.DEFAULT_LEAGUE.getId(), 365L, OffsetDateTime.parse("2023-04-01T14:00:00+00:00"),
                "Not Started", "81:48", "Liverpool", "Liverpool.logo",
                true, "Everton", "Everton.logo", false, 2, 1);

        MatchPrediction prediction = new MatchPrediction(22L, SeasonConfig.DEFAULT_LEAGUE.getId(), list.get(327L),
                user,match.getFixtureId(),-1,Result.HOME.getResult());

        Graduation graduation = new Graduation(prediction.getLeagueId(), user);

        when(service.findById(202L)).thenReturn(user);
        when(repository.existsGraduationByLeagueAndUserId(prediction.getLeagueId(), 202L)).thenReturn(true);
        when(repository.findByLeagueAndUserId(prediction.getLeagueId(), 202L)).thenReturn(graduation);

        //When
        manager.graduationUpdate(prediction);

        //Then
        assertEquals(-1, graduation.getPoints());
    }
}
