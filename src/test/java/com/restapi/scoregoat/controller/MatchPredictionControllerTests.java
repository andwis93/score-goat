package com.restapi.scoregoat.controller;

import com.google.gson.Gson;
import com.restapi.scoregoat.config.SeasonConfig;
import com.restapi.scoregoat.domain.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(MatchPredictionController.class)
public class MatchPredictionControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ScoreGoatFacade facade;

    @Test
    void shouldGetMatchPrediction() throws Exception {
        //Given
        UserPredictionDto userPredictionDto = new UserPredictionDto("homeLogo", "Liverpool", 2,
                "2023-07-14", "21:00", 0, "Everton", "awayLogo",
                Result.HOME.getResult(), Points.WIN.getPoints(), Result.HOME.getResult());
        List<UserPredictionDto> predictionDtoList = new ArrayList<>();
        predictionDtoList.add(userPredictionDto);

        when(facade.getUserMatchPredictions(1L, SeasonConfig.DEFAULT_LEAGUE.getId())).
                thenReturn(predictionDtoList);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/v1/scoregoat/prediction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("userId", "1")
                        .param("leagueId","39")
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].homeLogo", Matchers.is("homeLogo")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].homeTeam", Matchers.is("Liverpool")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].homeGoal", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].date", Matchers.is("2023-07-14")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].time", Matchers.is("21:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].awayGoal", Matchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].awayTeam", Matchers.is("Everton")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].awayLogo", Matchers.is("awayLogo")));
    }

    @Test
    void shouldSavePrediction() throws Exception {
        //Given
        Map<Long, String> matchesSelectedList = new HashMap<>();
        matchesSelectedList.put(1430L, Result.HOME.getResult());
        PredictionDto predictionDto = new PredictionDto(202L, SeasonConfig.DEFAULT_LEAGUE.getId(), matchesSelectedList);
        NotificationRespondDto notificationRespondDto = new NotificationRespondDto(
                Respond.PREDICTIONS_SAVE_OK.getRespond(), NotificationType.SUCCESS.getType());

        when(facade.saveUserPredictions(any())).thenReturn(notificationRespondDto);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(predictionDto);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/v1/scoregoat/prediction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        Matchers.is(Respond.PREDICTIONS_SAVE_OK.getRespond())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type",
                        Matchers.is(NotificationType.SUCCESS.getType())));
    }
}
