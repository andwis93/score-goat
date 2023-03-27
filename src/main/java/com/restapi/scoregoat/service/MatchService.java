package com.restapi.scoregoat.service;

import com.restapi.scoregoat.client.FootballClient;
import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.domain.client.FixturesList;
import com.restapi.scoregoat.mapper.MatchMapper;
import com.restapi.scoregoat.repository.MatchRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@EnableAspectJAutoProxy
public class MatchService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MatchService.class);
    private MatchRepository repository;
    private MatchMapper mapper;
    private final FootballClient client;
    private LogDataService logDataService;

    public void deleteFixtures(){
        repository.deleteAll();
    }

    public MatchesRespondDto uploadMatches(FixtureParam param) {
        try {
            FixturesList fixturesList = client.getFixtures(param);
            repository.saveAll(mapper.mapToList(fixturesList.getFixtureList()));
            return new MatchesRespondDto(Respond.MATCH_UPLOAD_OK.getRespond());
        }catch (Exception ex) {
            String message = ex.getMessage() + " --ERROR: Couldn't upload Matches to DataBase-- ";
            logDataService.saveLog(new LogData(null,"League ID: "
                    + param.getId(), Code.MATCH_UPLOAD.getCode(), message));
            LOGGER.error(message,ex);
            return null;
        }
    }
}
