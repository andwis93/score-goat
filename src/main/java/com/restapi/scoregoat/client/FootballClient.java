package com.restapi.scoregoat.client;

import com.restapi.scoregoat.config.FootballConfig;
import com.restapi.scoregoat.domain.ResponseDto;
import com.restapi.scoregoat.domain.SeasonDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Comparator;

@Component
@RequiredArgsConstructor
public class FootballClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(FootballClient.class);
    private final RestTemplate restTemplate;
    private final FootballConfig config;

    private URI uriBuild(String uri) {
        try {
            return UriComponentsBuilder.fromHttpUrl(config.getFootballApiEndpoint() + uri)
                    .build()
                    .encode()
                    .toUri();
        } catch (Exception err) {
            LOGGER.error(err.getMessage() + " --Incorrect uriBuild address-- ", err);
            throw new IllegalArgumentException(err.getMessage(), err);
        }
    }

    public String getFootballSeason(Long id) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-rapidapi-key", config.getFootballAppKey());

            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseDto respond = restTemplate.exchange(uriBuild("/leagues?id=" + id), HttpMethod.GET, entity, ResponseDto.class).getBody();

            assert respond != null;
            return respond.getResponse().stream()
                    .flatMap(season->season.getSeasonsDto().stream()).map(SeasonDto::getYear)
                    .max(Comparator.comparing(String::valueOf)).orElse(null);
        }catch (RestClientException ex) {
            LOGGER.error(ex.getMessage(),ex);
            return null;
        }
    }
}
