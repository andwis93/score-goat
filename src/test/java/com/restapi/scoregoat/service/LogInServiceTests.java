package com.restapi.scoregoat.service;

import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.repository.LogInRepository;
import com.restapi.scoregoat.repository.UserRepository;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LogInServiceTests {
    @InjectMocks
    private LogInService service;
    @Mock
    private LogInRepository repository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SessionService sessionService;
    @Mock
    private StrongPasswordEncryptor encryptor;

    @Test
    void testLogInAttempt() {
        //Given
        User user = new User("Name1","Email1@test.com", "Password1");
        LogIn attempt = new LogIn(user);
        UserDto userDto = new UserDto("Name1", "Password1");
        when(userRepository.findByName("Name1")).thenReturn(Optional.of(user));
        when(repository.findByUser(user)).thenReturn(Optional.of(attempt));
        when(encryptor.checkPassword(userDto.getPassword(), user.getPassword())).thenReturn(true);
        when(sessionService.saveRefreshedSession(any())).thenReturn(true);
        when(repository.save(attempt)).thenReturn(attempt);

        //When
        UserRespondDto respondDto = service.logInAttempt(userDto);

        //Then
        assertEquals("Name1", respondDto.getUserName());
        assertEquals("Email1@test.com", respondDto.getEmail());
        assertEquals(Respond.USER_LOGGED_IN.getRespond(), respondDto.getRespond());
        assertTrue(respondDto.isLogIn());
    }
}
