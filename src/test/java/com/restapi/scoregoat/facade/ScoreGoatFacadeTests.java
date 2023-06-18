package com.restapi.scoregoat.facade;

import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.domain.Season;
import com.restapi.scoregoat.service.ClientService.LogInClientService;
import com.restapi.scoregoat.service.ClientService.UserClientService;
import com.restapi.scoregoat.service.ClientService.SeasonClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ScoreGoatFacadeTests {
    @InjectMocks
    private ScoreGoatFacade facade;
    @Mock
    private UserClientService userClientService;
    @Mock
    private SeasonClientService seasonService;
    @Mock
    private LogInClientService logInService;

    @Test
    void shouldCreateUser() {
        //When
        UserDto userDto = new UserDto("Name1","Email1@test.com", "Password1");
        UserRespondDto respond = new UserRespondDto("Create Name1",  NotificationType.SUCCESS.getType());
        when(userClientService.signUpUser(userDto)).thenReturn(respond);

        //When
        UserRespondDto theRespond = facade.createUser(userDto);

        //Then
        assertEquals("Create Name1", theRespond.getRespond());
    }

    @Test
    void shouldTryLogIn() {
        //When
        UserDto userDto = new UserDto("Name1", "Password1");
        UserRespondDto respond = new UserRespondDto("Create Name1",  NotificationType.SUCCESS.getType());
        when(logInService.logInAttempt(userDto)).thenReturn(respond);

        //When
        UserRespondDto theRespond = facade.tryLogIn(userDto);

        //Then
        assertEquals("Create Name1", theRespond.getRespond());
    }

    @Test
    void shouldFetchSeason() {
        //When
        Season season = new Season("2023");
        when(seasonService.fetchSeason()).thenReturn(season);

        //When
        Season theSeason = facade.fetchSeason();

        //Then
        assertEquals("2023", theSeason.getYear());
    }

    @Test
    void shouldSetSeason() {
        //When
        Season season = new Season("2023");
        when(seasonService.setSeason()).thenReturn(season);

        //When
        Season theSeason = facade.setSeason();

        //Then
        assertEquals("2023", theSeason.getYear());
    }
}
