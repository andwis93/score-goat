package com.restapi.scoregoat.service.ClientService;

import com.restapi.scoregoat.client.FootballClient;
import com.restapi.scoregoat.config.DateConfig;
import com.restapi.scoregoat.config.SeasonConfig;
import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.domain.client.mapJSON.*;
import com.restapi.scoregoat.mapper.MatchMapper;
import com.restapi.scoregoat.service.DBService.MatchDBService;
import com.restapi.scoregoat.service.DBService.MatchPredictionDBService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MatchClientServiceTests {
    @InjectMocks
    private MatchClientService service;
    @Mock
    private SeasonClientService seasonService;
    @Mock
    private FootballClient client;
    @Mock
    private MatchDBService dbService;
    @Mock
    private MatchPredictionDBService predictionDBService;
    @Mock
    private MatchMapper mapper;
    @Mock
    private DateConfig config;

    @Test
    void shouldUploadMatches() {
        //Given
        Season season = new Season(1L, "2023");

        Team homeTeam = new Team("LiverPool", "http://Logo.png", true);
        Team awayTeam = new Team("Everton", "http://Logo2.png", false);
        Status status = new Status("Not Started", null);
        Fixture fixture = new Fixture(189L,"2023-04-01T14:00:00+00:00", status);
        League league = new League(SeasonConfig.DEFAULT_LEAGUE.getId(), "Premier League", "http://PremierLeague.png" );
        Teams teams = new Teams(homeTeam, awayTeam);
        Goals goals = new Goals(2,1);
        FixtureRespond fixtureRespond = new FixtureRespond(fixture, league, teams, goals);
        FixturesList fixturesList = new FixturesList();
        fixturesList.getFixtureList().add(fixtureRespond);

        FixtureParam param = new FixtureParam(SeasonConfig.DEFAULT_LEAGUE.getId(),
                season.getYear());

        when(client.getFixtures(param)).thenReturn(fixturesList);

        Match match = new Match(1L, SeasonConfig.DEFAULT_LEAGUE.getId(), 365L, OffsetDateTime.parse("2023-04-01T14:00:00+00:00"),
                "Not Started", "81:48", "Liverpool", "Liverpool.logo",
                true, "Everton", "Everton.logo", false, 2, 1);

        List<Match> matchList = new ArrayList<>();
        matchList.add(match);

        when(mapper.mapFixtureRespondToMatchList(fixturesList.getFixtureList())).thenReturn(matchList);

        //When
        MatchRespondDto theRespond = service.uploadMatches(param);

        //Then
        assertEquals(Respond.MATCH_UPLOAD_OK_LEAGUE.getRespond() + "39"
                + Respond.MATCH_UPLOAD_OK_SEASON.getRespond() + "2023", theRespond.getRespond());
    }

    @Test
    void shouldUploadMatchesFromLeagueConfigList() {
        //Given
        Season season = new Season(1L, "2023");

        Team homeTeam = new Team("LiverPool", "http://Logo.png", true);
        Team awayTeam = new Team("Everton", "http://Logo2.png", false);
        Status status = new Status("Not Started", null);
        Fixture fixture = new Fixture(189L,"2023-04-01T14:00:00+00:00", status);
        League league = new League(SeasonConfig.DEFAULT_LEAGUE.getId(), "Premier League", "http://PremierLeague.png" );
        Teams teams = new Teams(homeTeam, awayTeam);
        Goals goals = new Goals(2,1);
        FixtureRespond fixtureRespond = new FixtureRespond(fixture, league, teams, goals);
        FixturesList fixturesList = new FixturesList();
        fixturesList.getFixtureList().add(fixtureRespond);

        doReturn(season).when(seasonService).fetchSeason();
        when(client.getFixtures(any())).thenReturn(fixturesList);

        Match match = new Match(1L, SeasonConfig.DEFAULT_LEAGUE.getId(), 365L, OffsetDateTime.parse("2023-04-01T14:00:00+00:00"),
                "Not Started", "81:48", "Liverpool", "Liverpool.logo",
                true, "Everton", "Everton.logo", false, 2, 1);

        List<Match> matchList = new ArrayList<>();
        matchList.add(match);

        when(mapper.mapFixtureRespondToMatchList(any())).thenReturn(matchList);

        //When
        MatchRespondDto theRespond = service.uploadMatchesFromLeagueConfigList();

        //Then
        assertEquals(Respond.ALL_MATCH_UPLOAD_OK.getRespond(), theRespond.getRespond());
    }

    @Test
    void shouldEliminateSelected() {
        //Given
        Match match1 = new Match(67L, SeasonConfig.DEFAULT_LEAGUE.getId(), 365L, OffsetDateTime.parse("2023-04-01T14:00:00+00:00"),
                "Not Started", "00:00", "Liverpool", "Liverpool.logo",
                true, "Everton", "Everton.logo", false,
                2, 1);

        Match match2 = new Match(25L, SeasonConfig.DEFAULT_LEAGUE.getId(), 367L, OffsetDateTime.parse("2023-04-01T19:00:00+00:00"),
                "Not Started", "00:00", "Arsenal", "Arsenal.logo",
                false, "WestHam", "WestHam.logo", true, 2, 3);

        List<Match> matchList = new ArrayList<>();
        matchList.add(match1);
        matchList.add(match2);

        when(dbService.findByLeagueIdOrderByDate(SeasonConfig.DEFAULT_LEAGUE.getId())).thenReturn(matchList);
        when(predictionDBService.existsMatchPredictionByUserIdAndFixtureId(1L,365L)).thenReturn(false);
        when(predictionDBService.existsMatchPredictionByUserIdAndFixtureId(1L,367L)).thenReturn(true);

        //When
        List<Match> theMatchList = service.eliminateSelected(1L, 39);

        //Then
        assertEquals(1, theMatchList.size());
    }

    @Test
    void shouldMatchesWithDateRange() {
        //Given
        Match match1 = new Match(67L, SeasonConfig.DEFAULT_LEAGUE.getId(), 365L, OffsetDateTime.now(),
                "Not Started", "00:00", "Liverpool", "Liverpool.logo",
                true, "Everton", "Everton.logo", false,
                2, 1);

        Match match2 = new Match(25L, SeasonConfig.DEFAULT_LEAGUE.getId(), 367L, OffsetDateTime.now().minusDays(200),
                "Not Started", "00:00", "Arsenal", "Arsenal.logo",
                false, "WestHam", "WestHam.logo", true, 2, 3);

        List<Match> matchList = new ArrayList<>();
        matchList.add(match1);
        matchList.add(match2);

        when(dbService.findByLeagueIdOrderByDate(SeasonConfig.DEFAULT_LEAGUE.getId())).thenReturn(matchList);
        when(config.getFrom()).thenReturn(OffsetDateTime.now().minusDays(10));
        when(config.getTo()).thenReturn(OffsetDateTime.now().plusDays(10));

        //When
        List<Match> theMatchList = service.matchesWithDateRange(39);

        //Then
        assertEquals(1, theMatchList.size());
    }
}
