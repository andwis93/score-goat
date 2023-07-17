package com.restapi.scoregoat.manager;

import com.restapi.scoregoat.domain.RankStatus;
import com.restapi.scoregoat.domain.Ranking;
import com.restapi.scoregoat.domain.RankingNorm;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Service
public class RankManager {
    private final SortManager manager;
    public RankingNorm setRankingNorms(List<Ranking> rankingList) {
        manager.sortListByRank(rankingList);
        int last;
        double middle;
        middle = ((double) (rankingList.get(rankingList.size() - 1).getRank()) / 2) + .5;
        last = Collections.max(rankingList.stream().map(Ranking::getRank).toList());
        return new RankingNorm(middle,last);
    }

    public int setRankingStatus (RankingNorm norm, Ranking ranking) {
        if (ranking.getRank() == 1) {
            return RankStatus.FIRST.getStatus();
        } else {
            if (ranking.getRank() == norm.getMiddle()) {
                return RankStatus.MIDDLE.getStatus();
            } else {
                if (ranking.getRank() == norm.getLast()) {
                    return RankStatus.LAST.getStatus();
                } else {
                    if (ranking.getRank() < norm.getMiddle()) {
                        return RankStatus.ABOVE.getStatus();
                    } else {
                        return RankStatus.UNDER.getStatus();
                    }
                }
            }
        }
    }
}

