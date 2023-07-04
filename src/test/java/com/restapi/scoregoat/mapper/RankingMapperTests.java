package com.restapi.scoregoat.mapper;

import com.restapi.scoregoat.config.SeasonConfig;
import com.restapi.scoregoat.domain.Ranking;
import com.restapi.scoregoat.domain.RankingDto;
import com.restapi.scoregoat.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RankingMapperTests {
    @Autowired
    private RankingMapper mapper;

    @Test
    void testMapRankingToRankingDto() {
        //Given
        User user = new User("Name1","Email1@test.com", "Password1");
        Ranking ranking = new Ranking(SeasonConfig.DEFAULT_LEAGUE.getId(), user);
        ranking.setRank(7);
        ranking.setPoints(45);
        ranking.setStatus(2);

        //When
        RankingDto rankingDto = mapper.mapRankingToRankingDto(ranking);

        //Then
        assertEquals(7, rankingDto.getRank());
        assertEquals("Name1", rankingDto.getUserName());
        assertEquals(45, rankingDto.getPoints());
        assertEquals(2, rankingDto.getStatus());
    }

    @Test
    void testMapRankingToRankingDtoList() {
        //Given
        User user = new User("Name1","Email1@test.com", "Password1");
        Ranking ranking = new Ranking(SeasonConfig.DEFAULT_LEAGUE.getId(), user);
        ranking.setRank(7);
        ranking.setPoints(45);
        ranking.setStatus(2);
        List<Ranking> rankingList = new ArrayList<>();
        rankingList.add(ranking);

        //When
        List<RankingDto> rankingDtoList = mapper.mapRankingToRankingDtoList(rankingList);

        //Then
        assertEquals(1, rankingDtoList.size());
        assertEquals("Name1", rankingDtoList.get(0).getUserName());
    }
}
