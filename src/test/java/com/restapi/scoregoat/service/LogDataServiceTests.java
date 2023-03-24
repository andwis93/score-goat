package com.restapi.scoregoat.service;

import com.restapi.scoregoat.domain.LogData;
import com.restapi.scoregoat.repository.LogDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LogDataServiceTests {
    @InjectMocks
    private LogDataService service;
    @Mock
    private LogDataRepository repository;

    @Test
    public void testSaveLog() {
        //Given
        LogData logData = new LogData(1L,"League ID: 39", "test");
        when(repository.save(any(LogData.class))).thenReturn(logData);

        //When
        service.saveLog(logData);

        //Then
        verify(repository, times(1)).save(any(LogData.class));
    }
}
