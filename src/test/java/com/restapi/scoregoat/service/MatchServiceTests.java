package com.restapi.scoregoat.service;

import com.restapi.scoregoat.client.FootballClient;
import com.restapi.scoregoat.config.SeasonConfig;
import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.domain.client.TimeFrame.TimeFrame;
import com.restapi.scoregoat.domain.client.mapJSON.*;
import com.restapi.scoregoat.mapper.MatchMapper;
import com.restapi.scoregoat.repository.MatchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MatchServiceTests {
    @InjectMocks
    private MatchService service;
    @Mock
    private SeasonService seasonService;
    @Mock
    private FootballClient client;
    @Mock
    private MatchRepository repository;
    @Mock
    private MatchMapper mapper;

    @Test
    void shouldUploadMatches() {
        //Given
        Season season = new Season(1L, "2023");

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

        LocalDate toDate = LocalDate.now().plusDays(TimeFrame.DAYS.getTimeFrame());

        FixtureParam param = new FixtureParam(SeasonConfig.DEFAULT_LEAGUE.getId(),
                season.getYear(), toDate);

        when(client.getFixtures(param)).thenReturn(fixturesList);

        Match match = new Match(1L, 39, 365L, OffsetDateTime.parse("2023-04-01T14:00:00+00:00"),
                "Not Started", "81:48", "Liverpool", "Liverpool.logo",
                true, "Everton", "Everton.logo", false, 2, 1);

        List<Match> matchList = new ArrayList<>();
        matchList.add(match);

        when(mapper.mapFixtureRespondToMatchList(fixturesList.getFixtureList())).thenReturn(matchList);
        when(repository.saveAll(matchList)).thenReturn(matchList);

        //When
        MatchRespondDto theRespond = service.uploadMatches(param);

        //Then
        assertEquals(Respond.MATCH_UPLOAD_OK_LEAGUE.getRespond() + "39" +
                Respond.MATCH_UPLOAD_OK_DATE.getRespond() + toDate
                + Respond.MATCH_UPLOAD_OK_SEASON.getRespond() + "2023", theRespond.getRespond());
    }

    @Test
    void shouldUploadMatchesFromLeagueConfigList() {
        //Given
        Season season = new Season(1L, "2023");

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

        doReturn(season).when(seasonService).fetchSeason();
        when(client.getFixtures(any())).thenReturn(fixturesList);

        Match match = new Match(1L, 39, 365L, OffsetDateTime.parse("2023-04-01T14:00:00+00:00"),
                "Not Started", "81:48", "Liverpool", "Liverpool.logo",
                true, "Everton", "Everton.logo", false, 2, 1);

        List<Match> matchList = new ArrayList<>();
        matchList.add(match);

        when(mapper.mapFixtureRespondToMatchList(any())).thenReturn(matchList);
        when(repository.saveAll(any())).thenReturn(matchList);

        //When
        MatchRespondDto theRespond = service.uploadMatchesFromLeagueConfigList();

        //Then
        assertEquals(Respond.ALL_MATCH_UPLOAD_OK.getRespond(), theRespond.getRespond());
    }
}