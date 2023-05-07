package com.restapi.scoregoat.service;

import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.repository.MatchRepository;
import com.restapi.scoregoat.repository.MatchPredictionRepository;
import com.restapi.scoregoat.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
@EnableAspectJAutoProxy
public class MatchPredictionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MatchPredictionService.class);
    private MatchPredictionRepository repository;
    private UserRepository userRepository;
    private MatchRepository matchRepository;
    private LogDataService logDataService;

    public Match findMatch(Long fixtureId) {
        if (matchRepository.existsByFixtureId(fixtureId)) {
            return matchRepository.findByFixtureId(fixtureId).orElseThrow(NoSuchElementException::new);
        } else {
            return null;
        }
    }

    public NotificationRespondDto savePredictions(PredictionDto predictionDto) {
        if (userRepository.existsById(predictionDto.getUserId())) {
            User user = userRepository.findById(predictionDto.getUserId()).orElseThrow(NoSuchElementException::new);
            for (Map.Entry<Long, String> match : predictionDto.getMatchSelections().entrySet()) {
                if (!repository.existsMatchPredictionByUserIdAndFixtureId(user.getId(), match.getKey())) {
                    try {
                        Match theMatch = findMatch(match.getKey());
                        MatchPrediction prediction = new MatchPrediction();
                        prediction.setLeagueId(theMatch.getLeagueId());
                        prediction.setUser(user);
                        prediction.setFixtureId(theMatch.getFixtureId());
                        prediction.setPrediction(match.getValue());

                        repository.save(prediction);
                        user.getMatchPredictions().add(prediction);
                        userRepository.save(user);
                    } catch (NoSuchElementException ex) {
                        String message = ex.getMessage() + "  --ERROR: Couldn't execute \"savePredictions\"-- ";
                        logDataService.saveLog(new LogData(null, "With matchID: " +
                                match.getKey(), Code.COULD_NOT_EXECUTE_PREDICTION.getCode(), message));
                        LOGGER.error(message, ex);
                        return new NotificationRespondDto(message, NotificationType.ERROR.getType());
                    }
                }
            }
            return new NotificationRespondDto(Respond.PREDICTIONS_SAVE_OK.getRespond(), NotificationType.SUCCESS.getType());
        } else {
            return new NotificationRespondDto(Respond.USER_ID_INCORRECT.getRespond(), NotificationType.ERROR.getType());
        }
    }

    public List<UserPredictionDto> getMatchPredictions(Long userId, int leagueId) {
        List<MatchPrediction> predictions = repository.findAllByUserIdAndLeagueId(userId, leagueId);
        List<UserPredictionDto> userPredictionsDto = new ArrayList<>();
        for (MatchPrediction prediction: predictions) {
              Match match = findMatch(prediction.getFixtureId());
              if (match != null) {
                  userPredictionsDto.add(new UserPredictionDto(match.getHomeLogo(), match.getHomeTeam(), match.getHomeGoals(),
                          match.getDate().toLocalDate().toString(), match.getDate().toLocalTime().toString(), match.getAwayGoals(),
                          match.getAwayTeam(), match.getAwayLogo(), prediction.getPrediction(), prediction.getResult()));
              }
        }
        return userPredictionsDto;
    }
}