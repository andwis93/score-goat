package com.restapi.scoregoat.service;

import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.manager.MatchManager;
import com.restapi.scoregoat.repository.MatchPredictionRepository;
import com.restapi.scoregoat.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import java.util.*;

@AllArgsConstructor
@Service
@EnableAspectJAutoProxy
public class MatchPredictionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MatchPredictionService.class);
    private MatchPredictionRepository repository;
    private UserRepository userRepository;
    private MatchManager manager;
   // private MatchRepository matchRepository;
    private MatchService matchService;
    private LogDataService logDataService;

//    public Match findMatchByFixture(Long fixtureId) {
//        if (matchRepository.existsByFixtureId(fixtureId)) {
//            return matchRepository.findByFixtureId(fixtureId).orElseThrow(NoSuchElementException::new);
//        } else {
//            return null;
//        }
//    }

    public List<MatchPrediction> findPredictionsByResult(int result) {
        return repository.findAllByResult(result);
    }

    public NotificationRespondDto savePredictions(PredictionDto predictionDto) {
        if (userRepository.existsById(predictionDto.getUserId())) {
            User user = userRepository.findById(predictionDto.getUserId()).orElseThrow(NoSuchElementException::new);
            for (Map.Entry<Long, String> match : predictionDto.getMatchSelections().entrySet()) {
                if (!repository.existsMatchPredictionByUserIdAndFixtureId(user.getId(), match.getKey())) {
                    try {
                        Match theMatch = matchService.findMatchByFixture(match.getKey());
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
        List<MatchPrediction> predictions = repository.findByUserIdAndLeagueId(userId, leagueId);
        List<UserPredictionDto> userPredictionsDto = new ArrayList<>();
        for (MatchPrediction prediction: predictions) {
            Match match = matchService.findMatchByFixture(prediction.getFixtureId());
            if (match != null) {
                userPredictionsDto.add(new UserPredictionDto(match.getHomeLogo(), match.getHomeTeam(), match.getHomeGoals(),
                        match.getDate().toLocalDate().toString(), match.getDate().toLocalTime().toString(), match.getAwayGoals(),
                        match.getAwayTeam(), match.getAwayLogo(), prediction.getPrediction(), prediction.getPoints(), prediction.getResult()));
            }
        }
        return sortList(userPredictionsDto);
    }

    private List<UserPredictionDto> sortList(List<UserPredictionDto> list) {
        list.sort(Comparator.comparing(UserPredictionDto::getDate).reversed());
        return list;
    }

    public void graduatePredictions() {
    findPredictionsByResult(MatchResultType.UNSET.getResult()).forEach(unset -> {
            Match match = matchService.findMatchByFixture(unset.getFixtureId());
            if (match.getStatus().equals(MatchStatusType.FINISHED.getType())) {
                unset.setResult(manager.matchResultAssign(match));
                repository.save(unset);
            }
        });
    }
}