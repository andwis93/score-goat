package com.restapi.scoregoat.service.ClientService;

import com.restapi.scoregoat.config.LeaguesListConfig;
import com.restapi.scoregoat.config.SeasonConfig;
import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.service.DBService.GraduationDBService;
import com.restapi.scoregoat.service.DBService.UserDBService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GraduationClientServiceTests {

    @InjectMocks
    private GraduationClientService service;
    @Mock
    private LeaguesListConfig config;
    @Mock
    private GraduationDBService dbService;
    @Mock
    private UserDBService userService;

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

        when(userService.findById(202L)).thenReturn(user);
        when(dbService.existsGraduationByLeagueAndUserId(prediction.getLeagueId(), 202L)).thenReturn(true);
        when(dbService.findByLeagueAndUserId(prediction.getLeagueId(), 202L)).thenReturn(graduation);

        //When
        service.graduationUpdate(prediction);

        //Then
        assertEquals(-1, graduation.getPoints());
    }

    @Test
    void testAssignRank() {
        //Give
        User user1 = new User("Name1","Email1@test.com", "Password1");
        user1.setId(1L);
        Graduation graduation1 = new Graduation(200L, -2, 0, 39, user1);

        User user2 = new User("Name2","Email2@test.com", "Password2");
        user2.setId(1L);
        Graduation graduation2 = new Graduation(100L, 14, 0, 39, user2);

        User user3 = new User("Name3","Email3@test.com", "Password3");
        user3.setId(1L);
        Graduation graduation3 = new Graduation(300L, -2, 0, 39, user3);

        User user4 = new User("Name4","Email4@test.com", "Password4");
        user4.setId(1L);
        Graduation graduation4 = new Graduation(400L, 8, 0, 39, user4);

        Map<Integer, String> leagues = new HashMap<>();
        leagues.put(39, "Premier League");

        List<Graduation> graduations = new ArrayList<>();
        graduations.add(graduation1);
        graduations.add(graduation2);
        graduations.add(graduation3);
        graduations.add(graduation4);

        when(config.getLeagueList()).thenReturn(leagues);
        when(dbService.findByLeagueId(39)).thenReturn(graduations);

        //When
        service.executeRankAssign();

        //Then
        assertEquals(2, graduations.get(1).getRank());
    }
}
