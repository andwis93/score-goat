package com.restapi.scoregoat.service;

import com.restapi.scoregoat.domain.LogIn;
import com.restapi.scoregoat.domain.User;
import com.restapi.scoregoat.repository.LogInRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateLogInServiceTests {
    @InjectMocks
    private UpdateLogInService service;
    @Mock
    private LogInRepository repository;

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

        when(repository.findAll()).thenReturn(expiredLocks);

        //When
        long size = service.updateLogInLockedDates();

        //Given
        assertEquals(1, size);
        assertEquals(1, expiredLocks.get(0).getId());
    }
}
