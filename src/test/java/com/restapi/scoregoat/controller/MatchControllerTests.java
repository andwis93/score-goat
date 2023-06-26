package com.restapi.scoregoat.controller;

import com.restapi.scoregoat.config.SeasonConfig;
import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.facade.ScoreGoatFacade;
import com.restapi.scoregoat.mapper.MatchMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(MatchController.class)
public class MatchControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ScoreGoatFacade facade;
    @MockBean
    private MatchMapper mapper;

    @Test
    void shouldGetMatches() throws Exception {
        //Given
        Match match = new Match(1L, SeasonConfig.DEFAULT_LEAGUE.getId(), 365L, OffsetDateTime.parse("2023-04-01T14:00:00+00:00"),
                "Not Started", "81:48", "Liverpool", "Liverpool.logo",
                true, "Everton", "Everton.logo", false, 2, 1);
        List<Match> matchList = new ArrayList<>();
        matchList.add(match);

        MatchDto matchDto = new MatchDto(1L, SeasonConfig.DEFAULT_LEAGUE.getId(), 365L, "2023-04-01", "14:00",
                "Not Started", "00:00", "Liverpool", "Liverpool.logo",
                true, "Everton", "Everton.logo", false, 2, 1);
        List<MatchDto> matchDtoList = new ArrayList<>();
        matchDtoList.add(matchDto);
        when(mapper.mapMatchToMatchDtoList(matchList)).thenReturn(matchDtoList);
        when(facade.findByLeagueIdOrderByDate(1L, SeasonConfig.DEFAULT_LEAGUE.getId())).thenReturn(matchList);
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/v1/scoregoat/match")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("userId", "1")
                        .param("leagueId","39")
                        .characterEncoding("UTF-8"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].leagueId", Matchers.is(39)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].fixtureId", Matchers.is(365)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].date", Matchers.is("2023-04-01")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].time", Matchers.is("14:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status", Matchers.is("Not Started")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].elapsed", Matchers.is("00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].homeTeam", Matchers.is("Liverpool")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].homeLogo", Matchers.is("Liverpool.logo")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].homeWinner", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].awayTeam", Matchers.is("Everton")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].awayLogo", Matchers.is("Everton.logo")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].awayWinner", Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].homeGoals", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].awayGoals", Matchers.is(1)));
    }
}
