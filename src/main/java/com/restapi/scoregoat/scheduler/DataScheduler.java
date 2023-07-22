package com.restapi.scoregoat.scheduler;

import com.restapi.scoregoat.config.DateConfig;
import com.restapi.scoregoat.facade.ScoreGoatFacade;
import com.restapi.scoregoat.service.ClientService.*;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class DataScheduler {
    private final SeasonClientService seasonService;
    private final LogInClientService logInService;
    private final SessionClientService sessionService;
    private final LogDataClientService logDataService;
    private final RankingClientService rankingService;
    private final MatchPredictionClientService matchPredictionService;
    private final ScoreGoatFacade facade;
    private final DateConfig config;

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
    @Scheduled(cron = "0 30 2 10 6 ?", zone="Europe/Warsaw")
    public void annualCleaning() {
        logDataService.deleteAllLogs();
        logInService.deleteUnActiveUsers(config.getUnActiveUser());
        rankingService.deleteAll();
        matchPredictionService.deleteAll();
    }
}
