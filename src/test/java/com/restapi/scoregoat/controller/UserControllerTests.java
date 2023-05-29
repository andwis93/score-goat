package com.restapi.scoregoat.controller;

import com.google.gson.Gson;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(UserController.class)
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ScoreGoatFacade facade;

    @Test
    void shouldCreateUser() throws Exception {
        //Given
        UserDto userDto = new UserDto("NameDto1", "EmailDto1@test.com", "PasswordDto1");
        UserRespondDto respondDto = new UserRespondDto("Create Name1", NotificationType.SUCCESS.getType());
        when(facade.createUser(any())).thenReturn(respondDto);
        Gson gson = new Gson();
        String jsonContent = gson.toJson(userDto);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/v1/scoregoat/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.respond", Matchers.is("Create Name1")));
    }

    @Test
    void shouldChangePassword() throws Exception {
        //Given
        PasswordDto passwordDto = new PasswordDto(1L, "OldPassword", "MatchPassword",
                "MatchPassword");
        UserRespondDto respondDto = new UserRespondDto("Password was changed", NotificationType.SUCCESS.getType());
        when(facade.changePassword(any())).thenReturn(respondDto);
        Gson gson = new Gson();
        String jsonContent = gson.toJson(passwordDto);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/v1/scoregoat/users/passwordchange")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.respond", Matchers.is("Password was changed")));
    }

    @Test
    void shouldChangeAccountInformation() throws Exception {
        //Given
        AccountDto accountDto = new AccountDto(1L, "Test", "Test@test.com", "password");
        UserRespondDto respondDto = new UserRespondDto("Account information were changed", NotificationType.SUCCESS.getType());
        when(facade.accountChange(any())).thenReturn(respondDto);
        Gson gson = new Gson();
        String jsonContent = gson.toJson(accountDto);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/v1/scoregoat/users/accountchange")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.respond", Matchers.is("Account information were changed")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.notificationType", Matchers.is("success")));
    }
}
