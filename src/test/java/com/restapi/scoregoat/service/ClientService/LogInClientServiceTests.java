package com.restapi.scoregoat.service.ClientService;

import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.service.DBService.LogInDBService;
import com.restapi.scoregoat.service.DBService.UserDBService;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LogInClientServiceTests {
    @InjectMocks
    private LogInClientService service;
    @Mock
    private LogInDBService logInDBService;
    @Mock
    private UserDBService userDBService;
    @Mock
    private SessionClientService sessionService;
    @Mock
    private StrongPasswordEncryptor encryptor;

    @Test
    void testLogInAttempt() {
        //Given
        User user = new User("Name1","Email1@test.com", "Password1");
        LogIn attempt = new LogIn(user);
        UserDto userDto = new UserDto("Name1", "Password1");
        when(userDBService.existsByName(userDto.getName())).thenReturn(true);
        when(userDBService.findByName(userDto.getName())).thenReturn(user);
        when(logInDBService.findByUser(user)).thenReturn(attempt);
        when(encryptor.checkPassword(userDto.getPassword(), user.getPassword())).thenReturn(true);
        when(logInDBService.resetAttempt(attempt)).thenReturn(true);

        //When
        UserRespondDto respondDto = service.logInAttempt(userDto);

        //Then
        assertEquals("Name1", respondDto.getUserName());
        assertEquals("Email1@test.com", respondDto.getEmail());
        assertEquals(Respond.USER_LOGGED_IN.getRespond(), respondDto.getRespond());
        assertTrue(respondDto.isLogIn());
    }

    @Test
    void shouldUpdateLogInLockedDates() {
        //Given
        LogIn logIn1 = new LogIn();
        LogIn logIn2 = new LogIn();

        User user1 = new User();
        User user2 = new User();

        logIn1.setId(1L);
        logIn1.setUser(user1);
        logIn1.setLocked(LocalDateTime.now().plusHours(2));
        logIn1.setAttempt(2);

        logIn2.setId(2L);
        logIn2.setUser(user2);
        logIn2.setLocked(LocalDateTime.now().minusHours(2));
        logIn2.setAttempt(6);

        List<LogIn> expiredLocks = new ArrayList<>();
        expiredLocks.add(logIn1);
        expiredLocks.add(logIn2);

        when(logInDBService.findAll()).thenReturn(expiredLocks);

        //When
        long size = service.updateLogInLockedDates();

        //Given
        assertEquals(1, size);
        assertEquals(1, expiredLocks.get(0).getId());
    }
}
