package com.restapi.scoregoat.service.ClientService;

import com.restapi.scoregoat.domain.DurationValues;
import com.restapi.scoregoat.domain.Session;
import com.restapi.scoregoat.service.DBService.SessionDBService;
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
public class SessionClientServiceTests {

    @InjectMocks
    private SessionClientService service;
    @Mock
    private SessionDBService dbService;

    @Test
    void shouldRemoveExpiredSessions() {
        //Given
        Session session1 = new Session();
        Session session2 = new Session();

        session1.setEnd(LocalDateTime.now().plusMinutes(DurationValues.SESSION_LENGTH.getValue() + 1));
        session2.setEnd(LocalDateTime.now().minusMinutes(DurationValues.SESSION_LENGTH.getValue() - 1));

        List<Session> sessionList = new ArrayList<>();
        sessionList.add(session1);
        sessionList.add(session2);

        when(dbService.findAll()).thenReturn(sessionList);

        //When
        Long size = service.removeExpiredSession();

        //Given
        assertEquals(1, size);
    }
}
