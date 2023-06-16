package com.restapi.scoregoat.service;

import com.restapi.scoregoat.config.SeasonConfig;
import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.manager.MatchManager;
import com.restapi.scoregoat.repository.MatchPredictionRepository;
import com.restapi.scoregoat.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.OffsetDateTime;
import java.util.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MatchPredictionServiceTests {
    @InjectMocks
    private MatchPredictionService service;
    @Mock
    private MatchService matchService;
    @Mock
    private MatchPredictionRepository repository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private MatchManager manager;

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

        MatchPrediction prediction = new MatchPrediction(22L, SeasonConfig.DEFAULT_LEAGUE.getId(), list.get(327L),
                user,match.getFixtureId(),-1,Result.HOME.getResult());

        when(userRepository.existsById(202L)).thenReturn(true);
        when(userRepository.findById(202L)).thenReturn(Optional.of(user));
        when(repository.existsMatchPredictionByUserIdAndFixtureId(202L,333L)).thenReturn(true);
        when(repository.existsMatchPredictionByUserIdAndFixtureId(202L,327L)).thenReturn(false);
        when(matchService.findMatchByFixture(327L)).thenReturn(match);
        when(repository.save(any(MatchPrediction.class))).thenReturn(prediction);
        when(userRepository.save(user)).thenReturn(user);

        //When
        NotificationRespondDto respondDto = service.savePredictions(predictionDto);

        //Then
        assertEquals(Respond.PREDICTIONS_SAVE_OK.getRespond(), respondDto.getMessage());
        assertEquals(NotificationType.SUCCESS.getType(), respondDto.getType());
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

        when(repository.findAllByResult(Result.UNSET.getResult())).thenReturn(predictionList);
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


        when(repository.findAllByPoints(Points.NEUTRAL.getPoints())).thenReturn(predictions);

        //When
        long counter = service.assignPoints();

        //Then
        assertEquals(1, counter);
    }
}
