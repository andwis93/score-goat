package com.restapi.scoregoat.service;

import com.restapi.scoregoat.client.FootballClient;
import com.restapi.scoregoat.controller.exception.SeasonNotFoundException;
import com.restapi.scoregoat.domain.Season;
import com.restapi.scoregoat.repository.SeasonRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SeasonDbService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SeasonDbService.class);
    @Autowired
    private SeasonRepository repository;
    private final FootballClient footballClient;

    public Season setSeason(Long id) throws SeasonNotFoundException {
        if (footballClient.getFootballSeason(id) != null) {
            Season season = new Season(footballClient.getFootballSeason(id));
            repository.deleteAll();
            repository.save(season);
            return season;
        } else {
            throw new SeasonNotFoundException();
        }
    }
}
