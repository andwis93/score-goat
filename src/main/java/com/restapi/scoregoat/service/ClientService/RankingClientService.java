package com.restapi.scoregoat.service.ClientService;

import com.restapi.scoregoat.config.LeaguesListConfig;
import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.manager.RankManager;
import com.restapi.scoregoat.manager.SortManager;
import com.restapi.scoregoat.mapper.RankingMapper;
import com.restapi.scoregoat.service.DBService.RankingDBService;
import com.restapi.scoregoat.service.DBService.UserDBService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
@EnableAspectJAutoProxy
public class RankingClientService {
    private final LeaguesListConfig config;
    private final RankingDBService service;
    private final UserDBService userService;
    private final SortManager manager;
    private final RankManager rankManager;
    private final RankingMapper mapper;

    public void rankingUpdate(MatchPrediction prediction) {
        Ranking ranking;
        User user = userService.findById(prediction.getUser().getId());
        if (user != null) {
            if (service.existsRankingByLeagueAndUserId(prediction.getLeagueId(), user.getId())) {
                ranking = service.findByLeagueAndUserId(prediction.getLeagueId(), user.getId());
            } else {
                ranking = new Ranking(prediction.getLeagueId(), user);
            }
            ranking.addPoints(prediction.getPoints());
            service.save(ranking);
        } else {
            throw new NullPointerException();
        }
    }

    public void executeRankAssign() {
        Map<Integer, String> leagueList = config.getLeagueList();
        leagueList.keySet().forEach(leagueId -> {
            List<Ranking> rankingList = manager.sortListByPoints(service.findByLeagueId(leagueId));
            rankAssign(rankingList);
            service.saveAll(rankingList);
        });
    }

    public List<RankingDto> fetchRankingListByLeagueId(int leagueId) {
        List<Ranking> rankings = manager.sortListByRank(service.findByLeagueId(leagueId));
        return mapper.mapRankingToRankingDtoList(rankings);
    }

    private void rankAssign(List<Ranking> rankingList) {
        int rank = 1;
        rankingList.get(0).setRank(rank);
        for(int i = 1; i < rankingList.size(); i++) {
            if (rankingList.get(i).getPoints() != rankingList.get(i - 1).getPoints()) {
                rank++;
            }
            rankingList.get(i).setRank(rank);
        }
        statusAssign(rankingList);
    }

    private void statusAssign(List<Ranking> rankingList) {
        RankingNorm norm = rankManager.setRankingNorms(rankingList);
        rankingList.forEach(ranking -> {
            ranking.setStatus(rankManager.setRankingStatus(norm, ranking));
        });
    }

    public UserRankDto fetchRankingByUserIdAndLeagueId(Long userId, int leagueId) {
        RankingDto rankingDto = mapper.mapRankingToRankingDto(service.findByUserIdAndLeagueId(userId, leagueId));
        int rankingSize = service.getRankingSizeByLeagueId(leagueId);
        return new UserRankDto(rankingDto, rankingSize) ;
    }
}
