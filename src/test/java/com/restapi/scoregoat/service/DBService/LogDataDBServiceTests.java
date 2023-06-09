package com.restapi.scoregoat.service.DBService;

import com.restapi.scoregoat.domain.Code;
import com.restapi.scoregoat.domain.LogData;
import com.restapi.scoregoat.repository.LogDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LogDataDBServiceTests {
    @InjectMocks
    private LogDataDBService service;
    @Mock
    private LogDataRepository repository;

    @Test
    public void testSaveLog() {
        //Given
        LogData logData = new LogData(1L,"League ID: 39", "test", Code.LOGIN_LOCKED_DATES_UPDATE.getCode());
        when(repository.save(any(LogData.class))).thenReturn(logData);

        //When
        service.saveLog(logData);

        //Then
        verify(repository, times(1)).save(any(LogData.class));
    }
}
