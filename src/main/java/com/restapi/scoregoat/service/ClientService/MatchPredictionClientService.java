package com.restapi.scoregoat.service.ClientService;

import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.manager.MatchManager;
import com.restapi.scoregoat.service.DBService.LogDataDBService;
import com.restapi.scoregoat.service.DBService.MatchDBService;
import com.restapi.scoregoat.service.DBService.MatchPredictionDBService;
import com.restapi.scoregoat.service.DBService.UserDBService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import java.util.*;

@AllArgsConstructor
@Service
@EnableAspectJAutoProxy
public class MatchPredictionClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MatchPredictionClientService.class);
    private final MatchPredictionDBService service;
    private final UserDBService userService;
    private final MatchDBService matchService;
    private final LogDataDBService logDataService;
    private final GraduationClientService graduationService;
    private final MatchManager manager;

    public NotificationRespondDto savePredictions(PredictionDto predictionDto) {
        if (userService.existsById(predictionDto.getUserId())) {
            User user = userService.findById(predictionDto.getUserId());
            for (Map.Entry<Long, String> match : predictionDto.getMatchSelections().entrySet()) {
                if (!service.existsMatchPredictionByUserIdAndFixtureId(user.getId(), match.getKey())) {
                    try {
                        Match theMatch = matchService.findMatchByFixture(match.getKey());
                        MatchPrediction prediction = new MatchPrediction();
                        prediction.setLeagueId(theMatch.getLeagueId());
                        prediction.setUser(user);
                        prediction.setFixtureId(theMatch.getFixtureId());
                        prediction.setPrediction(match.getValue());
                        prediction.setResult(Result.UNSET.getResult());

                        service.save(prediction);

                        user.addPrediction(prediction);
                        userService.save(user);

                    } catch (NoSuchElementException ex) {
                        String message = ex.getMessage() + "  --ERROR: Couldn't execute \"savePredictions\"-- ";
                        logDataService.saveLog(new LogData(null, "With matchID: " +
                                match.getKey(), Code.EXECUTE_PREDICTION_ERROR.getCode(), message));
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
        List<MatchPrediction> predictions = service.findByUserIdAndLeagueId(userId, leagueId);
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
        service.findAllByResult(Result.UNSET.getResult()).forEach(unset -> {
            Match match = matchService.findMatchByFixture(unset.getFixtureId());
            if (match.getStatus().equals(MatchStatusType.FINISHED.getType())) {
                unset.setResult(manager.matchResultAssign(match));
                service.save(unset);
            }
        });
    }

    public void graduationExecution(MatchPrediction prediction) {
        try {
            prediction.setPoints(manager.matchPointsAssign(prediction));
            service.save(prediction);
            graduationService.graduationUpdate(prediction);
        } catch (Exception ex) {
            String message = ex.getMessage() + " --ERROR: Couldn't execute graduation-- ";
            logDataService.saveLog(new LogData(null,"Prediction ID: "
                    + prediction.getId(), Code.EXECUTION_GRADUATION_ERROR.getCode(), message));
            LOGGER.error(message,ex);
        }
    }

    public long  assignPoints() {
        List<MatchPrediction> predictions = service.findAllByPoints(Points.NEUTRAL.getPoints());
        long count = 0;
        for(MatchPrediction thePrediction:predictions) {
            graduationExecution(thePrediction);
            count++;
        }
        return count;
    }
}