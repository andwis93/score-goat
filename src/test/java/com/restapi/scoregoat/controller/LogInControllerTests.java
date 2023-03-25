package com.restapi.scoregoat.controller;

import com.restapi.scoregoat.domain.UserRespondDto;
import com.restapi.scoregoat.domain.WindowStatus;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(LogInController.class)
public class LogInControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ScoreGoatFacade facade;

    @Test
    void shouldLogUserIn() throws Exception {
        //Given
        UserRespondDto respond = new UserRespondDto("Create Name1", WindowStatus.CLOSE.getStatus());
        respond.setLogIn(true);
        when(facade.tryLogIn(any())). thenReturn(respond);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/v1/scoregoat/login?name=n3&password=12")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.respond", Matchers.is("Create Name1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.windowStatus", Matchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.logIn", Matchers.is(true)));
    }
}
