package com.restapi.scoregoat.client;

import com.restapi.scoregoat.config.FootballConfig;
import com.restapi.scoregoat.config.SeasonConfig;
import com.restapi.scoregoat.domain.FixtureParam;
import com.restapi.scoregoat.domain.client.mapJSON.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import java.net.URISyntaxException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FootballClientTests {
    @InjectMocks
    private FootballClient client;
    @Mock
    private RestTemplate rest;
    @Mock
    private FootballConfig config;

    @Test
    public void shouldFetchSeasonYear() throws URISyntaxException {
        //Given
        when(config.getFootballApiEndpoint()).thenReturn("https://test.com");
        when(config.getFootballAppHeader()).thenReturn("AppHeaderTest");
        when(config.getFootballAppKey()).thenReturn("ApiKeyTest");

        URI uri = new URI("https://test.com/leagues?id=" + SeasonConfig.DEFAULT_LEAGUE.getId());

        Year year = new Year("2023");

        Seasons season = new Seasons();
        season.getYearsList().add(year);

        SeasonsList seasonsList = new SeasonsList();
        seasonsList.getSeasonsLists().add(season);

        HttpHeaders headers = new HttpHeaders();
        headers.set("AppHeaderTest", "ApiKeyTest");
        HttpEntity<Object> header = new HttpEntity<>(headers);

        ResponseEntity<SeasonsList> respond = new ResponseEntity<>(seasonsList, HttpStatus.OK);

        when(rest.exchange(uri, HttpMethod.GET, header, SeasonsList.class)).thenReturn(respond);

        //When
        String theYear = client.getFootballSeason();

        //Then
        assertEquals("2023", theYear);
    }

    @Test
    public void shouldFetchLeagueFixtureList() throws URISyntaxException {
        //Given
        when(config.getFootballApiEndpoint()).thenReturn("https://test.com");
        when(config.getFootballAppHeader()).thenReturn("AppHeaderTest");
        when(config.getFootballAppKey()).thenReturn("ApiKeyTest");

        FixtureParam param = new FixtureParam(SeasonConfig.DEFAULT_LEAGUE.getId() ,"2023");

        URI uri = new URI("https://test.com/fixtures?league=" + param.getId() + "&season=2023");

        HttpHeaders headers = new HttpHeaders();
        headers.set("AppHeaderTest", "ApiKeyTest");
        HttpEntity<Object> header = new HttpEntity<>(headers);

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

        ResponseEntity<FixturesList> respond = new ResponseEntity<>(fixturesList, HttpStatus.OK);

        when(rest.exchange(uri, HttpMethod.GET, header, FixturesList.class)).thenReturn(respond);

        //When
        FixturesList theFixturesList = client.getFixtures(param);

        //Then
        assertEquals("LiverPool", theFixturesList.getFixtureList().get(0).getTeams().getHomeTeam().getName());
        assertEquals("http://Logo2.png", theFixturesList.getFixtureList().get(0).getTeams().getAwayTeam().getLogo());
        assertEquals("Not Started", theFixturesList.getFixtureList().get(0).getFixture().getStatus().getStatus());
        assertEquals("2023-04-01T14:00:00+00:00", theFixturesList.getFixtureList().get(0).getFixture().getDateUTS());
        assertEquals(39, theFixturesList.getFixtureList().get(0).getLeague().getId());
    }
}
