package com.restapi.scoregoat.controller;

import com.restapi.scoregoat.config.SeasonConfig;
import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.facade.ScoreGoatFacade;
import com.restapi.scoregoat.mapper.GraduationMapper;
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
@WebMvcTest(GraduationController.class)
public class GraduationControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ScoreGoatFacade facade;
    @MockBean
    private GraduationMapper mapper;

    @Test
    void shouldGetGraduations() throws Exception {
        //Given
        User user = new User("Name1","Email1@test.com", "Password1");
        user.setId(202L);

        Graduation graduation = new Graduation(SeasonConfig.DEFAULT_LEAGUE.getId(), user);
        graduation.setRank(1);
        graduation.setPoints(7);

        GraduationDto graduationDto = new GraduationDto(graduation.getRank(), user.getName(), graduation.getPoints());

        List<Graduation> graduations = new ArrayList<>();
        graduations.add(graduation);

        List<GraduationDto> graduationDtos = new ArrayList<>();
        graduationDtos.add(graduationDto);

        when(mapper.mapGraduationToGraduationDtoList(graduations)).thenReturn(graduationDtos);
        when(facade.fetchGraduationDtoListByLeagueId(SeasonConfig.DEFAULT_LEAGUE.getId())).thenReturn(graduations);
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/v1/scoregoat/graduation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("leagueId","39")
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].rank", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userName", Matchers.is("Name1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].points", Matchers.is(7)));
    }
}
