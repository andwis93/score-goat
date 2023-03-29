package com.restapi.scoregoat.controller;

import com.google.gson.Gson;
import com.restapi.scoregoat.domain.Season;
import com.restapi.scoregoat.domain.client.mapJSON.Year;
import com.restapi.scoregoat.facade.ScoreGoatFacade;
import com.restapi.scoregoat.mapper.SeasonMapper;
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
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(SeasonController.class)
public class SeasonControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ScoreGoatFacade facade;
    @MockBean
    private SeasonMapper mapper;

    @Test
    void shouldGetSeason() throws Exception {
        //Given
        Year year = new Year("2023Dto");
        Season season = new Season("2023");
        when(facade.fetchSeason()).thenReturn(season);
        when(mapper.mapSeasonToSeasonDto(season)).thenReturn(year);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/v1/scoregoat/season")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.year", Matchers.is("2023Dto")));
    }

    @Test
    void shouldSetSeason() throws Exception {
        //Given
        Year year = new Year("2023Dto");
        Season season = new Season("2023");
        when(facade.setSeason()).thenReturn(season);
        when(mapper.mapSeasonToSeasonDto(season)).thenReturn(year);
        Gson gson = new Gson();
        String jsonContent = gson.toJson(year);


        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/v1/scoregoat/season?id=39")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.year", Matchers.is("2023Dto")));
    }
}
