package com.restapi.scoregoat.manager;

import com.restapi.scoregoat.domain.Match;
import com.restapi.scoregoat.domain.MatchPrediction;
import com.restapi.scoregoat.domain.Points;
import com.restapi.scoregoat.domain.Result;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class MatchManager {

    public String matchResultAssign(Match match) {
        if (match.isHomeWinner() && !match.isAwayWinner()) {
            return Result.HOME.getResult();
        } else {
            if(!match.isHomeWinner() && match.isAwayWinner()) {
                return Result.AWAY.getResult();
            } else {
                return Result.DRAW.getResult();
            }
        }
    }

    public Map<Long, String> removeEmptyPredictions(Map<Long, String> predictions) {
        Map<Long, String> filteredList = new HashMap<>();
        predictions.entrySet().stream().filter(match -> !match.getValue().isEmpty()).forEach(entry -> filteredList.put(entry.getKey(), entry.getValue()));
        return filteredList;
    }

    public int matchPointsAssign(MatchPrediction prediction) {
        if (prediction.getPrediction().equals(prediction.getResult())) {
            return Points.WIN.getPoints();
        } else {
            return Points.LOSS.getPoints();
        }
    }
}
