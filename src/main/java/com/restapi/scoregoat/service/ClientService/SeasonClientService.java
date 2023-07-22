package com.restapi.scoregoat.service.ClientService;

import com.restapi.scoregoat.client.FootballClient;
import com.restapi.scoregoat.domain.Code;
import com.restapi.scoregoat.config.SeasonConfig;
import com.restapi.scoregoat.domain.LogData;
import com.restapi.scoregoat.domain.Season;
import com.restapi.scoregoat.service.DBService.LogDataDBService;
import com.restapi.scoregoat.service.DBService.SeasonDBService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import java.util.List;

@AllArgsConstructor
@Service
@EnableAspectJAutoProxy
public class  SeasonClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FootballClient.class);
    private final SeasonDBService dbService;
    private final LogDataDBService logDataService;
    private final FootballClient footballClient;

    public Season setSeason() {
        try {
            Season season = new Season(footballClient.getFootballSeason());
            dbService.deleteAll();
            dbService.save(season);
            return season;
        }catch (Exception ex) {
            StringBuilder message = new StringBuilder(ex.getMessage() + " --ERROR: Couldn't replace season in DataBase-- ");
            logDataService.saveLog(new LogData(null,"Setting League ID: "
                    + SeasonConfig.DEFAULT_LEAGUE.getId(), Code.SEASON_SET_DB_ERROR.getCode(), message.toString()));
            LOGGER.error(message.toString(),ex);
            return null;
        }
    }

    public Season fetchSeason() {
        List<Season> list = dbService.findAll();
        if (list.size() == 0) {
            setSeason();
            list = dbService.findAll();
        } else {
            if (list.get(0).getYear().equals("")) {
                setSeason();
                list = dbService.findAll();
            }
        }
        return list.get(0);
    }
}
