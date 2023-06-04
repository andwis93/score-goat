package com.restapi.scoregoat.service;

import com.restapi.scoregoat.client.FootballClient;
import com.restapi.scoregoat.config.DateConfig;
import com.restapi.scoregoat.config.LeaguesListConfig;
import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.domain.client.mapJSON.FixturesList;
import com.restapi.scoregoat.mapper.MatchMapper;
import com.restapi.scoregoat.repository.MatchPredictionRepository;
import com.restapi.scoregoat.repository.MatchRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@EnableAspectJAutoProxy
public class MatchService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MatchService.class);
    private DateConfig dateConfig;
    private final MatchRepository repository;
    private MatchPredictionRepository matchPredictionRepository;
    private final MatchMapper mapper;
    private final FootballClient client;
    private final SeasonService seasonService;
    private final LogDataService logDataService;


    public Match findMatchByFixture(Long fixtureId) {
        if (repository.existsByFixtureId(fixtureId)) {
            return repository.findByFixtureId(fixtureId).orElseThrow(NoSuchElementException::new);
        } else {
            return null;
        }
    }

    public void deleteFixtures(){
        repository.deleteAll();
    }

    public MatchRespondDto uploadMatches(FixtureParam param) {
        try {
            FixturesList fixturesList = client.getFixtures(param);
            repository.saveAll(mapper.mapFixtureRespondToMatchList(fixturesList.getFixtureList()));
            return new MatchRespondDto(param.getId(),Respond.MATCH_UPLOAD_OK_LEAGUE.getRespond() + param.getId()
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
                        league.getKey(), season);
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

    public List<Match> findByLeagueIdOrderByDate(int leagueId) {
        return repository.findByLeagueIdOrderByDate(leagueId);
    }

    public List<Match> matchesWithDateRange(int leagueId) {
        List<Match> finalMatchList = repository.findByLeagueIdOrderByDate(leagueId);
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
            if (!matchPredictionRepository.existsMatchPredictionByUserIdAndFixtureId(userId, match.getFixtureId())) {
                finalMatchList.add(match);
            }
        }
        return finalMatchList;
    }
}
