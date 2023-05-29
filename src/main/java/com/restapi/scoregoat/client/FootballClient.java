package com.restapi.scoregoat.client;

import com.restapi.scoregoat.config.FootballConfig;
import com.restapi.scoregoat.config.SeasonConfig;
import com.restapi.scoregoat.domain.Code;
import com.restapi.scoregoat.domain.FixtureParam;
import com.restapi.scoregoat.domain.LogData;
import com.restapi.scoregoat.domain.client.mapJSON.FixturesList;
import com.restapi.scoregoat.domain.client.mapJSON.Seasons;
import com.restapi.scoregoat.domain.client.mapJSON.SeasonsList;
import com.restapi.scoregoat.domain.client.mapJSON.Year;
import com.restapi.scoregoat.service.LogDataService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.*;

@Component
@RequiredArgsConstructor
public class FootballClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(FootballClient.class);
    private final LogDataService logDataService;
    private final RestTemplate restTemplate;
    private final FootballConfig config;

    private URI uriBuild() {
        try {
            return UriComponentsBuilder.fromHttpUrl(config.getFootballApiEndpoint())
                    .build().encode().toUri();
        } catch (Exception err) {
            LOGGER.error(err.getMessage() + " --Incorrect URI address-- ", err);
            throw new IllegalArgumentException(err.getMessage(), err);
        }
    }

    private URI uriForSeason() {
        return UriComponentsBuilder.fromHttpUrl(uriBuild() + "/leagues")
                .queryParam("id", SeasonConfig.DEFAULT_LEAGUE.getId())
                .build().encode().toUri();
    }

    private URI uriForFixture(FixtureParam param) {
        return UriComponentsBuilder.fromHttpUrl(uriBuild() + "/fixtures")
                .queryParam("league", param.getId())
                .queryParam("season", param.getSeason())
                .build().encode().toUri();
    }

    private HttpEntity<Void> passHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.set(config.getFootballAppHeader(), config.getFootballAppKey());
        return new HttpEntity<>(headers);
    }

    public String getFootballSeason() {
        try {
            ResponseEntity<SeasonsList> responseEntity =
                    restTemplate.exchange(
                            uriForSeason(), HttpMethod.GET, passHeaders(), SeasonsList.class);
            List<Year> yearList = Objects.requireNonNull(responseEntity.getBody())
                    .getSeasonsLists().stream().map(Seasons::getYearsList).flatMap(Collection::stream).toList();
            return Collections.max(yearList.stream().map(Year::getYear).toList());
        } catch (RestClientException ex) {
            String message = ex.getMessage() + "  --ERROR: Couldn't get season from Api Client-- ";
            logDataService.saveLog(new LogData(
                    null,"League ID: " + SeasonConfig.DEFAULT_LEAGUE.getId(), Code.SEASON_CLEAN.getCode(), message));
            LOGGER.error(message,ex);
            return null;
        }
    }

    public FixturesList getFixtures(FixtureParam param) {
        try {
            ResponseEntity<FixturesList> respond = restTemplate.exchange(uriForFixture(param),
                    HttpMethod.GET, passHeaders(), FixturesList.class);
            return respond.getBody();

        } catch (RestClientException ex) {
            String message = ex.getMessage() + "  --ERROR: Couldn't get fixtures from Api Client-- ";
            logDataService.saveLog(new LogData(
                    null,"League ID: " + param.getId(), Code.FIXTURE_GET.getCode(), message));
            LOGGER.error(message,ex);
            return null;
        }
    }
}
