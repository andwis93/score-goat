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
import java.util.HashMap;
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
    void shouldSavePrediction() throws Exception {
        //Given
        Map<Long, String> matchesSelectedList = new HashMap<>();
        matchesSelectedList.put(1430L, Prediction.HOME.getResult());
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is(Respond.PREDICTIONS_SAVE_OK.getRespond())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type", Matchers.is(NotificationType.SUCCESS.getType())));
    }
}
