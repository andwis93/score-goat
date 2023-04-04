package com.restapi.scoregoat.service;

import com.restapi.scoregoat.client.FootballClient;
import com.restapi.scoregoat.config.LeaguesListConfig;
import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.domain.client.mapJSON.FixturesList;
import com.restapi.scoregoat.domain.client.TimeFrame.TimeFrame;
import com.restapi.scoregoat.mapper.MatchMapper;
import com.restapi.scoregoat.repository.MatchRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
@EnableAspectJAutoProxy
public class MatchService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MatchService.class);
    private final MatchRepository repository;
    private final MatchMapper mapper;
    private final FootballClient client;
    private final SeasonService seasonService;
    private final LogDataService logDataService;
    public void deleteFixtures(){
        repository.deleteAll();
    }

    public MatchRespondDto uploadMatches(FixtureParam param) {
        try {
            FixturesList fixturesList = client.getFixtures(param);
            repository.saveAll(mapper.mapFixtureRespondToMatchList(fixturesList.getFixtureList()));
            return new MatchRespondDto(param.getId(),Respond.MATCH_UPLOAD_OK_LEAGUE.getRespond() + param.getId()
                    + Respond.MATCH_UPLOAD_OK_DATE.getRespond() + param.getToDate()
                    + Respond.MATCH_UPLOAD_OK_SEASON.getRespond() + param.getSeason());
        }catch (Exception ex) {
            String message = ex.getMessage() + " --ERROR: Couldn't upload Matches to DataBase-- ";
            logDataService.saveLog(new LogData(null,"League ID: "
                    + param.getId(), Code.MATCH_UPLOAD.getCode(), message));
            LOGGER.error(message,ex);
            return null;
        }
    }
    public MatchRespondDto uploadMatchesFromLeagueConfigList() {
        try {
            LeaguesListConfig leaguesList = new LeaguesListConfig();
            String season = seasonService.fetchSeason().getYear();
            deleteFixtures();
            for (Map.Entry<Integer, String> league : leaguesList.createList().entrySet()) {
                FixtureParam param = new FixtureParam(
                        league.getKey(), season, LocalDate.now().plusDays(TimeFrame.DAYS.getTimeFrame()));
                uploadMatches(param);
            }
            return new MatchRespondDto(Respond.ALL_MATCH_UPLOAD_OK.getRespond());
        } catch (Exception ex) {
            String message = ex.getMessage() + " --ERROR: Couldn't upload all Matches to DataBase-- ";
            logDataService.saveLog(new LogData(null, "LeagueConfigList"
                    , Code.MATCH_UPLOAD_ALL.getCode(), message));
            LOGGER.error(message,ex);
            return null;
        }
    }

    public List<Match> findByLeagueIdOrderByDate(int id) {
        return repository.findByLeagueIdOrderByDate(id);
    }
}
