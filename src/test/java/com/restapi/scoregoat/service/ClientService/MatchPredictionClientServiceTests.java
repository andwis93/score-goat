package com.restapi.scoregoat.service.ClientService;

import com.restapi.scoregoat.config.SeasonConfig;
import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.manager.MatchManager;
import com.restapi.scoregoat.service.DBService.MatchDBService;
import com.restapi.scoregoat.service.DBService.MatchPredictionDBService;
import com.restapi.scoregoat.service.DBService.UserDBService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.OffsetDateTime;
import java.util.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MatchPredictionClientServiceTests {
    @InjectMocks
    private MatchPredictionClientService service;
    @Mock
    private MatchDBService matchService;
    @Mock
    private MatchPredictionDBService predictionDBService;
    @Mock
    private UserDBService userDBService;
    @Mock
    private MatchManager manager;
    @Mock
    private GraduationClientService graduationService;

    @Test
    void testSavePredictions() {
        //Given
        Map<Long, String> list = new HashMap<>();
        list.put(333L, Result.HOME.getResult());
        list.put(327L, Result.AWAY.getResult());

        PredictionDto predictionDto = new PredictionDto(202L, SeasonConfig.DEFAULT_LEAGUE.getId(), list);

        User user = new User("Name1","Email1@test.com", "Password1");
        user.setId(202L);

        Match match = new Match(327L, SeasonConfig.DEFAULT_LEAGUE.getId(), 365L, OffsetDateTime.parse("2023-04-01T14:00:00+00:00"),
                "Not Started", "81:48", "Liverpool", "Liverpool.logo",
                true, "Everton", "Everton.logo", false, 2, 1);


        when(userDBService.existsById(202L)).thenReturn(true);
        when(userDBService.findById(202L)).thenReturn(user);
        when(predictionDBService.existsMatchPredictionByUserIdAndFixtureId(202L,333L)).thenReturn(true);
        when(predictionDBService.existsMatchPredictionByUserIdAndFixtureId(202L,327L)).thenReturn(false);
        when(matchService.findMatchByFixture(327L)).thenReturn(match);
        when(userDBService.save(user)).thenReturn(user);

        //When
        NotificationRespondDto respondDto = service.savePredictions(predictionDto);

        //Then
        assertEquals(Respond.PREDICTIONS_SAVE_OK.getRespond(), respondDto.getMessage());
        assertEquals(NotificationType.SUCCESS.getType(), respondDto.getType());
    }

    @Test
    void testGetMatchPrediction() {
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
        List<MatchPrediction> predictions = new ArrayList<>();
        predictions.add(prediction);

        when(predictionDBService.findByUserIdAndLeagueId(202L, SeasonConfig.DEFAULT_LEAGUE.getId())).thenReturn(predictions);
        when(matchService.findMatchByFixture(365L)).thenReturn(match);

        //When
        List<UserPredictionDto> userPredictionDtoList = service.getMatchPredictions(
                202L, SeasonConfig.DEFAULT_LEAGUE.getId());

        //Then
        assertEquals(1,userPredictionDtoList.size());
    }

    @Test
    void testGraduatePredictions() {
        //Given
        Match match = new Match(327L, SeasonConfig.DEFAULT_LEAGUE.getId(), 365L,
                OffsetDateTime.parse("2023-04-01T14:00:00+00:00"), MatchStatusType.FINISHED.getType(),
                "81:48", "Liverpool", "Liverpool.logo", true, "Everton",
                "Everton.logo", false, 2, 1);

        Map<Long, String> list = new HashMap<>();
        list.put(327L, Result.AWAY.getResult());

        User user = new User("Name1","Email1@test.com", "Password1");
        user.setId(202L);

        MatchPrediction prediction = new MatchPrediction(22L, SeasonConfig.DEFAULT_LEAGUE.getId(), list.get(327L),
                user,match.getFixtureId(),0, Result.UNSET.getResult());
        List<MatchPrediction> predictionList = new ArrayList<>();
        predictionList.add(prediction);

        when(predictionDBService.findAllByResult(Result.UNSET.getResult())).thenReturn(predictionList);
        when(matchService.findMatchByFixture(365L)).thenReturn(match);
        when(manager.matchResultAssign(match)).thenReturn(Result.HOME.getResult());

        //When
        service.graduatePredictions();

        //Then
        assertEquals("home", predictionList.get(0).getResult());
    }

    @Test
    void testAssignPoints() {
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
                user,match.getFixtureId(),Points.NEUTRAL.getPoints(), Result.HOME.getResult());

        List<MatchPrediction> predictions = new ArrayList<>();
        predictions.add(prediction);

        when(predictionDBService.findAllByPoints(Points.NEUTRAL.getPoints())).thenReturn(predictions);

        //When
        long counter = service.assignPoints();

        //Then
        assertEquals(1, counter);
    }
}
