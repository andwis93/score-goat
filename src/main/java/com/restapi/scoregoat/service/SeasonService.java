package com.restapi.scoregoat.service;

import com.restapi.scoregoat.client.FootballClient;
import com.restapi.scoregoat.domain.Code;
import com.restapi.scoregoat.config.SeasonConfig;
import com.restapi.scoregoat.domain.LogData;
import com.restapi.scoregoat.domain.Season;
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
    private final SeasonRepository repository;
    private final LogDataService logDataService;
    private final FootballClient footballClient;

    public Season setSeason() {
        try {
            Season season = new Season(footballClient.getFootballSeason());
            repository.deleteAll();
            repository.save(season);
            return season;
        }catch (Exception ex) {
            String message = ex.getMessage() + " --ERROR: Couldn't replace season in DataBase-- ";
            logDataService.saveLog(new LogData(null,"Setting League ID: "
                    + SeasonConfig.DEFAULT_LEAGUE.getId(), Code.SEASON_SET_DB_ERROR.getCode(), message));
            LOGGER.error(message,ex);
            return null;
        }
    }

    public Season fetchSeason() {
        List<Season> list = repository.findAll();
        if (list.size() == 0) {
            setSeason();
            list = repository.findAll();
        } else {
            if (list.get(0).getYear().equals("")) {
                setSeason();
                list = repository.findAll();
            }
        }
        return list.get(0);
    }
}
