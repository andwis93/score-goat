package com.restapi.scoregoat.service.ClientService;

import com.restapi.scoregoat.client.FootballClient;
import com.restapi.scoregoat.config.DateConfig;
import com.restapi.scoregoat.config.LeaguesListConfig;
import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.domain.client.mapJSON.FixturesList;
import com.restapi.scoregoat.mapper.MatchMapper;
import com.restapi.scoregoat.repository.MatchPredictionRepository;
import com.restapi.scoregoat.service.DBService.LogDataDBService;
import com.restapi.scoregoat.service.DBService.MatchDBService;
import com.restapi.scoregoat.service.DBService.MatchPredictionDBService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;


@AllArgsConstructor
@Service
@EnableAspectJAutoProxy
public class MatchClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MatchClientService.class);
    private DateConfig dateConfig;
    private final MatchDBService service;
    private final MatchPredictionDBService predictionDBService;
    private final MatchMapper mapper;
    private final FootballClient client;
    private final SeasonClientService seasonService;
    private final LogDataDBService logDataService;

    public MatchRespondDto uploadMatches(FixtureParam param) {
        try {
            FixturesList fixturesList = client.getFixtures(param);
            service.saveAll(mapper.mapFixtureRespondToMatchList(fixturesList.getFixtureList()));
            return new MatchRespondDto(param.getId(),Respond.MATCH_UPLOAD_OK_LEAGUE.getRespond() + param.getId()
                    + Respond.MATCH_UPLOAD_OK_SEASON.getRespond() + param.getSeason());
        }catch (Exception ex) {
            String message = ex.getMessage() + " --ERROR: Couldn't upload Matches to DataBase-- ";
            logDataService.saveLog(new LogData(null,"League ID: "
                    + param.getId(), Code.MATCH_UPLOAD_ERROR.getCode(), message));
            LOGGER.error(message,ex);
            return null;
        }
    }
    public MatchRespondDto uploadMatchesFromLeagueConfigList() {
        try {
            LeaguesListConfig leaguesList = new LeaguesListConfig();
            String season = seasonService.fetchSeason().getYear();
            service.deleteFixtures();
            for (Map.Entry<Integer, String> league : leaguesList.getLeagueList().entrySet()) {
                FixtureParam param = new FixtureParam(
                        league.getKey(), season);
                uploadMatches(param);
            }
            return new MatchRespondDto(Respond.ALL_MATCH_UPLOAD_OK.getRespond());
        } catch (Exception ex) {
            String message = ex.getMessage() + " --ERROR: Couldn't upload all Matches to DataBase-- ";
            logDataService.saveLog(new LogData(null, "LeagueConfigList"
                    , Code.MATCH_UPLOAD_ALL_ERROR.getCode(), message));
            LOGGER.error(message,ex);
            return null;
        }
    }

    public List<Match> findByLeagueIdOrderByDate(int leagueId) {
        return service.findByLeagueIdOrderByDate(leagueId);
    }

    public List<Match> matchesWithDateRange(int leagueId) {
        List<Match> finalMatchList = service.findByLeagueIdOrderByDate(leagueId);
        return finalMatchList.stream().filter(match -> match.getDate().isAfter(dateConfig.getFrom())
                && match.getDate().isBefore(dateConfig.getTo())).collect(Collectors.toList());
    }

//    public List<Match> matchesNotStarted(int leagueId) {
//        return matchesWithDateRange(leagueId).stream().filter(match ->
//                match.getStatus().equals(MatchStatusType.NOT_STARTED.getType())).toList();
//    }

    public List<Match> eliminateSelected(Long userId, int leagueId){
        List<Match> finalMatchList = new ArrayList<>();
        //  for (Match match: matchesNotStarted(leagueId)) {
        for (Match match: findByLeagueIdOrderByDate(leagueId)) {
            if (!predictionDBService.existsMatchPredictionByUserIdAndFixtureId(userId, match.getFixtureId())) {
                finalMatchList.add(match);
            }
        }
        return finalMatchList;
    }
}
