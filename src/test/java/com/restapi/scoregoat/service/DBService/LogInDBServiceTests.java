package com.restapi.scoregoat.service.DBService;

import com.restapi.scoregoat.domain.LogIn;
import com.restapi.scoregoat.domain.User;
import com.restapi.scoregoat.repository.LogInRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
public class LogInDBServiceTests {
    @InjectMocks
    private LogInDBService service;
    @Mock private LogInRepository repository;

    @Test
    void testResetAttempt() {
        //Given
        User user = new User("Name1","Email1@test.com", "Password1");
        LogIn logIn = new LogIn(100L, 1, LocalDateTime.now(), LocalDate.now().minusDays(1), user);

        //When
        service.resetAttempt(logIn);

        //Then
        assertEquals(0, logIn.getAttempt());
    }
}
