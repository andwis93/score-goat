package com.restapi.scoregoat.service;

import com.restapi.scoregoat.domain.LogData;
import com.restapi.scoregoat.repository.LogDataRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class LogDataService {
    private LogDataRepository repository;

    public void saveLog(LogData logData) {
        repository.save(logData);
    }
}
