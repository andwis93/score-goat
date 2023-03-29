package com.restapi.scoregoat.mapper;

import com.restapi.scoregoat.domain.Match;
import com.restapi.scoregoat.domain.MatchDto;
import com.restapi.scoregoat.domain.client.mapJSON.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MatchMapperTests {
    @Autowired
    private MatchMapper mapper;

    @Test
    void testMapFixtureRespondToMatchList() {
        //Given
        Team homeTeam = new Team("LiverPool", "http://Logo.png", true);
        Team awayTeam = new Team("Everton", "http://Logo2.png", false);
        Status status = new Status("Not Started", null);
        Fixture fixture = new Fixture(39L,"2023-04-01T14:00:00+00:00", status);
        League league = new League(39, "Premier League", "http://PremierLeague.png" );
        Teams teams = new Teams(homeTeam, awayTeam);
        Goals goals = new Goals(2,1);
        FixtureRespond fixtureRespond = new FixtureRespond(fixture, league, teams, goals);
        FixturesList fixturesList = new FixturesList();
        fixturesList.getFixtureList().add(fixtureRespond);

        //When
        List<Match> matchList =  mapper.mapFixtureRespondToMatchList(fixturesList.getFixtureList());

        //Then
        assertEquals(1, matchList.size());
    }

    @Test
    void testMapMatchToMatchDtoList() {
        //Given
        Match match = new Match(1L, 39, 365L, OffsetDateTime.parse("2023-04-01T14:00:00+00:00"),
                "Not Started", "81:48", "Liverpool", "Liverpool.logo",
                true, "Everton", "Everton.logo", false, 2, 1);
        List<Match> matchList = new ArrayList<>();
        matchList.add(match);
        //When
        List<MatchDto> matchListDto =  mapper.mapMatchToMatchDtoList(matchList);

        //Then
        assertEquals(1, matchListDto.size());
        assertEquals("Liverpool", matchListDto.get(0).getHomeTeam());
    }
}
