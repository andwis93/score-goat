package com.restapi.scoregoat.service.DBService;

import com.restapi.scoregoat.domain.LogData;
import com.restapi.scoregoat.repository.LogDataRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class LogDataDBService {
    private final LogDataRepository repository;
    public void saveLog(LogData logData) {
        repository.save(logData);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
