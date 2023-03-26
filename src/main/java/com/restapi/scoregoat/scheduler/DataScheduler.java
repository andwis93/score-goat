package com.restapi.scoregoat.scheduler;

import com.restapi.scoregoat.domain.Leagues;
import com.restapi.scoregoat.service.UpdateLogInService;
import com.restapi.scoregoat.service.SeasonService;
import com.restapi.scoregoat.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataScheduler {
    @Autowired
    private SeasonService seasonService;
    @Autowired
    private UpdateLogInService cleanLogInService;
    @Autowired
    private SessionService sessionService;

    @Scheduled(cron = "0 0 2 1 * ?", zone="Europe/Warsaw")
    public void reloadData() {
        seasonService.setSeason(Leagues.PREMIER_LEAGUE.getId());
    }

    @Scheduled(cron = "0 59 * * * ?", zone="Europe/Warsaw")
    public void updateLogIn() {
        cleanLogInService.updateLogInLockedDates();
    }

    @Scheduled(cron = "0 */5 * * * ?", zone="Europe/Warsaw")
    public void removeExpiredSessions() {
        sessionService.removeExpiredSession();
    }
}
