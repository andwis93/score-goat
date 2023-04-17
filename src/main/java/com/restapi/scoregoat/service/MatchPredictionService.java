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

    public Match findMatch(Long matchId) {
        if (matchRepository.existsById(matchId)) {
            return matchRepository.findById(matchId).orElseThrow(NoSuchElementException::new);
        } else {
            return null;
        }
    }

    public String savePredictions(PredictionDto predictionDto) {
        if (userRepository.existsById(predictionDto.getUserId())) {
            User user = userRepository.findById(predictionDto.getUserId()).orElseThrow(NoSuchElementException::new);
            for (MatchSelection match : predictionDto.getMatchSelections()) {
                if (!repository.existsMatchPredictionByUserIdAndMatchId(user.getId(), match.getMatchId())) {
                    try {
                        Match theMatch = findMatch(match.getMatchId());
                        MatchPrediction prediction = new MatchPrediction();
                        prediction.setUser(user);
                        prediction.setMatch(theMatch);
                        prediction.setWhoWin(match.getWhoWin());
                        repository.save(prediction);
                        user.getMatchPredictions().add(prediction);
                        theMatch.getMatchPredictions().add(prediction);
                        userRepository.save(user);
                        matchRepository.save(theMatch);
                    } catch (NoSuchElementException ex) {
                        String message = ex.getMessage() + "  --ERROR: Couldn't execute \"savePredictions\"-- ";
                        logDataService.saveLog(new LogData(null, "With matchID: " +
                                match.getMatchId(), Code.COULD_NOT_EXECUTE_PREDICTION.getCode(), message));
                        LOGGER.error(message, ex);
                        return message;
                    }
                }
            }
            return Respond.PREDICTIONS_SAVE_OK.getRespond();
        } else {
            return Respond.USER_ID_INCORRECT.getRespond();
        }
    }
}