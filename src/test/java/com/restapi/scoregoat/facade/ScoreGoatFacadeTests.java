package com.restapi.scoregoat.facade;

import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.service.LogInService;
import com.restapi.scoregoat.service.SeasonService;
import com.restapi.scoregoat.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ScoreGoatFacadeTests {
    private static final int LEAGUE_ID = 39;
    @InjectMocks
    private ScoreGoatFacade facade;
    @Mock
    private UserService userService;
    @Mock
    private SeasonService seasonService;
    @Mock
    private LogInService logInService;

    @Test
    void shouldCreateUser() {
        //When
        UserDto userDto = new UserDto("Name1","Email1@test.com", "Password1");
        UserRespondDto respond = new UserRespondDto("Create Name1", WindowStatus.CLOSE.getStatus());
        when(userService.signInUser(userDto)).thenReturn(respond);

        //When
        UserRespondDto theRespond = facade.createUser(userDto);

        //Then
        assertEquals("Create Name1", theRespond.getRespond());
        assertEquals(WindowStatus.CLOSE.getStatus(), theRespond.getWindowStatus());
    }

    @Test
    void shouldTryLogIn() {
        //When
        UserParam userParam = new UserParam("Name1", "Password1");
        UserRespondDto respond = new UserRespondDto("Create Name1", WindowStatus.CLOSE.getStatus());
        when(logInService.logInAttempt(userParam)).thenReturn(respond);

        //When
        UserRespondDto theRespond = facade.tryLogIn(userParam);

        //Then
        assertEquals("Create Name1", theRespond.getRespond());
        assertEquals(WindowStatus.CLOSE.getStatus(), theRespond.getWindowStatus());
    }

    @Test
    void shouldFetchSeason() {
        //When
        Season season = new Season("2023");
        when(seasonService.getSeason()).thenReturn(season);

        //When
        Season theSeason = facade.fetchSeason();

        //Then
        assertEquals("2023", theSeason.getYear());
    }

    @Test
    void shouldSetSeason() {
        //When
        Season season = new Season("2023");
        when(seasonService.setSeason(LEAGUE_ID)).thenReturn(season);

        //When
        Season theSeason = facade.setSeason(LEAGUE_ID);

        //Then
        assertEquals("2023", theSeason.getYear());
    }
}
