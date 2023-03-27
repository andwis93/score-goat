package com.restapi.scoregoat.mapper;

import com.restapi.scoregoat.domain.Match;
import com.restapi.scoregoat.domain.client.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MatchMapperTests {
    @Autowired
    private MatchMapper mapper;

    @Test
    void testMapToList() {
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
        List<Match> matchList =  mapper.mapToList(fixturesList.getFixtureList());

        //Then
        assertEquals(1, matchList.size());
    }
}
