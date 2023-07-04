package com.restapi.scoregoat.scheduler;

import com.restapi.scoregoat.facade.ScoreGoatFacade;
import com.restapi.scoregoat.service.ClientService.LogInClientService;
import com.restapi.scoregoat.service.ClientService.SeasonClientService;
import com.restapi.scoregoat.service.ClientService.SessionClientService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class DataScheduler {
    private SeasonClientService seasonService;
    private LogInClientService logInService;
    private SessionClientService sessionService;
    private ScoreGoatFacade facade;

    @Scheduled(cron = "0 58 3 1 * ?", zone="Europe/Warsaw")
    public void reloadData() {
        seasonService.setSeason();
    }

    @Scheduled(cron = "0 59 * * * ?", zone="Europe/Warsaw")
    public void updateLogIn() {
        logInService.updateLogInLockedDates();
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
        facade.rankPredictions();
        facade.assignRanks();
    }
}
