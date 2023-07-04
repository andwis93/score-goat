package com.restapi.scoregoat.controller;

import com.restapi.scoregoat.config.SeasonConfig;
import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.facade.ScoreGoatFacade;
import com.restapi.scoregoat.mapper.RankingMapper;
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
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(RankingController.class)
public class RankingControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ScoreGoatFacade facade;
    @MockBean
    private RankingMapper mapper;

    @Test
    void shouldGetRankings() throws Exception {
        //Given
        User user = new User("Name1","Email1@test.com", "Password1");
        user.setId(202L);

        Ranking ranking = new Ranking(SeasonConfig.DEFAULT_LEAGUE.getId(), user);
        ranking.setRank(1);
        ranking.setPoints(7);

        RankingDto rankingDto = new RankingDto(Integer.toString(ranking.getRank()), user.getName(),
                Integer.toString(ranking.getPoints()), RankStatus.ABOVE.getStatus());

        List<Ranking> rankings = new ArrayList<>();
        rankings.add(ranking);

        List<RankingDto> rankingsDto = new ArrayList<>();
        rankingsDto.add(rankingDto);

        when(mapper.mapRankingToRankingDtoList(rankings)).thenReturn(rankingsDto);
        when(facade.fetchRankingDtoListByLeagueId(SeasonConfig.DEFAULT_LEAGUE.getId())).thenReturn(rankingsDto);
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/v1/scoregoat/ranking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("leagueId","39")
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].rank", Matchers.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userName", Matchers.is("Name1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].points", Matchers.is("7")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status", Matchers.is(2)));
    }
}
