package com.restapi.scoregoat.repository;

import com.restapi.scoregoat.domain.LogData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class LogDataRepositoryTests {
    @Autowired
    private LogDataRepository repository;

    @Test
    void testSaveAndFindAll() {
        //Given
        LogData logData = new LogData();
        logData.setOperateValue("OperationValueTest");
        logData.setCode("CodeTest");
        logData.setDescription("DescriptionTest");

        //When
        repository.save(logData);
        List<LogData> logDataList = repository.findAll()
                .stream().filter(l -> l.getOperateValue().equals("OperationValueTest")).toList();

        //Then
        assertEquals(1, logDataList.size());
        assertEquals("OperationValueTest", logDataList.get(0).getOperateValue());

        //CleanUp
        try{
            repository.deleteById(logDataList.get(0).getId());
        } catch (Exception ex) {
            // do nothing
        }
    }
}
