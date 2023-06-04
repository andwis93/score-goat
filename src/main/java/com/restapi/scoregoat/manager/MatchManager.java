package com.restapi.scoregoat.manager;

import com.restapi.scoregoat.domain.Match;
import com.restapi.scoregoat.domain.MatchResultType;
import com.restapi.scoregoat.domain.MatchStatusType;
import org.springframework.stereotype.Service;

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
}
