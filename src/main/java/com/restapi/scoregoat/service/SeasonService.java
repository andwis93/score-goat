package com.restapi.scoregoat.service;

import com.restapi.scoregoat.client.FootballClient;
import com.restapi.scoregoat.domain.Code;
import com.restapi.scoregoat.domain.LogData;
import com.restapi.scoregoat.domain.client.Season;
import com.restapi.scoregoat.repository.SeasonRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import java.util.List;

@AllArgsConstructor
@Service
@EnableAspectJAutoProxy
public class SeasonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FootballClient.class);
    private LogDataService logDataService;
    private SeasonRepository repository;
    private final FootballClient footballClient;

    public Season setSeason(final int id) {
        try {
            Season season = new Season(footballClient.getFootballSeason(id));
            repository.deleteAll();
            repository.save(season);
            return season;
        }catch (Exception ex) {
            String message = ex.getMessage() + " --ERROR: Couldn't replace season in DataBase-- ";
            logDataService.saveLog(new LogData(null,"League ID: " + id, Code.SEASON_SET_DB.getCode(), message));
            LOGGER.error(message,ex);
            return null;
        }
    }

    public Season getSeason() {
        List<Season> list = repository.findAll();
        return list.get(0);
    }
}
