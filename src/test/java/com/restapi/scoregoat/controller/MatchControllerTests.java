package com.restapi.scoregoat.controller;

import com.restapi.scoregoat.domain.MatchDto;
import com.restapi.scoregoat.domain.MatchRespondDto;
import com.restapi.scoregoat.facade.ScoreGoatFacade;
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
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(MatchController.class)
public class MatchControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ScoreGoatFacade facade;

    @Test
    void shouldGetMatches() throws Exception {
        //Given
        MatchDto matchDto = new MatchDto(1L, 39, 365L, "2023-04-01", "00:00",
                "Not Started", "81:48", "Liverpool", "Liverpool.logo",
                true, "Everton", "Everton.logo", false, 2, 1);
        List<MatchDto> matchDtoList = new ArrayList<>();
        matchDtoList.add(matchDto);
        when(facade.findByLeagueIdOrderByDate(any(Integer.class))).thenReturn(matchDtoList);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/v1/scoregoat/match")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id","39"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].leagueId", Matchers.is(39)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].fixtureId", Matchers.is(365)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].date", Matchers.is("2023-04-01")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].time", Matchers.is("00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status", Matchers.is("Not Started")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].elapsed", Matchers.is("81:48")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].homeTeam", Matchers.is("Liverpool")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].homeLogo", Matchers.is("Liverpool.logo")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].homeWinner", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].awayTeam", Matchers.is("Everton")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].awayLogo", Matchers.is("Everton.logo")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].awayWinner", Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].homeGoals", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].awayGoals", Matchers.is(1)));
    }

    @Test
    void shouldUploadMatches() throws Exception {
        //Given
        MatchRespondDto respondDto = new MatchRespondDto("TestRespond");
        when(facade.uploadMatchesFromLeagueConfigList()).thenReturn(respondDto);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/v1/scoregoat/match")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.respond", Matchers.is("TestRespond")));
    }
}
