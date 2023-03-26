package com.restapi.scoregoat.client;

import com.restapi.scoregoat.config.FootballConfig;
import com.restapi.scoregoat.domain.Code;
import com.restapi.scoregoat.domain.LogData;
import com.restapi.scoregoat.domain.ResponseDto;
import com.restapi.scoregoat.domain.SeasonDto;
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

    private URI uriForSeason(int id) {
        return UriComponentsBuilder.fromHttpUrl(uriBuild() + "/leagues?id=" + id)
                .build().encode().toUri();
    }

    private HttpEntity<Void> passHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.set(config.getFootballAppHeader(), config.getFootballAppKey());
        return new HttpEntity<>(headers);
    }

    public String getFootballSeason(int id) {
        try {
            ResponseEntity<ResponseDto> respond = restTemplate.exchange(uriForSeason(id),
                    HttpMethod.GET, passHeaders(), ResponseDto.class);

            return Collections.max(Objects.requireNonNull(respond.getBody()).getResponse()
                    .stream().flatMap(season -> season.getSeasonsDto().stream()).map(SeasonDto::getYear).toList());

        } catch (RestClientException ex) {
            String message = ex.getMessage() + "  --ERROR: Couldn't get season from Api Client-- ";
            logDataService.saveLog(new LogData(
                    null,"League ID: " + id, Code.SEASON_CLEAN.getCode(), message));
            LOGGER.error(message,ex);
            return null;
        }
    }
}
