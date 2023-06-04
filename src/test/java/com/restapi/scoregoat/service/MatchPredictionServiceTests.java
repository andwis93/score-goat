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
    private MatchManager manager;
    @Mock
    private UserRepository userRepository;



    @Test
    void testSavePredictions() {
        //Given
        Map<Long, String> list = new HashMap<>();
        list.put(333L, Prediction.HOME.getResult());
        list.put(327L, Prediction.AWAY.getResult());

        PredictionDto predictionDto = new PredictionDto(202L, SeasonConfig.DEFAULT_LEAGUE.getId(), list);

        User user = new User("Name1","Email1@test.com", "Password1");
        user.setId(202L);

        Match match2 = new Match(327L, SeasonConfig.DEFAULT_LEAGUE.getId(), 365L, OffsetDateTime.parse("2023-04-01T14:00:00+00:00"),
                "Not Started", "81:48", "Liverpool", "Liverpool.logo",
                true, "Everton", "Everton.logo", false, 2, 1);

        MatchPrediction prediction = new MatchPrediction(22L, SeasonConfig.DEFAULT_LEAGUE.getId(), list.get(327L), user,match2.getFixtureId(),-1, -1);

        when(userRepository.existsById(202L)).thenReturn(true);
        when(userRepository.findById(202L)).thenReturn(Optional.of(user));
        when(repository.existsMatchPredictionByUserIdAndFixtureId(202L,333L)).thenReturn(true);
        when(repository.existsMatchPredictionByUserIdAndFixtureId(202L,327L)).thenReturn(false);
        when(matchService.findMatchByFixture(327L)).thenReturn(match2);
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
        list.put(333L, Prediction.HOME.getResult());
        list.put(327L, Prediction.AWAY.getResult());

        User user = new User("Name1","Email1@test.com", "Password1");
        user.setId(202L);

        MatchPrediction prediction = new MatchPrediction(22L, SeasonConfig.DEFAULT_LEAGUE.getId(), list.get(327L),
                user,match.getFixtureId(),0, MatchResultType.UNSET.getResult());
        List<MatchPrediction> predictionList = new ArrayList<>();
        predictionList.add(prediction);

        when(repository.findAllByResult(0)).thenReturn(predictionList);
        when(matchService.findMatchByFixture(365L)).thenReturn(match);
        when(manager.matchResultAssign(match)).thenReturn(MatchResultType.HOME.getResult());

        //When
        service.graduatePredictions();

        //Then
        assertEquals(1, predictionList.get(0).getResult());
    }
}
