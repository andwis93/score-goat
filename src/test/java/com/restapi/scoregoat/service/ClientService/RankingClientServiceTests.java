package com.restapi.scoregoat.service.ClientService;

import com.restapi.scoregoat.config.LeaguesListConfig;
import com.restapi.scoregoat.config.SeasonConfig;
import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.manager.RankManager;
import com.restapi.scoregoat.manager.SortManager;
import com.restapi.scoregoat.mapper.RankingMapper;
import com.restapi.scoregoat.service.DBService.RankingDBService;
import com.restapi.scoregoat.service.DBService.UserDBService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RankingClientServiceTests {
    @InjectMocks
    private RankingClientService service;
    @Mock
    private LeaguesListConfig config;
    @Mock
    private RankingDBService dbService;
    @Mock
    private UserDBService userService;
    @Mock
    private SortManager sortManager;
    @Mock
    private RankManager rankManager;
    @Mock
    private RankingMapper mapper;

    @Test
    void testRankingUpdate() {
        //Given
        Map<Long, String> list = new HashMap<>();
        list.put(333L, Result.HOME.getResult());
        list.put(327L, Result.AWAY.getResult());

        User user = new User("Name1","Email1@test.com", "Password1");
        user.setId(202L);

        Match match = new Match(327L, SeasonConfig.DEFAULT_LEAGUE.getId(), 365L, OffsetDateTime.parse("2023-04-01T14:00:00+00:00"),
                "Not Started", "81:48", "Liverpool", "Liverpool.logo",
                true, "Everton", "Everton.logo", false, 2, 1);

        MatchPrediction prediction = new MatchPrediction(22L, SeasonConfig.DEFAULT_LEAGUE.getId(), list.get(327L),
                user,match.getFixtureId(),-1,Result.HOME.getResult());

        Ranking ranking = new Ranking(prediction.getLeagueId(), user);

        when(userService.findById(202L)).thenReturn(user);
        when(dbService.existsRankingByLeagueAndUserId(prediction.getLeagueId(), 202L)).thenReturn(true);
        when(dbService.findByLeagueAndUserId(prediction.getLeagueId(), 202L)).thenReturn(ranking);

        //When
        service.rankingUpdate(prediction);

        //Then
        assertEquals(-1, ranking.getPoints());
    }

    @Test
    void testExecuteRankAssign() {
        //Give
        User user1 = new User("Name1","Email1@test.com", "Password1");
        user1.setId(1L);
        Ranking ranking1 = new Ranking(200L, -2, 0, 39, user1, 1);

        User user2 = new User("Name2","Email2@test.com", "Password2");
        user2.setId(1L);
        Ranking ranking2 = new Ranking(100L, 14, 0, 39, user2, 2);

        User user3 = new User("Name3","Email3@test.com", "Password3");
        user3.setId(1L);
        Ranking ranking3 = new Ranking(300L, -2, 0, 39, user3, 4);

        User user4 = new User("Name4","Email4@test.com", "Password4");
        user4.setId(1L);
        Ranking ranking4 = new Ranking(400L, 8, 0, 39, user4, 3);

        Map<Integer, String> leagues = new HashMap<>();
        leagues.put(39, "Premier League");

        RankingNorm norm = new RankingNorm(2.5, 4);

        List<Ranking> rankings = new ArrayList<>();
        rankings.add(ranking1);
        rankings.add(ranking2);
        rankings.add(ranking3);
        rankings.add(ranking4);

        when(config.getLeagueList()).thenReturn(leagues);
        when(dbService.findByLeagueId(39)).thenReturn(rankings);
        when(rankManager.setRankingNorms(any())).thenReturn(norm);
        when(rankManager.setRankingStatus(any(),any())).thenCallRealMethod();
        when(sortManager.sortListByPoints(any())).thenCallRealMethod();

        //When
        service.executeRankAssign();

        //Then
        assertEquals(2, rankings.get(1).getRank());
    }

    @Test
    void testFetchRankingListByLeagueId() {
        //Give
        User user1 = new User("Name1","Email1@test.com", "Password1");
        user1.setId(1L);
        Ranking ranking1 = new Ranking(200L, -2, 3, 39, user1,1);
        RankingDto rankingDto1 = new RankingDto("3", "Name1", "-2", 5);

        User user2 = new User("Name2","Email2@test.com", "Password2");
        user2.setId(1L);
        Ranking ranking2 = new Ranking(100L, 14, 1, 39, user2, 2);
        RankingDto rankingDto2 = new RankingDto("1", "Name2", "14", 1);

        User user3 = new User("Name3","Email3@test.com", "Password3");
        user3.setId(1L);
        Ranking ranking3 = new Ranking(300L, -2, 3, 39, user3, 4);
        RankingDto rankingDto3 = new RankingDto("3", "Name3", "-2", 5);

        User user4 = new User("Name4","Email4@test.com", "Password4");
        user4.setId(1L);
        Ranking ranking4 = new Ranking(400L, 8, 2, 39, user4, 3);
        RankingDto rankingDto4 = new RankingDto("2", "Name4", "8", 3);

        List<Ranking> rankings = new ArrayList<>();
        rankings.add(ranking1);
        rankings.add(ranking2);
        rankings.add(ranking3);
        rankings.add(ranking4);

        List<RankingDto> rankingDtoList = new ArrayList<>();
        rankingDtoList.add(rankingDto2);
        rankingDtoList.add(rankingDto4);
        rankingDtoList.add(rankingDto1);
        rankingDtoList.add(rankingDto3);

        when(dbService.findByLeagueId(39)).thenReturn(rankings);
        when(sortManager.sortListByRank(any())).thenCallRealMethod();
        when(mapper.mapRankingToRankingDtoList(any())).thenReturn(rankingDtoList);

        //When
        List<RankingDto> theRankingDtoList = service.fetchRankingListByLeagueId(SeasonConfig.DEFAULT_LEAGUE.getId());

        //Then
        assertEquals(4, theRankingDtoList.size());
        assertEquals("3", theRankingDtoList.get(2).getRank());
    }
}
