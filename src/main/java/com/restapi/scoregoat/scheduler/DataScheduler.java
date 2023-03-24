package com.restapi.scoregoat.scheduler;

import com.restapi.scoregoat.service.SeasonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataScheduler {
    public static final int PREMIER_LEAGUE = 39;
    @Autowired
    private SeasonService service;

    @Scheduled(cron = "* * 6 * * *")
    public void reloadData() {
        service.setSeason(PREMIER_LEAGUE);
    }
}
