package com.restapi.scoregoat.scheduler;

import com.restapi.scoregoat.facade.ScoreGoatFacade;
import com.restapi.scoregoat.service.UpdateLogInService;
import com.restapi.scoregoat.service.SeasonService;
import com.restapi.scoregoat.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataScheduler {
    private SeasonService seasonService;
    private UpdateLogInService cleanLogInService;
    private SessionService sessionService;
    private ScoreGoatFacade facade;

    public DataScheduler(SeasonService seasonService, UpdateLogInService cleanLogInService,
                         SessionService sessionService, ScoreGoatFacade facade) {
        this.seasonService = seasonService;
        this.cleanLogInService = cleanLogInService;
        this.sessionService = sessionService;
        this.facade = facade;
    }

    @Scheduled(cron = "0 58 3 1 * ?", zone="Europe/Warsaw")
    public void reloadData() {
        seasonService.setSeason();
    }

    @Scheduled(cron = "0 59 * * * ?", zone="Europe/Warsaw")
    public void updateLogIn() {
        cleanLogInService.updateLogInLockedDates();
    }

    @Scheduled(cron = "0 */5 * * * ?", zone="Europe/Warsaw")
    public void removeExpiredSessions() {
        sessionService.removeExpiredSession();
    }

    @Scheduled(cron = "0 0 */1 * * ?", zone="Europe/Warsaw")
    public void updateMatches() {
        facade.uploadMatchesFromLeagueConfigList();
    }

    @Scheduled(cron = "0 0 2 * * ?", zone="Europe/Warsaw")
    public void graduatePredictions() {
        facade.graduatePredictions();
    }
}
