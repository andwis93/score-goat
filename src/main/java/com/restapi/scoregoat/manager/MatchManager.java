package com.restapi.scoregoat.manager;

import com.restapi.scoregoat.domain.Match;
import com.restapi.scoregoat.domain.MatchResultType;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class MatchManager {

    public int matchResultAssign(Match match) {
        if (match.isHomeWinner() && !match.isAwayWinner()) {
            return MatchResultType.HOME.getResult();
        } else {
            if(!match.isHomeWinner() && match.isAwayWinner()) {
                return MatchResultType.AWAY.getResult();
            } else {
                return MatchResultType.DRAW.getResult();
            }
        }
    }
    public Map<Long, String> removeEmptyPredictions(Map<Long, String> predictions) {
        Map<Long, String> filteredList = new HashMap<>();
        predictions.entrySet().stream().filter(match -> !match.getValue().isEmpty()).forEach(entry -> filteredList.put(entry.getKey(), entry.getValue()));
        return filteredList;
    }
}
