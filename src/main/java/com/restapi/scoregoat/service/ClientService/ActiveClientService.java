package com.restapi.scoregoat.service.ClientService;

import com.restapi.scoregoat.client.FootballClient;
import com.restapi.scoregoat.config.SeasonConfig;
import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.service.DBService.ActiveDBService;
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
public class ActiveClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FootballClient.class);
    private final ActiveDBService dbService;
    private final LogDataDBService logDataService;


    public void setActive(boolean isActive, int leagueId) {
        try {
            Active active = dbService.findByLeagueId(leagueId);
            active.setActive(isActive);
            dbService.save(active);
        }catch (Exception ex) {
            StringBuilder message = new StringBuilder(ex.getMessage() + " --ERROR: Couldn't set Active status in DataBase-- ");
            logDataService.saveLog(new LogData(null,"League Id: " + leagueId, Code.ACTIVE_STATUS_SET_ERROR.getCode(), message.toString()));
            LOGGER.error(message.toString(),ex);
        }
    }

    public boolean fetchActiveStatus(int leagueId) {
        Active active = dbService.findByLeagueId(leagueId);
        if (active == null) {
           setActive(true, leagueId);
           active = dbService.findByLeagueId(leagueId);
        }
        return active.getActive();
    }
}
