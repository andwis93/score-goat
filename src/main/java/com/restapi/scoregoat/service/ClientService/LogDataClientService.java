package com.restapi.scoregoat.service.ClientService;

import com.restapi.scoregoat.service.DBService.LogDataDBService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class LogDataClientService {
    private final LogDataDBService dbService;

    public void deleteAllLogs() {
        dbService.deleteAll();
    }
}
